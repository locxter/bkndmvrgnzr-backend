package com.github.locxter.bkndmvrgnzr.backend.book.api

import com.github.locxter.bkndmvrgnzr.backend.book.db.Book
import com.github.locxter.bkndmvrgnzr.backend.book.db.BookRepository
import com.github.locxter.bkndmvrgnzr.backend.book.db.Isbn
import com.github.locxter.bkndmvrgnzr.backend.bookcontributor.db.BookContributor
import com.github.locxter.bkndmvrgnzr.backend.bookcontributor.db.BookContributorId
import com.github.locxter.bkndmvrgnzr.backend.bookcontributor.db.BookContributorRepository
import com.github.locxter.bkndmvrgnzr.backend.contributor.db.ContributorId
import com.github.locxter.bkndmvrgnzr.backend.contributor.db.ContributorRepository
import com.github.locxter.bkndmvrgnzr.backend.genre.db.Genre
import com.github.locxter.bkndmvrgnzr.backend.genre.db.GenreId
import com.github.locxter.bkndmvrgnzr.backend.genre.db.GenreRepository
import com.github.locxter.bkndmvrgnzr.backend.publishinghouse.db.PublishingHouse
import com.github.locxter.bkndmvrgnzr.backend.publishinghouse.db.PublishingHouseId
import com.github.locxter.bkndmvrgnzr.backend.publishinghouse.db.PublishingHouseRepository
import com.github.locxter.bkndmvrgnzr.backend.user.db.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/book")
class BookController(
    private val bookRepository: BookRepository,
    private val bookContributorRepository: BookContributorRepository,
    private val userRepository: UserRepository,
    private val publishingHouseRepository: PublishingHouseRepository,
    private val genreRepository: GenreRepository,
    private val contributorRepository: ContributorRepository
) {
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    fun getAllBooks(): List<BookResponseDto> {
        val books = bookRepository.findAll()
        return books.map { it.toDto() }
    }

    @PostMapping
    @PreAuthorize("hasRole('EDITOR')")
    fun createBook(@RequestBody bookCreateDto: BookCreateDto): BookResponseDto {
        if (bookCreateDto.isbn.isBlank() || bookCreateDto.title.isBlank() || bookCreateDto.publishingHouseId.isBlank() ||
            bookCreateDto.year < 0 || bookCreateDto.pages < 0
        ) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Sent book not valid")
        }
        try {
            Isbn(bookCreateDto.isbn)
        } catch (exception: IllegalArgumentException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Sent book not valid")
        }
        if (bookRepository.existsById(Isbn(bookCreateDto.isbn))) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Sent book not valid")
        }
        val publishingHouse =
            publishingHouseRepository.findById(PublishingHouseId(bookCreateDto.publishingHouseId)).orElse(null)
                ?: throw ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Requested publishing house not found"
                )
        val genres: ArrayList<Genre> = ArrayList()
        for (genreId in bookCreateDto.genreIds) {
            val genre = genreRepository.findById(GenreId(genreId)).orElse(null) ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND, "Requested genre not found"
            )
            genres.add(genre)
        }
        val bookContributors: ArrayList<BookContributor> = ArrayList()
        for (bookContributorId in bookCreateDto.bookContributorIds) {
            val bookContributor =
                bookContributorRepository.findById(BookContributorId(bookContributorId)).orElse(null)
                    ?: throw ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Requested book contributor not found"
                    )
            bookContributors.add(bookContributor)
        }
        val book = Book(
            Isbn(bookCreateDto.isbn),
            bookCreateDto.title,
            bookCreateDto.subtitle,
            bookCreateDto.description,
            bookCreateDto.year,
            bookCreateDto.pages,
            publishingHouse,
            genres,
            bookContributors
        )
        bookRepository.save(book)
        return book.toDto()
    }

    @GetMapping("/{isbn}")
    @PreAuthorize("hasRole('USER')")
    fun getBook(@PathVariable(name = "isbn") isbn: String): BookResponseDto {
        val book = bookRepository.findById(Isbn(isbn)).orElse(null) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Requested book not found"
        )
        return book.toDto()
    }

    @PutMapping("/{isbn}")
    @PreAuthorize("hasRole('EDITOR')")
    fun updateBook(
        @PathVariable(name = "isbn") isbn: String,
        @RequestBody bookUpdateDto: BookUpdateDto
    ): BookResponseDto {
        val book = bookRepository.findById(Isbn(isbn)).orElse(null) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Requested book not found"
        )
        if (bookUpdateDto.year < 0 || bookUpdateDto.pages < 0) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Sent book not valid")
        }
        var publishingHouse: PublishingHouse = PublishingHouse()
        if (bookUpdateDto.publishingHouseId.isNotBlank()) {
            publishingHouse =
                publishingHouseRepository.findById(PublishingHouseId(bookUpdateDto.publishingHouseId)).orElse(null)
                    ?: throw ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Requested genre not found"
                    )
        }
        val genres: ArrayList<Genre> = ArrayList()
        for (genreId in bookUpdateDto.genreIds) {
            val genre = genreRepository.findById(GenreId(genreId)).orElse(null) ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND, "Requested genre not found"
            )
            genres.add(genre)
        }
        val bookContributors: ArrayList<BookContributor> = ArrayList()
        for (bookContributorId in bookUpdateDto.bookContributorIds) {
            val bookContributor =
                bookContributorRepository.findById(BookContributorId(bookContributorId)).orElse(null)
                    ?: throw ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Requested book contributor not found"
                    )
            bookContributors.add(bookContributor)
        }
        val updatedBook = book.copy(
            title = bookUpdateDto.title.ifBlank { book.title },
            subtitle = bookUpdateDto.subtitle.ifBlank { book.subtitle },
            description = bookUpdateDto.description.ifBlank { book.description },
            year = if (bookUpdateDto.year != 0) bookUpdateDto.year else book.year,
            pages = if (bookUpdateDto.pages != 0) bookUpdateDto.pages else book.pages,
            publishingHouse = if (bookUpdateDto.publishingHouseId.isNotBlank()) publishingHouse else book.publishingHouse,
            genres = genres.ifEmpty { book.genres },
            bookContributors = bookContributors.ifEmpty { book.bookContributors }
        )
        bookRepository.save(updatedBook)
        return updatedBook.toDto()
    }

    @DeleteMapping("/{isbn}")
    @PreAuthorize("hasRole('EDITOR')")
    fun deleteBook(@PathVariable(name = "isbn") isbn: String): BookResponseDto {
        val book = bookRepository.findById(Isbn(isbn)).orElse(null) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Requested book not found"
        )
        val bookDto = book.toDto()
        bookRepository.delete(book)
        return bookDto
    }

    @GetMapping("/genre/{genre-id}")
    @PreAuthorize("hasRole('USER')")
    fun getAllBooksOfGenre(@PathVariable(name = "genre-id") genreId: String): List<BookResponseDto> {
        val genre = genreRepository.findById(GenreId(genreId)).orElse(null) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Requested genre not found"
        )
        val books = bookRepository.findByGenresId(genre.id)
        return books.map { it.toDto() }
    }

    @GetMapping("/publishing-house/{publishing-house-id}")
    @PreAuthorize("hasRole('USER')")
    fun getAllBooksOfPublishingHouse(@PathVariable(name = "publishing-house-id") publishingHouseId: String): List<BookResponseDto> {
        val publishingHouse = publishingHouseRepository.findById(PublishingHouseId(publishingHouseId)).orElse(null)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Requested publishing house not found")
        val books = bookRepository.findByPublishingHouseId(publishingHouse.id)
        return books.map { it.toDto() }
    }

    @GetMapping("/book-contributor/{book-contributor-id}")
    @PreAuthorize("hasRole('USER')")
    fun getAllBooksOfBookContributor(@PathVariable(name = "book-contributor-id") bookContributorId: String): List<BookResponseDto> {
        val bookContributor = bookContributorRepository.findById(BookContributorId(bookContributorId)).orElse(null)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Requested book contributor not found")
        val books = bookRepository.findByBookContributorsId(bookContributor.id)
        return books.map { it.toDto() }
    }

    @GetMapping("/contributor/{contributor-id}")
    @PreAuthorize("hasRole('USER')")
    fun getAllBooksOfContributor(@PathVariable(name = "contributor-id") contributorId: String): List<BookResponseDto> {
        val contributor = contributorRepository.findById(ContributorId(contributorId)).orElse(null)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Requested contributor not found")
        val bookContributors = bookContributorRepository.findByContributorId(contributor.id)
        val books: ArrayList<Book> = ArrayList()
        for (bookContributor in bookContributors) {
            books.addAll(bookRepository.findByBookContributorsId(bookContributor.id))
        }
        return books.map { it.toDto() }
    }

    @GetMapping("/search/{query}")
    @PreAuthorize("hasRole('USER')")
    fun getAllBooksOfSearchQuery(@PathVariable(name = "query") query: String): List<BookResponseDto> {
        val books = bookRepository.findAll()
        val iterator = books.iterator()
        while(iterator.hasNext()) {
            val book = iterator.next()
            var containsQuery = false
            if (book.title.contains(query, true) || book.subtitle.contains(query, true) ||
                book.description.contains(query, true) || book.year == query.toIntOrNull() ||
                book.pages == query.toIntOrNull() || book.publishingHouse.name.contains(query, true)
            ) {
                containsQuery = true
            } else {
                for (genre in book.genres) {
                    if (genre.name.contains(query, true)) {
                        containsQuery = true
                        break
                    }
                }
                for (bookContributor in book.bookContributors) {
                    if (bookContributor.contributor.firstName.contains(query, true) ||
                        bookContributor.contributor.lastName.contains(query, true) ||
                        bookContributor.bookRole.name.contains(query, true)
                    ) {
                        containsQuery = true
                        break
                    }
                }
            }
            if (!containsQuery) {
                iterator.remove()
            }
        }
        return books.map { it.toDto() }
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    fun getAllBooksOfUser(authentication: Authentication): List<BookResponseDto> {
        val user = userRepository.findByUsername(authentication.name) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Requested user not found"
        )
        val books = bookRepository.findByUsersId(user.id)
        return books.map { it.toDto() }
    }

    @PostMapping("/user/{isbn}")
    @PreAuthorize("hasRole('USER')")
    fun addBookToUser(
        authentication: Authentication,
        @PathVariable(name = "isbn") isbn: String
    ): List<BookResponseDto> {
        val user = userRepository.findByUsername(authentication.name) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Requested user not found"
        )
        val book = bookRepository.findById(Isbn(isbn)).orElse(null) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Requested book not found"
        )
        val userBooks = user.books.toMutableList()
        userBooks.add(book)
        val updatedUser = user.copy(books = userBooks)
        userRepository.save(updatedUser)
        return userBooks.map { it.toDto() }
    }

    @DeleteMapping("/user/{isbn}")
    @PreAuthorize("hasRole('USER')")
    fun removeBookFromUser(
        authentication: Authentication,
        @PathVariable(name = "isbn") isbn: String
    ): List<BookResponseDto> {
        val user = userRepository.findByUsername(authentication.name) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Requested user not found"
        )
        val book = bookRepository.findById(Isbn(isbn)).orElse(null) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Requested book not found"
        )
        val userBooks = user.books.toMutableList()
        userBooks.remove(book)
        val updatedUser = user.copy(books = userBooks)
        userRepository.save(updatedUser)
        return userBooks.map { it.toDto() }
    }

    @GetMapping("/user/genre/{genre-id}")
    @PreAuthorize("hasRole('USER')")
    fun getAllBooksOfGenreFromUser(
        authentication: Authentication,
        @PathVariable(name = "genre-id") genreId: String
    ): List<BookResponseDto> {
        val user = userRepository.findByUsername(authentication.name) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Requested user not found"
        )
        val genre = genreRepository.findById(GenreId(genreId)).orElse(null) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Requested genre not found"
        )
        val books = bookRepository.findByUsersId(user.id).toMutableList()
        val iterator = books.iterator()
        while(iterator.hasNext()) {
            val book = iterator.next()
            if (!book.genres.contains(genre)) {
                iterator.remove()
            }
        }
        return books.map { it.toDto() }
    }

    @GetMapping("/user/publishing-house/{publishing-house-id}")
    @PreAuthorize("hasRole('USER')")
    fun getAllBooksOfPublishingHouseFromUser(
        authentication: Authentication,
        @PathVariable(name = "publishing-house-id") publishingHouseId: String
    ): List<BookResponseDto> {
        val user = userRepository.findByUsername(authentication.name) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Requested user not found"
        )
        val publishingHouse = publishingHouseRepository.findById(PublishingHouseId(publishingHouseId)).orElse(null)
            ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Requested publishing house not found"
            )
        val books = bookRepository.findByUsersId(user.id).toMutableList()
        val iterator = books.iterator()
        while(iterator.hasNext()) {
            val book = iterator.next()
            if (book.publishingHouse != publishingHouse) {
                iterator.remove()
            }
        }
        return books.map { it.toDto() }
    }

    @GetMapping("/user/book-contributor/{book-contributor-id}")
    @PreAuthorize("hasRole('USER')")
    fun getAllBooksOfBookContributorFromUser(
        authentication: Authentication,
        @PathVariable(name = "book-contributor-id") bookContributorId: String
    ): List<BookResponseDto> {
        val user = userRepository.findByUsername(authentication.name) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Requested user not found"
        )
        val bookContributor = bookContributorRepository.findById(BookContributorId(bookContributorId)).orElse(null)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Requested book contributor not found")
        val books = bookRepository.findByUsersId(user.id).toMutableList()
        val iterator = books.iterator()
        while(iterator.hasNext()) {
            val book = iterator.next()
            if (!book.bookContributors.contains(bookContributor)) {
                iterator.remove()
            }
        }
        return books.map { it.toDto() }
    }

    @GetMapping("/user/contributor/{contributor-id}")
    @PreAuthorize("hasRole('USER')")
    fun getAllBooksOfContributorFromUser(
        authentication: Authentication,
        @PathVariable(name = "contributor-id") contributorId: String
    ): List<BookResponseDto> {
        val user = userRepository.findByUsername(authentication.name) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Requested user not found"
        )
        val contributor = contributorRepository.findById(ContributorId(contributorId)).orElse(null)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Requested contributor not found")
        val bookContributors = bookContributorRepository.findByContributorId(contributor.id)
        val books = bookRepository.findByUsersId(user.id).toMutableList()
        val iterator = books.iterator()
        while(iterator.hasNext()) {
            val book = iterator.next()
            var containsBookContributor = false
            for (bookContributor in bookContributors) {
                if (book.bookContributors.contains(bookContributor)) {
                    containsBookContributor = true
                    break
                }
            }
            if (!containsBookContributor) {
                iterator.remove()
            }
        }
        return books.map { it.toDto() }
    }

    @GetMapping("/user/search/{query}")
    @PreAuthorize("hasRole('USER')")
    fun getAllBooksOfSearchQueryFromUser(
        authentication: Authentication,
        @PathVariable(name = "query") query: String
    ): List<BookResponseDto> {
        val user = userRepository.findByUsername(authentication.name) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Requested user not found"
        )
        val books = bookRepository.findByUsersId(user.id).toMutableList()
        val iterator = books.iterator()
        while(iterator.hasNext()) {
            var containsQuery = false
            val book = iterator.next()
            if (book.title.contains(query, true) || book.subtitle.contains(query, true) ||
                book.description.contains(query, true) || book.year == query.toIntOrNull() ||
                book.pages == query.toIntOrNull() || book.publishingHouse.name.contains(query, true)
            ) {
                containsQuery = true
            } else {
                for (genre in book.genres) {
                    if (genre.name.contains(query, true)) {
                        containsQuery = true
                        break
                    }
                }
                for (bookContributor in book.bookContributors) {
                    if (bookContributor.contributor.firstName.contains(query, true) ||
                        bookContributor.contributor.lastName.contains(query, true) ||
                        bookContributor.bookRole.name.contains(query, true)
                    ) {
                        containsQuery = true
                        break
                    }
                }
            }
            if (!containsQuery) {
                iterator.remove()
            }
        }
        return books.map { it.toDto() }
    }
}
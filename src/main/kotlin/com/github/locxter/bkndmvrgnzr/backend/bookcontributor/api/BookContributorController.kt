package com.github.locxter.bkndmvrgnzr.backend.bookcontributor.api

import com.github.locxter.bkndmvrgnzr.backend.bookcontributor.db.BookContributor
import com.github.locxter.bkndmvrgnzr.backend.bookcontributor.db.BookContributorId
import com.github.locxter.bkndmvrgnzr.backend.bookcontributor.db.BookContributorRepository
import com.github.locxter.bkndmvrgnzr.backend.bookrole.db.BookRole
import com.github.locxter.bkndmvrgnzr.backend.bookrole.db.BookRoleId
import com.github.locxter.bkndmvrgnzr.backend.bookrole.db.BookRoleRepository
import com.github.locxter.bkndmvrgnzr.backend.contributor.db.Contributor
import com.github.locxter.bkndmvrgnzr.backend.contributor.db.ContributorId
import com.github.locxter.bkndmvrgnzr.backend.contributor.db.ContributorRepository
import me.xdrop.fuzzywuzzy.FuzzySearch
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/book-contributor")
class BookContributorController(
    private val bookContributorRepository: BookContributorRepository,
    private val contributorRepository: ContributorRepository,
    private val bookRoleRepository: BookRoleRepository
) {
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    fun getAllBookContributors(): List<BookContributorResponseDto> {
        val bookContributors = bookContributorRepository.findAll(BookContributor.getSort())
        return bookContributors.map { it.toDto() }
    }

    @PostMapping
    @PreAuthorize("hasRole('EDITOR')")
    fun createBookContributor(@RequestBody bookContributorCreateDto: BookContributorCreateDto): BookContributorResponseDto {
        if (bookContributorCreateDto.contributorId.isBlank() || bookContributorCreateDto.bookRoleId.isBlank()) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Sent book contributor not valid")
        }
        val contributor =
            contributorRepository.findById(ContributorId(bookContributorCreateDto.contributorId)).orElse(null)
                ?: throw ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Requested contributor not found"
                )
        val bookRole = bookRoleRepository.findById(BookRoleId(bookContributorCreateDto.bookRoleId)).orElse(null)
            ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND, "Requested book role not found"
            )
        val bookContributor = BookContributor(
            contributor = contributor,
            bookRole = bookRole
        )
        bookContributorRepository.save(bookContributor)
        return bookContributor.toDto()
    }

    @GetMapping("/{bookContributorId}")
    @PreAuthorize("hasRole('USER')")
    fun getBookContributor(@PathVariable(name = "bookContributorId") bookContributorId: String): BookContributorResponseDto {
        val bookContributor = bookContributorRepository.findById(BookContributorId(bookContributorId)).orElse(null)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Requested book contributor not found")
        return bookContributor.toDto()
    }

    @PutMapping("/{bookContributorId}")
    @PreAuthorize("hasRole('EDITOR')")
    fun updateBookContributor(
        @PathVariable(name = "bookContributorId") bookContributorId: String,
        @RequestBody bookContributorUpdateDto: BookContributorUpdateDto
    ): BookContributorResponseDto {
        val bookContributor = bookContributorRepository.findById(BookContributorId(bookContributorId)).orElse(null)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Requested book contributor not found")
        var contributor = Contributor()
        var bookRole = BookRole()
        if (bookContributorUpdateDto.contributorId.isNotBlank()) {
            contributor =
                contributorRepository.findById(ContributorId(bookContributorUpdateDto.contributorId)).orElse(null)
                    ?: throw ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Requested contributor not found"
                    )
        }
        if (bookContributorUpdateDto.bookRoleId.isNotBlank()) {
            bookRole = bookRoleRepository.findById(BookRoleId(bookContributorUpdateDto.bookRoleId)).orElse(null)
                ?: throw ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Requested book role not found"
                )
        }
        val updatedBookContributor = bookContributor.copy(
            contributor = if (bookContributorUpdateDto.contributorId.isNotBlank()) contributor else bookContributor.contributor,
            bookRole = if (bookContributorUpdateDto.bookRoleId.isNotBlank()) bookRole else bookContributor.bookRole
        )
        bookContributorRepository.save(updatedBookContributor)
        return updatedBookContributor.toDto()
    }

    @DeleteMapping("/{bookContributorId}")
    @PreAuthorize("hasRole('EDITOR')")
    fun deleteBookContributor(@PathVariable(name = "bookContributorId") bookContributorId: String): BookContributorResponseDto {
        val bookContributor = bookContributorRepository.findById(BookContributorId(bookContributorId)).orElse(null)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Requested book contributor not found")
        val bookContributorDto = bookContributor.toDto()
        bookContributorRepository.delete(bookContributor)
        return bookContributorDto
    }

    @GetMapping("/contributor/{contributorId}")
    @PreAuthorize("hasRole('USER')")
    fun getAllBookContributorsOfContributor(@PathVariable(name = "contributorId") contributorId: String): List<BookContributorResponseDto> {
        val contributor = contributorRepository.findById(ContributorId(contributorId)).orElse(null)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Requested contributor not found")
        val bookContributors =
            bookContributorRepository.findByContributorId(contributor.id, BookContributor.getSort())
        return bookContributors.map { it.toDto() }
    }

    @GetMapping("/book-role/{bookRoleId}")
    @PreAuthorize("hasRole('USER')")
    fun getAllBookContributorsOfBookRole(@PathVariable(name = "bookRoleId") bookRoleId: String): List<BookContributorResponseDto> {
        val bookRole = bookRoleRepository.findById(BookRoleId(bookRoleId)).orElse(null)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Requested book role not found")
        val bookContributors =
            bookContributorRepository.findByBookRoleId(bookRole.id, BookContributor.getSort())
        return bookContributors.map { it.toDto() }
    }

    @GetMapping("/search/{query}")
    @PreAuthorize("hasRole('USER')")
    fun getAllBookContributorsOfSearchQuery(@PathVariable(name = "query") query: String): List<BookContributorResponseDto> {
        val bookContributors = bookContributorRepository.findAll(BookContributor.getSort())
        val iterator = bookContributors.iterator()
        while (iterator.hasNext()) {
            val bookContributor = iterator.next()
            var containsQuery = false
            if (FuzzySearch.weightedRatio(bookContributor.contributor.firstName, query) >= 90 ||
                FuzzySearch.weightedRatio(bookContributor.contributor.lastName, query) >= 90 ||
                FuzzySearch.weightedRatio(
                    bookContributor.contributor.firstName + ' ' + bookContributor.contributor.lastName,
                    query
                ) >= 90 ||
                FuzzySearch.weightedRatio(bookContributor.bookRole.name, query) >= 90
            ) {
                containsQuery = true
            }
            if (!containsQuery) {
                iterator.remove()
            }
        }
        return bookContributors.map { it.toDto() }
    }
}

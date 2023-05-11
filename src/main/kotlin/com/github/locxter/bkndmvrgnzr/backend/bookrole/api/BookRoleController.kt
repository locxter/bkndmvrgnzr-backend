package com.github.locxter.bkndmvrgnzr.backend.bookrole.api

import com.github.locxter.bkndmvrgnzr.backend.bookcontributor.db.BookContributorRepository
import com.github.locxter.bkndmvrgnzr.backend.bookrole.db.BookRole
import com.github.locxter.bkndmvrgnzr.backend.bookrole.db.BookRoleId
import com.github.locxter.bkndmvrgnzr.backend.bookrole.db.BookRoleRepository
import com.github.locxter.bkndmvrgnzr.backend.contributor.db.ContributorId
import com.github.locxter.bkndmvrgnzr.backend.contributor.db.ContributorRepository
import me.xdrop.fuzzywuzzy.FuzzySearch
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException


@RestController
@RequestMapping("/api/book-role")
class BookRoleController(
    private val bookRoleRepository: BookRoleRepository,
    private val bookContributorRepository: BookContributorRepository,
    private val contributorRepository: ContributorRepository
) {
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    fun getAllBookRoles(): List<BookRoleResponseDto> {
        val bookRoles = bookRoleRepository.findAll(BookRole.getSort())
        return bookRoles.map { it.toDto() }
    }

    @PostMapping
    @PreAuthorize("hasRole('EDITOR')")
    fun createBookRole(@RequestBody bookRoleCreateDto: BookRoleCreateDto): BookRoleResponseDto {
        if (bookRoleCreateDto.name.isBlank()) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Sent book role not valid")
        }
        val bookRole = BookRole(
            name = bookRoleCreateDto.name
        )
        bookRoleRepository.save(bookRole)
        return bookRole.toDto()
    }

    @GetMapping("/{bookRoleId}")
    @PreAuthorize("hasRole('USER')")
    fun getBookRole(@PathVariable(name = "bookRoleId") bookRoleId: String): BookRoleResponseDto {
        val bookRole =
            bookRoleRepository.findById(BookRoleId(bookRoleId)).orElse(null) ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Requested book role not found"
            )
        return bookRole.toDto()
    }

    @PutMapping("/{bookRoleId}")
    @PreAuthorize("hasRole('EDITOR')")
    fun updateBookRole(
        @PathVariable(name = "bookRoleId") bookRoleId: String,
        @RequestBody bookRoleUpdateDto: BookRoleUpdateDto
    ): BookRoleResponseDto {
        val bookRole =
            bookRoleRepository.findById(BookRoleId(bookRoleId)).orElse(null) ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Requested book role not found"
            )
        val updatedBookRole = bookRole.copy(
            name = bookRoleUpdateDto.name.ifBlank { bookRole.name }
        )
        bookRoleRepository.save(updatedBookRole)
        return updatedBookRole.toDto()
    }

    @DeleteMapping("/{bookRoleId}")
    @PreAuthorize("hasRole('EDITOR')")
    fun deleteBookRole(@PathVariable(name = "bookRoleId") bookRoleId: String): BookRoleResponseDto {
        val bookRole =
            bookRoleRepository.findById(BookRoleId(bookRoleId)).orElse(null) ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Requested book role not found"
            )
        val bookRoleDto = bookRole.toDto()
        bookRoleRepository.delete(bookRole)
        return bookRoleDto
    }

    @GetMapping("/contributor/{contributorId}")
    @PreAuthorize("hasRole('USER')")
    fun getAllBookRolesOfContributor(@PathVariable(name = "contributorId") contributorId: String): List<BookRoleResponseDto> {
        val contributor = contributorRepository.findById(ContributorId(contributorId)).orElse(null)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Requested contributor not found")
        val bookContributors =
            bookContributorRepository.findByContributorId(contributor.id, Sort.by(Sort.Direction.ASC, "bookRole.name"))
        val bookRoles: ArrayList<BookRole> = ArrayList()
        for (bookContributor in bookContributors) {
            bookRoles.add(
                bookRoleRepository.findByBookContributorsId(bookContributor.id) ?: throw ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Requested book role not found"
                )
            )
        }
        return bookRoles.map { it.toDto() }
    }

    @GetMapping("/search/{query}")
    @PreAuthorize("hasRole('USER')")
    fun getAllBookRolesOfSearchQuery(@PathVariable(name = "query") query: String): List<BookRoleResponseDto> {
        val bookRoles = bookRoleRepository.findAll(BookRole.getSort())
        val iterator = bookRoles.iterator()
        while (iterator.hasNext()) {
            val bookRole = iterator.next()
            var containsQuery = false
            if (FuzzySearch.weightedRatio(bookRole.name, query) >= 90) {
                containsQuery = true
            } else {
                for (bookContributor in bookRole.bookContributors) {
                    if (FuzzySearch.weightedRatio(bookContributor.contributor.firstName, query) >= 90 ||
                        FuzzySearch.weightedRatio(bookContributor.contributor.lastName, query) >= 90 ||
                        FuzzySearch.weightedRatio(
                            bookContributor.contributor.firstName + ' ' + bookContributor.contributor.lastName,
                            query
                        ) >= 90
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
        return bookRoles.map { it.toDto() }
    }
}

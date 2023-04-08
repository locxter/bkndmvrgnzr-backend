package com.github.locxter.bkndmvrgnzr.backend.bookrole.api

import com.github.locxter.bkndmvrgnzr.backend.bookrole.db.BookRole
import com.github.locxter.bkndmvrgnzr.backend.bookrole.db.BookRoleId
import com.github.locxter.bkndmvrgnzr.backend.bookrole.db.BookRoleRepository
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException


@RestController
@RequestMapping("/api/book-role")
class BookRoleController(private val bookRoleRepository: BookRoleRepository) {
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    fun getAllBookRoles(): List<BookRoleResponseDto> {
        val bookRoles = bookRoleRepository.findAll()
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

    @GetMapping("/{book-role-id}")
    @PreAuthorize("hasRole('USER')")
    fun getBookRole(@PathVariable(name = "book-role-id") bookRoleId: String): BookRoleResponseDto {
        val bookRole =
            bookRoleRepository.findById(BookRoleId(bookRoleId)).orElse(null) ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Requested book role not found"
            )
        return bookRole.toDto()
    }

    @PutMapping("/{book-role-id}")
    @PreAuthorize("hasRole('EDITOR')")
    fun updateBookRole(
        @PathVariable(name = "book-role-id") bookRoleId: String,
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

    @DeleteMapping("/{book-role-id}")
    @PreAuthorize("hasRole('EDITOR')")
    fun deleteBookRole(@PathVariable(name = "book-role-id") bookRoleId: String): BookRoleResponseDto {
        val bookRole =
            bookRoleRepository.findById(BookRoleId(bookRoleId)).orElse(null) ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Requested book role not found"
            )
        val bookRoleDto = bookRole.toDto()
        bookRoleRepository.delete(bookRole)
        return bookRoleDto
    }
}
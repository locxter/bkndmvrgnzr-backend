package com.github.locxter.bkndmvrgnzr.backend.movierole.api

import com.github.locxter.bkndmvrgnzr.backend.movierole.db.MovieRole
import com.github.locxter.bkndmvrgnzr.backend.movierole.db.MovieRoleId
import com.github.locxter.bkndmvrgnzr.backend.movierole.db.MovieRoleRepository
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/movie-role")
class MovieRoleController(private val movieRoleRepository: MovieRoleRepository) {
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    fun getAllMovieRoles(): List<MovieRoleResponseDto> {
        val movieRoles = movieRoleRepository.findAll()
        return movieRoles.map { it.toDto() }
    }

    @PostMapping
    @PreAuthorize("hasRole('EDITOR')")
    fun createMovieRole(@RequestBody movieRoleCreateDto: MovieRoleCreateDto): MovieRoleResponseDto {
        if (movieRoleCreateDto.name.isBlank()) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Sent movie role not valid")
        }
        val movieRole = MovieRole(
            name = movieRoleCreateDto.name
        )
        movieRoleRepository.save(movieRole)
        return movieRole.toDto()
    }

    @GetMapping("/{movie-role-id}")
    @PreAuthorize("hasRole('USER')")
    fun getMovieRole(@PathVariable(name = "movie-role-id") movieRoleId: String): MovieRoleResponseDto {
        val movieRole =
            movieRoleRepository.findById(MovieRoleId(movieRoleId)).orElse(null) ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Requested movie role not found"
            )
        return movieRole.toDto()
    }

    @PutMapping("/{movie-role-id}")
    @PreAuthorize("hasRole('EDITOR')")
    fun updateMovieRole(
        @PathVariable(name = "movie-role-id") movieRoleId: String,
        @RequestBody movieRoleUpdateDto: MovieRoleUpdateDto
    ): MovieRoleResponseDto {
        val movieRole =
            movieRoleRepository.findById(MovieRoleId(movieRoleId)).orElse(null) ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Requested movie role not found"
            )
        val updatedMovieRole = movieRole.copy(
            name = movieRoleUpdateDto.name.ifBlank { movieRole.name }
        )
        movieRoleRepository.save(updatedMovieRole)
        return updatedMovieRole.toDto()
    }

    @DeleteMapping("/{movie-role-id}")
    @PreAuthorize("hasRole('EDITOR')")
    fun deleteMovieRole(@PathVariable(name = "movie-role-id") movieRoleId: String): MovieRoleResponseDto {
        val movieRole =
            movieRoleRepository.findById(MovieRoleId(movieRoleId)).orElse(null) ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Requested movie role not found"
            )
        val movieRoleDto = movieRole.toDto()
        movieRoleRepository.delete(movieRole)
        return movieRoleDto
    }
}
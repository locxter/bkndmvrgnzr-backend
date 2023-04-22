package com.github.locxter.bkndmvrgnzr.backend.movierole.api

import com.github.locxter.bkndmvrgnzr.backend.movierole.db.MovieRole
import com.github.locxter.bkndmvrgnzr.backend.movierole.db.MovieRoleId
import com.github.locxter.bkndmvrgnzr.backend.movierole.db.MovieRoleRepository
import org.springframework.data.domain.Sort
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
        val movieRoles = movieRoleRepository.findAll(Sort.by(Sort.Direction.ASC, "name"))
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

    @GetMapping("/{movieRoleId}")
    @PreAuthorize("hasRole('USER')")
    fun getMovieRole(@PathVariable(name = "movieRoleId") movieRoleId: String): MovieRoleResponseDto {
        val movieRole =
            movieRoleRepository.findById(MovieRoleId(movieRoleId)).orElse(null) ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Requested movie role not found"
            )
        return movieRole.toDto()
    }

    @PutMapping("/{movieRoleId}")
    @PreAuthorize("hasRole('EDITOR')")
    fun updateMovieRole(
        @PathVariable(name = "movieRoleId") movieRoleId: String,
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

    @DeleteMapping("/{movieRoleId}")
    @PreAuthorize("hasRole('EDITOR')")
    fun deleteMovieRole(@PathVariable(name = "movieRoleId") movieRoleId: String): MovieRoleResponseDto {
        val movieRole =
            movieRoleRepository.findById(MovieRoleId(movieRoleId)).orElse(null) ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Requested movie role not found"
            )
        val movieRoleDto = movieRole.toDto()
        movieRoleRepository.delete(movieRole)
        return movieRoleDto
    }

    @GetMapping("/search/{query}")
    @PreAuthorize("hasRole('USER')")
    fun getAllMovieRolesOfSearchQuery(@PathVariable(name = "query") query: String): List<MovieRoleResponseDto> {
        val movieRoles = movieRoleRepository.findAll(Sort.by(Sort.Direction.ASC, "name"))
        val iterator = movieRoles.iterator()
        while (iterator.hasNext()) {
            val movieRole = iterator.next()
            var containsQuery = false
            if (movieRole.name.contains(query, true)) {
                containsQuery = true
            } else {
                for (movieContributor in movieRole.movieContributors) {
                    if (movieContributor.contributor.firstName.contains(query, true) ||
                        movieContributor.contributor.lastName.contains(query, true)
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
        return movieRoles.map { it.toDto() }
    }
}

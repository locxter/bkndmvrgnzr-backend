package com.github.locxter.bkndmvrgnzr.backend.moviecontributor.api

import com.github.locxter.bkndmvrgnzr.backend.contributor.db.Contributor
import com.github.locxter.bkndmvrgnzr.backend.contributor.db.ContributorId
import com.github.locxter.bkndmvrgnzr.backend.contributor.db.ContributorRepository
import com.github.locxter.bkndmvrgnzr.backend.moviecontributor.db.MovieContributor
import com.github.locxter.bkndmvrgnzr.backend.moviecontributor.db.MovieContributorId
import com.github.locxter.bkndmvrgnzr.backend.moviecontributor.db.MovieContributorRepository
import com.github.locxter.bkndmvrgnzr.backend.movierole.db.MovieRole
import com.github.locxter.bkndmvrgnzr.backend.movierole.db.MovieRoleId
import com.github.locxter.bkndmvrgnzr.backend.movierole.db.MovieRoleRepository
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/movie-contributor")
class MovieContributorController(
    private val movieContributorRepository: MovieContributorRepository,
    private val contributorRepository: ContributorRepository,
    private val movieRoleRepository: MovieRoleRepository
) {
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    fun getAllMovieContributors(): List<MovieContributorResponseDto> {
        val movieContributors = movieContributorRepository.findAll()
        return movieContributors.map { it.toDto() }
    }

    @PostMapping
    @PreAuthorize("hasRole('EDITOR')")
    fun createMovieContributor(@RequestBody movieContributorCreateDto: MovieContributorCreateDto): MovieContributorResponseDto {
        if (movieContributorCreateDto.contributorId.isBlank() || movieContributorCreateDto.movieRoleId.isBlank()) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Sent movie contributor not valid")
        }
        val contributor =
            contributorRepository.findById(ContributorId(movieContributorCreateDto.contributorId)).orElse(null)
                ?: throw ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Requested contributor not found"
                )
        val movieRole = movieRoleRepository.findById(MovieRoleId(movieContributorCreateDto.movieRoleId)).orElse(null)
            ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND, "Requested movie role not found"
            )
        val movieContributor = MovieContributor(
            contributor = contributor,
            movieRole = movieRole
        )
        movieContributorRepository.save(movieContributor)
        return movieContributor.toDto()
    }

    @GetMapping("/{movie-contributor-id}")
    @PreAuthorize("hasRole('USER')")
    fun getMovieContributor(@PathVariable(name = "movie-contributor-id") movieContributorId: String): MovieContributorResponseDto {
        val movieContributor =
            movieContributorRepository.findById(MovieContributorId(movieContributorId)).orElse(null)
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Requested movie contributor not found")
        return movieContributor.toDto()
    }

    @PutMapping("/{movie-contributor-id}")
    @PreAuthorize("hasRole('EDITOR')")
    fun updateMovieContributor(
        @PathVariable(name = "movie-contributor-id") movieContributorId: String,
        @RequestBody movieContributorUpdateDto: MovieContributorUpdateDto
    ): MovieContributorResponseDto {
        val movieContributor =
            movieContributorRepository.findById(MovieContributorId(movieContributorId)).orElse(null)
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Requested movie contributor not found")
        var contributor = Contributor()
        var movieRole = MovieRole()
        if (movieContributorUpdateDto.contributorId.isNotBlank()) {
            contributor =
                contributorRepository.findById(ContributorId(movieContributorUpdateDto.contributorId)).orElse(null)
                    ?: throw ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Requested contributor not found"
                    )
        }
        if (movieContributorUpdateDto.movieRoleId.isNotBlank()) {
            movieRole = movieRoleRepository.findById(MovieRoleId(movieContributorUpdateDto.movieRoleId)).orElse(null)
                ?: throw ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Requested movie role not found"
                )
        }
        val updatedMovieContributor = movieContributor.copy(
            contributor = if (movieContributorUpdateDto.contributorId.isNotBlank()) contributor else movieContributor.contributor,
            movieRole = if (movieContributorUpdateDto.movieRoleId.isNotBlank()) movieRole else movieContributor.movieRole
        )
        movieContributorRepository.save(updatedMovieContributor)
        return updatedMovieContributor.toDto()
    }

    @DeleteMapping("/{movie-contributor-id}")
    @PreAuthorize("hasRole('EDITOR')")
    fun deleteMovieContributor(@PathVariable(name = "movie-contributor-id") movieContributorId: String): MovieContributorResponseDto {
        val movieContributor =
            movieContributorRepository.findById(MovieContributorId(movieContributorId)).orElse(null)
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Requested movie contributor not found")
        val movieContributorDto = movieContributor.toDto()
        movieContributorRepository.delete(movieContributor)
        return movieContributorDto
    }

    @GetMapping("/contributor/{contributor-id}")
    @PreAuthorize("hasRole('USER')")
    fun getAllMovieContributorsOfContributor(@PathVariable(name = "contributor-id") contributorId: String): List<MovieContributorResponseDto> {
        val contributor = contributorRepository.findById(ContributorId(contributorId)).orElse(null)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Requested contributor not found")
        val movieContributors = movieContributorRepository.findByContributorId(contributor.id)
        return movieContributors.map { it.toDto() }
    }

    @GetMapping("/movie-role/{movie-role-id}")
    @PreAuthorize("hasRole('USER')")
    fun getAllMovieContributorsOfMovieRole(@PathVariable(name = "movie-role-id") movieRoleId: String): List<MovieContributorResponseDto> {
        val movieRole = movieRoleRepository.findById(MovieRoleId(movieRoleId)).orElse(null)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Requested movie role not found")
        val movieContributors = movieContributorRepository.findByMovieRoleId(movieRole.id)
        return movieContributors.map { it.toDto() }
    }
}
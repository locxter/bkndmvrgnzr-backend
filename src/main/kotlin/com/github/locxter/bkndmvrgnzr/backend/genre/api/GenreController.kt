package com.github.locxter.bkndmvrgnzr.backend.genre.api

import com.github.locxter.bkndmvrgnzr.backend.genre.db.Genre
import com.github.locxter.bkndmvrgnzr.backend.genre.db.GenreId
import com.github.locxter.bkndmvrgnzr.backend.genre.db.GenreRepository
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/genre")
class GenreController(private val genreRepository: GenreRepository) {
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    fun getAllGenres(): List<GenreResponseDto> {
        val genres = genreRepository.findAll(Sort.by(Sort.Direction.ASC, "name"))
        return genres.map { it.toDto() }
    }

    @PostMapping
    @PreAuthorize("hasRole('EDITOR')")
    fun createGenre(@RequestBody genreCreateDto: GenreCreateDto): GenreResponseDto {
        if (genreCreateDto.name.isBlank()) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Sent genre not valid")
        }
        val genre = Genre(
            name = genreCreateDto.name
        )
        genreRepository.save(genre)
        return genre.toDto()
    }

    @GetMapping("/{genreId}")
    @PreAuthorize("hasRole('USER')")
    fun getGenre(@PathVariable(name = "genreId") genreId: String): GenreResponseDto {
        val genre = genreRepository.findById(GenreId(genreId)).orElse(null) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Requested genre not found"
        )
        return genre.toDto()
    }

    @PutMapping("/{genreId}")
    @PreAuthorize("hasRole('EDITOR')")
    fun updateGenre(
        @PathVariable(name = "genreId") genreId: String,
        @RequestBody genreUpdateDto: GenreUpdateDto
    ): GenreResponseDto {
        val genre = genreRepository.findById(GenreId(genreId)).orElse(null) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Requested genre not found"
        )
        val updatedGenre = genre.copy(
            name = genreUpdateDto.name.ifBlank { genre.name }
        )
        genreRepository.save(updatedGenre)
        return updatedGenre.toDto()
    }

    @DeleteMapping("/{genreId}")
    @PreAuthorize("hasRole('EDITOR')")
    fun deleteGenre(@PathVariable(name = "genreId") genreId: String): GenreResponseDto {
        val genre = genreRepository.findById(GenreId(genreId)).orElse(null) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Requested genre not found"
        )
        val genreDto = genre.toDto()
        genreRepository.delete(genre)
        return genreDto
    }
}

package com.github.locxter.bkndmvrgnzr.backend.genre.api

import com.github.locxter.bkndmvrgnzr.backend.book.api.BookResponseBriefDto
import com.github.locxter.bkndmvrgnzr.backend.movie.api.MovieResponseBriefDto

data class GenreResponseDto(
    val id: String = "",
    val name: String = "",
    val books: List<BookResponseBriefDto> = listOf(),
    val movies: List<MovieResponseBriefDto> = listOf()
)

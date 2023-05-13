package com.github.locxter.bkndmvrgnzr.backend.user.api

import com.github.locxter.bkndmvrgnzr.backend.book.api.BookResponseBriefDto
import com.github.locxter.bkndmvrgnzr.backend.movie.api.MovieResponseBriefDto
import com.github.locxter.bkndmvrgnzr.backend.role.api.RoleResponseDto

data class UserResponseDto(
    val id: String = "",
    val username: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val roles: List<RoleResponseDto> = mutableListOf(),
    val books: List<BookResponseBriefDto> = mutableListOf(),
    val movies: List<MovieResponseBriefDto> = mutableListOf()
)

package com.github.locxter.bkndmvrgnzr.backend.movie.api

data class MovieUpdateDto(
    val title: String = "",
    val description: String = "",
    val year: Int = 0,
    val playTime: Int = 0,
    val ageRestriction: Int = -1,
    val genreIds: List<String> = mutableListOf(),
    val movieContributorIds: List<String> = mutableListOf(),
)

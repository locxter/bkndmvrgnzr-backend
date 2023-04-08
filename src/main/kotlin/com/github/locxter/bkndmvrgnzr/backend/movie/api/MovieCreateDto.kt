package com.github.locxter.bkndmvrgnzr.backend.movie.api

data class MovieCreateDto(
    val isan: String = "",
    val title: String = "",
    val description: String = "",
    val year: Int = 0,
    val playTime: Int = 0,
    val ageRestriction: Int = 0,
    val genreIds: List<String> = ArrayList(),
    val movieContributorIds: List<String> = ArrayList(),
)
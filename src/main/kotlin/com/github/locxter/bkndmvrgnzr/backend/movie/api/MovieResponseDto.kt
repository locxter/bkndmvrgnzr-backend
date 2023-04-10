package com.github.locxter.bkndmvrgnzr.backend.movie.api

import com.github.locxter.bkndmvrgnzr.backend.genre.api.GenreResponseBriefDto
import com.github.locxter.bkndmvrgnzr.backend.moviecontributor.api.MovieContributorResponseBriefDto

data class MovieResponseDto(
    val isan: String = "",
    val title: String = "",
    val description: String = "",
    val year: Int = 0,
    val playTime: Int = 0,
    val ageRestriction: Int = 0,
    val genres: List<GenreResponseBriefDto> = ArrayList(),
    val movieContributors: List<MovieContributorResponseBriefDto> = ArrayList()
)

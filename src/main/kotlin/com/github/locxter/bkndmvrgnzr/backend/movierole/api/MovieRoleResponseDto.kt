package com.github.locxter.bkndmvrgnzr.backend.movierole.api

import com.github.locxter.bkndmvrgnzr.backend.moviecontributor.api.MovieContributorResponseBriefDto

data class MovieRoleResponseDto(
    val id: String = "",
    val name: String = "",
    val movieContributors: List<MovieContributorResponseBriefDto> = listOf()
)

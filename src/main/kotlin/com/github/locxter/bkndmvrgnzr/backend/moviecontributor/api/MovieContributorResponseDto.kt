package com.github.locxter.bkndmvrgnzr.backend.moviecontributor.api

import com.github.locxter.bkndmvrgnzr.backend.contributor.api.ContributorResponseBriefDto
import com.github.locxter.bkndmvrgnzr.backend.movie.api.MovieResponseBriefDto
import com.github.locxter.bkndmvrgnzr.backend.movierole.api.MovieRoleResponseBriefDto

data class MovieContributorResponseDto(
    val id: String = "",
    val contributor: ContributorResponseBriefDto = ContributorResponseBriefDto(),
    val movieRole: MovieRoleResponseBriefDto = MovieRoleResponseBriefDto(),
    val movies: List<MovieResponseBriefDto> = ArrayList()
)

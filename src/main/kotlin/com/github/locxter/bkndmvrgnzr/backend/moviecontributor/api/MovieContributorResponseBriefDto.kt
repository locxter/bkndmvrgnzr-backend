package com.github.locxter.bkndmvrgnzr.backend.moviecontributor.api

import com.github.locxter.bkndmvrgnzr.backend.contributor.api.ContributorResponseBriefDto
import com.github.locxter.bkndmvrgnzr.backend.movierole.api.MovieRoleResponseBriefDto

data class MovieContributorResponseBriefDto(
    val id: String = "",
    val contributor: ContributorResponseBriefDto = ContributorResponseBriefDto(),
    val movieRole: MovieRoleResponseBriefDto = MovieRoleResponseBriefDto(),
)

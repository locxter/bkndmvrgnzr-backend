package com.github.locxter.bkndmvrgnzr.backend.contributor.api

import com.github.locxter.bkndmvrgnzr.backend.bookcontributor.api.BookContributorResponseBriefDto
import com.github.locxter.bkndmvrgnzr.backend.moviecontributor.api.MovieContributorResponseBriefDto

data class ContributorResponseDto(
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val birthYear: Int = 0,
    val birthMonth: Int = 0,
    val birthDay: Int = 0,
    val bookContributors: List<BookContributorResponseBriefDto> = ArrayList(),
    val movieContributors: List<MovieContributorResponseBriefDto> = ArrayList()
)
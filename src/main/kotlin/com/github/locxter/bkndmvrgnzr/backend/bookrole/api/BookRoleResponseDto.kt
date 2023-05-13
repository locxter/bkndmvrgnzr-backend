package com.github.locxter.bkndmvrgnzr.backend.bookrole.api

import com.github.locxter.bkndmvrgnzr.backend.bookcontributor.api.BookContributorResponseBriefDto

data class BookRoleResponseDto(
    val id: String = "",
    val name: String = "",
    val bookContributors: List<BookContributorResponseBriefDto> = listOf()
)

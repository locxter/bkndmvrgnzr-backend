package com.github.locxter.bkndmvrgnzr.backend.bookcontributor.api

import com.github.locxter.bkndmvrgnzr.backend.book.api.BookResponseBriefDto
import com.github.locxter.bkndmvrgnzr.backend.bookrole.api.BookRoleResponseBriefDto
import com.github.locxter.bkndmvrgnzr.backend.contributor.api.ContributorResponseBriefDto

data class BookContributorResponseDto(
    val id: String = "",
    val contributor: ContributorResponseBriefDto = ContributorResponseBriefDto(),
    val bookRole: BookRoleResponseBriefDto = BookRoleResponseBriefDto(),
    val books: List<BookResponseBriefDto> = mutableListOf(),
)

package com.github.locxter.bkndmvrgnzr.backend.book.api

import com.github.locxter.bkndmvrgnzr.backend.bookcontributor.api.BookContributorResponseBriefDto
import com.github.locxter.bkndmvrgnzr.backend.genre.api.GenreResponseBriefDto
import com.github.locxter.bkndmvrgnzr.backend.publishinghouse.api.PublishingHouseResponseBriefDto

data class BookResponseDto(
    val isbn: String = "",
    val title: String = "",
    val subtitle: String = "",
    val description: String = "",
    val year: Int = 0,
    val pages: Int = 0,
    val publishingHouse: PublishingHouseResponseBriefDto = PublishingHouseResponseBriefDto(),
    val genres: List<GenreResponseBriefDto> = ArrayList(),
    val bookContributors: List<BookContributorResponseBriefDto> = ArrayList(),
)
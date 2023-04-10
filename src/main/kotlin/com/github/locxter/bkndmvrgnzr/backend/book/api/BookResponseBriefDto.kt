package com.github.locxter.bkndmvrgnzr.backend.book.api

import com.github.locxter.bkndmvrgnzr.backend.publishinghouse.api.PublishingHouseResponseBriefDto

data class BookResponseBriefDto(
    val isbn: String = "",
    val title: String = "",
    val subtitle: String = "",
    val description: String = "",
    val year: Int = 0,
    val pages: Int = 0,
    val publishingHouse: PublishingHouseResponseBriefDto = PublishingHouseResponseBriefDto(),
)

package com.github.locxter.bkndmvrgnzr.backend.publishinghouse.api

import com.github.locxter.bkndmvrgnzr.backend.book.api.BookResponseBriefDto

data class PublishingHouseResponseDto(
    val id: String = "",
    val name: String = "",
    val country: String = "",
    val city: String = "",
    val books: List<BookResponseBriefDto> = ArrayList()
)

package com.github.locxter.bkndmvrgnzr.backend.book.api

data class BookUpdateDto(
    val title: String = "",
    val subtitle: String = "",
    val description: String = "",
    val year: Int = 0,
    val pages: Int = 0,
    val publishingHouseId: String = "",
    val genreIds: List<String> = ArrayList(),
    val bookContributorIds: List<String> = ArrayList(),
)

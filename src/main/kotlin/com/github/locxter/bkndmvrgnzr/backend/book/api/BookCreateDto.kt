package com.github.locxter.bkndmvrgnzr.backend.book.api

data class BookCreateDto(
    val isbn: String = "",
    val title: String = "",
    val subtitle: String = "",
    val description: String = "",
    val year: Int = 0,
    val pages: Int = 0,
    val publishingHouseId: String = "",
    val genreIds: List<String> = mutableListOf(),
    val bookContributorIds: List<String> = mutableListOf(),
)

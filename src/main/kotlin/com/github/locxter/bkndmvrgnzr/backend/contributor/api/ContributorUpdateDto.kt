package com.github.locxter.bkndmvrgnzr.backend.contributor.api

data class ContributorUpdateDto(
    val firstName: String = "",
    val lastName: String = "",
    val birthYear: Int = 0,
    val birthMonth: Int = 0,
    val birthDay: Int = 0
)

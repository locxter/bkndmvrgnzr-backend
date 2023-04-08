package com.github.locxter.bkndmvrgnzr.backend.contributor.api

data class ContributorCreateDto(
    val firstName: String = "",
    val lastName: String = "",
    val birthYear: Int = 0,
    val birthMonth: Int = 0,
    val birthDay: Int = 0
)

package com.github.locxter.bkndmvrgnzr.backend.user.api

data class UserCreateDto(
    val username: String = "",
    val password: String = "",
    val firstName: String = "",
    val lastName: String = ""
)

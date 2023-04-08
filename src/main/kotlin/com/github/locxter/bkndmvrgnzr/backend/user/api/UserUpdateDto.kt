package com.github.locxter.bkndmvrgnzr.backend.user.api

data class UserUpdateDto(
    val username: String = "",
    val password: String = "",
    val firstName: String = "",
    val lastName: String = ""
)
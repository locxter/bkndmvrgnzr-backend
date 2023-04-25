package com.github.locxter.bkndmvrgnzr.backend.user.api

data class UserDeleteDto(
    val username: String = "",
    val password: String = "",
    val confirmPassword: String = ""
)

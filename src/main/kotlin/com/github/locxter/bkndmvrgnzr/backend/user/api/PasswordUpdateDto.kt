package com.github.locxter.bkndmvrgnzr.backend.user.api

data class PasswordUpdateDto(
    val password: String = "",
    val newPassword: String = "",
    val confirmNewPassword: String = ""
)

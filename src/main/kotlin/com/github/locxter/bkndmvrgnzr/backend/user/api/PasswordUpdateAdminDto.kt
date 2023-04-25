package com.github.locxter.bkndmvrgnzr.backend.user.api

data class PasswordUpdateAdminDto(
    val newPassword: String = "",
    val confirmNewPassword: String = ""
)

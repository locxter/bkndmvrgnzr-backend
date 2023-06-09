package com.github.locxter.bkndmvrgnzr.backend.role.api

import com.github.locxter.bkndmvrgnzr.backend.role.db.ERole

data class RoleResponseDto(
    val id: String = "",
    val type: String = ERole.ROLE_USER.name
)

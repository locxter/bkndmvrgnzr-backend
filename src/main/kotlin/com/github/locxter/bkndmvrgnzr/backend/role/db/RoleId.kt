package com.github.locxter.bkndmvrgnzr.backend.role.db

import com.github.locxter.bkndmvrgnzr.backend.lib.IdGenerator
import jakarta.persistence.Embeddable
import java.io.Serializable

@Embeddable
data class RoleId(
    val value: String = IdGenerator.next()
) : Serializable

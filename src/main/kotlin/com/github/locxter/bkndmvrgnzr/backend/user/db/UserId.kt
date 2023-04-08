package com.github.locxter.bkndmvrgnzr.backend.user.db

import com.github.locxter.bkndmvrgnzr.backend.lib.IdGenerator
import jakarta.persistence.Embeddable
import java.io.Serializable

@Embeddable
data class UserId(
    val value: String = IdGenerator.next()
) : Serializable

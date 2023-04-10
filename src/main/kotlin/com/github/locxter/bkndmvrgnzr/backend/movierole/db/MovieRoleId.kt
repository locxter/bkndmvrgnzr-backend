package com.github.locxter.bkndmvrgnzr.backend.movierole.db

import com.github.locxter.bkndmvrgnzr.backend.lib.IdGenerator
import jakarta.persistence.Embeddable
import java.io.Serializable

@Embeddable
data class MovieRoleId(
    val value: String = IdGenerator.next()
) : Serializable

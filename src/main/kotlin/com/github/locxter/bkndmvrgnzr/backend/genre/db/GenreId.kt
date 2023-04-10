package com.github.locxter.bkndmvrgnzr.backend.genre.db

import com.github.locxter.bkndmvrgnzr.backend.lib.IdGenerator
import jakarta.persistence.Embeddable
import java.io.Serializable

@Embeddable
data class GenreId(
    val value: String = IdGenerator.next()
) : Serializable

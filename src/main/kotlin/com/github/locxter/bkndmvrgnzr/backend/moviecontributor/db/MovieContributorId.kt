package com.github.locxter.bkndmvrgnzr.backend.moviecontributor.db

import com.github.locxter.bkndmvrgnzr.backend.lib.IdGenerator
import jakarta.persistence.Embeddable
import java.io.Serializable

@Embeddable
data class MovieContributorId(
    val value: String = IdGenerator.next()
) : Serializable
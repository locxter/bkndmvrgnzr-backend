package com.github.locxter.bkndmvrgnzr.backend.bookcontributor.db

import com.github.locxter.bkndmvrgnzr.backend.lib.IdGenerator
import jakarta.persistence.Embeddable
import java.io.Serializable

@Embeddable
data class BookContributorId(
    val value: String = IdGenerator.next()
) : Serializable

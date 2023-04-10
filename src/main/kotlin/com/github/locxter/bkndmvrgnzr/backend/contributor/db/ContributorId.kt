package com.github.locxter.bkndmvrgnzr.backend.contributor.db

import com.github.locxter.bkndmvrgnzr.backend.lib.IdGenerator
import jakarta.persistence.Embeddable
import java.io.Serializable

@Embeddable
data class ContributorId(
    val value: String = IdGenerator.next()
) : Serializable

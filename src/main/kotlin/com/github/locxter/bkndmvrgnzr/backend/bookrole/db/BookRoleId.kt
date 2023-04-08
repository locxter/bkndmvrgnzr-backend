package com.github.locxter.bkndmvrgnzr.backend.bookrole.db

import com.github.locxter.bkndmvrgnzr.backend.lib.IdGenerator
import jakarta.persistence.Embeddable
import java.io.Serializable

@Embeddable
data class BookRoleId(
    val value: String = IdGenerator.next()
) : Serializable
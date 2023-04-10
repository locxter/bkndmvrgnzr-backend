package com.github.locxter.bkndmvrgnzr.backend.user.db

import jakarta.persistence.Embeddable
import jakarta.persistence.Transient
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.io.Serializable

@Embeddable
class Password(plainValue: String = "") : Serializable {
    @Transient
    private val passwordEncoder = BCryptPasswordEncoder()
    val value: String

    init {
        value = passwordEncoder.encode(plainValue)
    }

    fun matches(plainValue: String = ""): Boolean {
        return passwordEncoder.matches(plainValue, value)
    }
}

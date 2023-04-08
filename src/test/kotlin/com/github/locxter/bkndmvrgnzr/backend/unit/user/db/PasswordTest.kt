package com.github.locxter.bkndmvrgnzr.backend.unit.user.db

import com.github.locxter.bkndmvrgnzr.backend.user.db.Password
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PasswordTest {

    @Test
    fun `Check password hashing on creation`() {
        // setup
        val plaintext = "12345"
        // test
        val password = Password(plaintext)
        // assertion
        assertThat(password.value)
            .`as`("password is in plaintext")
            .isNotEqualTo(plaintext)
    }

    @Test
    fun `Check matches function`() {
        // setup
        val plaintext = "12345"
        val password = Password(plaintext)
        // test / assertion
        assertThat(password.matches(plaintext)).isTrue
    }

}
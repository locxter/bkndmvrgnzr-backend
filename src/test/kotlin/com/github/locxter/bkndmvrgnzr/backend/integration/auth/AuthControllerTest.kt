package com.github.locxter.bkndmvrgnzr.backend.integration.auth

import com.github.locxter.bkndmvrgnzr.backend.Main
import com.github.locxter.bkndmvrgnzr.backend.auth.api.AuthLoginDto
import com.github.locxter.bkndmvrgnzr.backend.lib.DataLoader
import com.github.locxter.bkndmvrgnzr.backend.user.api.UserCreateDto
import com.github.locxter.bkndmvrgnzr.backend.user.api.UserResponseDto
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.testng.Assert.assertEquals
import org.testng.Assert.assertNotNull


@SpringBootTest(
    classes = [Main::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class AuthControllerTest(
    @Autowired private val restTemplate: TestRestTemplate,
    @Autowired private val dataLoader: DataLoader
) {
    private val username = "test"
    private val password = "password"

    @Test
    @Order(1)
    fun `Create user`() {
        // Setup
        dataLoader.loadData()
        val userCreateDto = UserCreateDto(
            username,
            password
        )
        // Test
        val result = restTemplate.exchange(
            "/api/user",
            HttpMethod.POST,
            HttpEntity(userCreateDto),
            UserResponseDto::class.java
        )
        // Assertion
        assertNotNull(result)
        assertEquals(result!!.statusCode, HttpStatus.OK)
        assertEquals(result.body!!.username, username)
    }

    @Test
    @Order(2)
    fun `Login`() {
        // Setup
        val authLoginDto = AuthLoginDto(
            username,
            password
        )
        // Test
        val result = restTemplate.exchange(
            "/api/auth",
            HttpMethod.POST,
            HttpEntity(authLoginDto),
            String::class.java
        )
        // Assertion
        assertNotNull(result)
        assertEquals(result!!.statusCode, HttpStatus.OK)
        assert(result.headers.contains("set-cookie"))
        println(result.headers["set-cookie"]!![0].toString())
        assert(
            Regex("bkndmvrgnzr=(\\S+\\.){2}\\S+; Path=\\/; Max-Age=28800; Expires=.*; HttpOnly").containsMatchIn(
                result.headers["set-cookie"]!![0].toString()
            )
        )
    }

    @Test
    @Order(3)
    fun `Logout`() {
        // Test
        val result = restTemplate.exchange(
            "/api/auth",
            HttpMethod.DELETE,
            HttpEntity.EMPTY,
            String::class.java
        )
        // Assertion
        assertNotNull(result)
        assertEquals(result!!.statusCode, HttpStatus.OK)
        assert(result.headers.contains("set-cookie"))
        assert(result.headers["set-cookie"]!![0].toString() == "bkndmvrgnzr=; Path=/")
    }
}

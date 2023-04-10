package com.github.locxter.bkndmvrgnzr.backend.integration.genre

import com.github.locxter.bkndmvrgnzr.backend.Main
import com.github.locxter.bkndmvrgnzr.backend.lib.DataLoader
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
import org.testng.Assert

@SpringBootTest(
    classes = [Main::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class GenreControllerTest(
    @Autowired private val restTemplate: TestRestTemplate,
    @Autowired private val dataLoader: DataLoader
) {
    @Test
    @Order(1)
    fun `Get all genres`() {
        // Setup
        dataLoader.loadData()
        // Figure out how to get the JWT
        // Test
        val result = restTemplate.exchange(
            "/api/user",
            HttpMethod.GET,
            HttpEntity.EMPTY,
            Any::class.java
        )
        // Assertion
        Assert.assertNotNull(result)
        Assert.assertEquals(result!!.statusCode, HttpStatus.OK)
    }
}

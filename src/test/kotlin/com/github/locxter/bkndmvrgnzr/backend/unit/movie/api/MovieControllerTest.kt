package com.github.locxter.bkndmvrgnzr.backend.unit.movie.api

import com.github.locxter.bkndmvrgnzr.backend.movie.api.MovieController
import com.github.locxter.bkndmvrgnzr.backend.movie.db.Movie
import com.github.locxter.bkndmvrgnzr.backend.movie.db.MovieRepository
import com.github.locxter.bkndmvrgnzr.backend.user.db.UserId
import com.github.locxter.bkndmvrgnzr.backend.user.db.UserRepository
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.web.server.ResponseStatusException

class MovieControllerTest {
    @Test
    fun `Get all movies of user`() {
        // setup
        val username = "Luca"
        val userId = UserId("12345")

        val mocks = initMocks()
            .expectFindByUsername(username, userId)
            .expectFindByUserId(userId, emptyList())

        val controller = MovieController(mocks.movieRepo, mockk(), mocks.userRepo, mockk(), mockk())
        val authMock = mockk<Authentication> {
            every { name } returns username
        }

        // test
        val movieDtoList = controller.getAllMoviesOfUser(authMock)

        // assertion
        assertThat(movieDtoList).isEmpty()
    }

    @Test
    fun `Error handling`() {
        // setup
        val username = "test"

        val mocks = initMocks()
            .expectFindByUsernameReturnNull(username)

        val controller = MovieController(mocks.movieRepo, mockk(), mocks.userRepo, mockk(), mockk())
        val authMock = mockk<Authentication> {
            every { name } returns username
        }

        // test
        assertThrows<ResponseStatusException> {
            controller.getAllMoviesOfUser(authMock)
        }.let { exception ->
            assertThat(exception.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
            assertThat(exception.reason).isNotEmpty
        }

    }

    private fun initMocks() = MockBuilder()

    private class MockBuilder {
        fun expectFindByUsername(forUsername: String, withUserId: UserId) = apply {
            every { userRepo.findByUsername(forUsername) } returns mockk(relaxed = true) {
                every { id } returns withUserId
            }
        }

        fun expectFindByUserId(userId: UserId, result: List<Movie>) = apply {
            every { movieRepo.findByUsersId(userId) } returns result
        }

        fun expectFindByUsernameReturnNull(forUsername: String) = apply {
            every { userRepo.findByUsername(forUsername) } returns null
        }

        val userRepo = mockk<UserRepository>()
        val movieRepo = mockk<MovieRepository>()
    }
}

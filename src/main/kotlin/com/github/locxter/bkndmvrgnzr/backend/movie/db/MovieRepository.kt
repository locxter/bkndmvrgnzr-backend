package com.github.locxter.bkndmvrgnzr.backend.movie.db

import com.github.locxter.bkndmvrgnzr.backend.genre.db.GenreId
import com.github.locxter.bkndmvrgnzr.backend.moviecontributor.db.MovieContributorId
import com.github.locxter.bkndmvrgnzr.backend.user.db.UserId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MovieRepository : JpaRepository<Movie, Isan> {
    fun findByDescription(description: String): List<Movie>
    fun findByYear(year: Int): List<Movie>
    fun findByPlayTime(playTime: Int): List<Movie>
    fun findByAgeRestriction(ageRestriction: Int): List<Movie>
    fun findByUsersId(userId: UserId): List<Movie>
    fun findByGenresId(genreId: GenreId): List<Movie>
    fun findByMovieContributorsId(movieContributorId: MovieContributorId): List<Movie>
}
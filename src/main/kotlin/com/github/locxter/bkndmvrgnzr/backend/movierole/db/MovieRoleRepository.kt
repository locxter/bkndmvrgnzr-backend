package com.github.locxter.bkndmvrgnzr.backend.movierole.db

import com.github.locxter.bkndmvrgnzr.backend.moviecontributor.db.MovieContributorId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MovieRoleRepository : JpaRepository<MovieRole, MovieRoleId> {
    fun findByName(name: String): List<MovieRole>
    fun findByMovieContributorsId(movieContributorId: MovieContributorId): MovieRole?
}
package com.github.locxter.bkndmvrgnzr.backend.moviecontributor.db

import com.github.locxter.bkndmvrgnzr.backend.contributor.db.ContributorId
import com.github.locxter.bkndmvrgnzr.backend.movie.db.Isan
import com.github.locxter.bkndmvrgnzr.backend.movierole.db.MovieRoleId
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MovieContributorRepository : JpaRepository<MovieContributor, MovieContributorId> {
    fun findByContributorId(contributorId: ContributorId): List<MovieContributor>
    fun findByContributorId(contributorId: ContributorId, sort: Sort): List<MovieContributor>
    fun findByMovieRoleId(movieRoleId: MovieRoleId): List<MovieContributor>
    fun findByMovieRoleId(movieRoleId: MovieRoleId, sort: Sort): List<MovieContributor>
    fun findByMoviesIsan(isan: Isan): List<MovieContributor>
    fun findByMoviesIsan(isan: Isan, sort: Sort): List<MovieContributor>
}

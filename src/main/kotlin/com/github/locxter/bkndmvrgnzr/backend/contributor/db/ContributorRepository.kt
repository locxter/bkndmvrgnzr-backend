package com.github.locxter.bkndmvrgnzr.backend.contributor.db

import com.github.locxter.bkndmvrgnzr.backend.bookcontributor.db.BookContributorId
import com.github.locxter.bkndmvrgnzr.backend.moviecontributor.db.MovieContributorId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ContributorRepository : JpaRepository<Contributor, ContributorId> {
    fun findByFirstName(firstName: String): List<Contributor>
    fun findByLastName(lastName: String): List<Contributor>
    fun findByBirthYear(birthYear: Int): List<Contributor>
    fun findByBirthMonth(birthMonth: Int): List<Contributor>
    fun findByBirthDay(birthDay: Int): List<Contributor>
    fun findByBookContributorsId(bookContributorId: BookContributorId): Contributor?
    fun findByMovieContributorsId(movieContributorId: MovieContributorId): Contributor?
}
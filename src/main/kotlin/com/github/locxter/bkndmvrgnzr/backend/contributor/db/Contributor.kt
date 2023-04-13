package com.github.locxter.bkndmvrgnzr.backend.contributor.db

import com.github.locxter.bkndmvrgnzr.backend.bookcontributor.db.BookContributor
import com.github.locxter.bkndmvrgnzr.backend.contributor.api.ContributorResponseBriefDto
import com.github.locxter.bkndmvrgnzr.backend.contributor.api.ContributorResponseDto
import com.github.locxter.bkndmvrgnzr.backend.moviecontributor.db.MovieContributor
import jakarta.persistence.*

@Entity
data class Contributor(
    @Id
    @AttributeOverride(name = "value", column = Column(name = "id"))
    val id: ContributorId = ContributorId(),
    val firstName: String = "",
    val lastName: String = "",
    val birthYear: Int = 0,
    val birthMonth: Int = 0,
    val birthDay: Int = 0,
    @OneToMany(mappedBy = "contributor")
    val bookContributors: List<BookContributor> = ArrayList(),
    @OneToMany(mappedBy = "contributor")
    val movieContributors: List<MovieContributor> = ArrayList()
) {
    fun toDto(): ContributorResponseDto = ContributorResponseDto(
        id.value,
        firstName,
        lastName,
        birthYear,
        birthMonth,
        birthDay,
        bookContributors.sortedBy { it.contributor.id.value }.map { it.toBriefDto() },
        movieContributors.sortedBy { it.contributor.id.value }.map { it.toBriefDto() }
    )

    fun toBriefDto(): ContributorResponseBriefDto = ContributorResponseBriefDto(
        id.value,
        firstName,
        lastName,
        birthYear,
        birthMonth,
        birthDay,
    )
}

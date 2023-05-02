package com.github.locxter.bkndmvrgnzr.backend.contributor.db

import com.github.locxter.bkndmvrgnzr.backend.bookcontributor.db.BookContributor
import com.github.locxter.bkndmvrgnzr.backend.contributor.api.ContributorResponseBriefDto
import com.github.locxter.bkndmvrgnzr.backend.contributor.api.ContributorResponseDto
import com.github.locxter.bkndmvrgnzr.backend.moviecontributor.db.MovieContributor
import jakarta.persistence.*
import org.springframework.data.domain.Sort

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
    @OneToMany(mappedBy = "contributor", cascade = [CascadeType.REMOVE])
    val bookContributors: List<BookContributor> = ArrayList(),
    @OneToMany(mappedBy = "contributor", cascade = [CascadeType.REMOVE])
    val movieContributors: List<MovieContributor> = ArrayList()
) {
    fun toDto(): ContributorResponseDto = ContributorResponseDto(
        id.value,
        firstName,
        lastName,
        birthYear,
        birthMonth,
        birthDay,
        bookContributors.sortedWith(BookContributor)
            .map { it.toBriefDto() },
        movieContributors.sortedWith(MovieContributor)
            .map { it.toBriefDto() }
    )

    fun toBriefDto(): ContributorResponseBriefDto = ContributorResponseBriefDto(
        id.value,
        firstName,
        lastName,
        birthYear,
        birthMonth,
        birthDay,
    )

    companion object : Comparator<Contributor> {
        override fun compare(o1: Contributor, o2: Contributor): Int {
            val s1 = o1.lastName + o1.firstName
            val s2 = o2.lastName + o2.firstName
            return s1.compareTo(s2)
        }

        fun getSort() : Sort {
            return Sort.by(
                Sort.Direction.ASC,
                "lastName",
                "firstName",
            )
        }
    }
}

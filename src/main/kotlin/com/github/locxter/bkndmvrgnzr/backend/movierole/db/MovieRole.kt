package com.github.locxter.bkndmvrgnzr.backend.movierole.db

import com.github.locxter.bkndmvrgnzr.backend.moviecontributor.db.MovieContributor
import com.github.locxter.bkndmvrgnzr.backend.movierole.api.MovieRoleResponseBriefDto
import com.github.locxter.bkndmvrgnzr.backend.movierole.api.MovieRoleResponseDto
import jakarta.persistence.*
import org.springframework.data.domain.Sort

@Entity
data class MovieRole(
    @Id
    @AttributeOverride(name = "value", column = Column(name = "id"))
    val id: MovieRoleId = MovieRoleId(),
    val name: String = "",
    @OneToMany(mappedBy = "movieRole")
    val movieContributors: List<MovieContributor> = ArrayList()
) {
    fun toDto(): MovieRoleResponseDto = MovieRoleResponseDto(
        id.value,
        name,
        movieContributors.sortedWith(MovieContributor).map { it.toBriefDto() }
    )

    fun toBriefDto(): MovieRoleResponseBriefDto = MovieRoleResponseBriefDto(
        id.value,
        name
    )

    companion object : Comparator<MovieRole> {
        override fun compare(o1: MovieRole, o2: MovieRole): Int {
            val s1 = o1.name
            val s2 = o1.name
            return s1.compareTo(s2)
        }

        fun getSort(): Sort {
            return Sort.by(
                Sort.Direction.ASC,
                "name",
            )
        }
    }
}

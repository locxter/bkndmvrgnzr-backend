package com.github.locxter.bkndmvrgnzr.backend.movierole.db

import com.github.locxter.bkndmvrgnzr.backend.moviecontributor.db.MovieContributor
import com.github.locxter.bkndmvrgnzr.backend.movierole.api.MovieRoleResponseBriefDto
import com.github.locxter.bkndmvrgnzr.backend.movierole.api.MovieRoleResponseDto
import jakarta.persistence.*

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
        movieContributors.sortedBy { it.contributor.id.value }.map { it.toBriefDto() }
    )

    fun toBriefDto(): MovieRoleResponseBriefDto = MovieRoleResponseBriefDto(
        id.value,
        name
    )
}

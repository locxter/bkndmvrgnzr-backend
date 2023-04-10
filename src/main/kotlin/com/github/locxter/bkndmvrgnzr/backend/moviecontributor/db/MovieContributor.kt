package com.github.locxter.bkndmvrgnzr.backend.moviecontributor.db

import com.github.locxter.bkndmvrgnzr.backend.contributor.db.Contributor
import com.github.locxter.bkndmvrgnzr.backend.movie.db.Movie
import com.github.locxter.bkndmvrgnzr.backend.moviecontributor.api.MovieContributorResponseBriefDto
import com.github.locxter.bkndmvrgnzr.backend.moviecontributor.api.MovieContributorResponseDto
import com.github.locxter.bkndmvrgnzr.backend.movierole.db.MovieRole
import jakarta.persistence.*

@Entity
data class MovieContributor(
    @Id
    @AttributeOverride(name = "value", column = Column(name = "id"))
    val id: MovieContributorId = MovieContributorId(),
    @ManyToOne
    @JoinColumn(name = "contributor_id")
    val contributor: Contributor,
    @ManyToOne
    @JoinColumn(name = "movie_role_id")
    val movieRole: MovieRole,
    @ManyToMany(mappedBy = "movieContributors")
    val movies: List<Movie> = ArrayList(),
) {
    fun toDto(): MovieContributorResponseDto = MovieContributorResponseDto(
        id.value,
        contributor.toBriefDto(),
        movieRole.toBriefDto(),
        movies.map { it.toBriefDto() }
    )

    fun toBriefDto(): MovieContributorResponseBriefDto = MovieContributorResponseBriefDto(
        id.value,
        contributor.toBriefDto(),
        movieRole.toBriefDto()
    )
}

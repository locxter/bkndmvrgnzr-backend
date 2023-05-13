package com.github.locxter.bkndmvrgnzr.backend.moviecontributor.db

import com.github.locxter.bkndmvrgnzr.backend.contributor.db.Contributor
import com.github.locxter.bkndmvrgnzr.backend.movie.db.Movie
import com.github.locxter.bkndmvrgnzr.backend.moviecontributor.api.MovieContributorResponseBriefDto
import com.github.locxter.bkndmvrgnzr.backend.moviecontributor.api.MovieContributorResponseDto
import com.github.locxter.bkndmvrgnzr.backend.movierole.db.MovieRole
import jakarta.persistence.*
import org.springframework.data.domain.Sort

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
    @ManyToMany
    @JoinTable(
        name = "movie_x_movie_contributor",
        joinColumns = [JoinColumn(name = "movie_contributor_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "isan", referencedColumnName = "isan")]
    )
    val movies: List<Movie> = listOf(),
) {
    fun toDto(): MovieContributorResponseDto = MovieContributorResponseDto(
        id.value,
        contributor.toBriefDto(),
        movieRole.toBriefDto(),
        movies.sortedWith(Movie).map { it.toBriefDto() }
    )

    fun toBriefDto(): MovieContributorResponseBriefDto = MovieContributorResponseBriefDto(
        id.value,
        contributor.toBriefDto(),
        movieRole.toBriefDto()
    )

    companion object : Comparator<MovieContributor> {
        override fun compare(o1: MovieContributor, o2: MovieContributor): Int {
            val s1 = o1.contributor.lastName + o1.contributor.firstName + o1.movieRole.name
            val s2 = o2.contributor.lastName + o2.contributor.firstName + o2.movieRole.name
            return s1.compareTo(s2)
        }

        fun getSort(): Sort {
            return Sort.by(
                Sort.Direction.ASC,
                "contributor.lastName",
                "contributor.firstName",
                "movieRole.name"
            )
        }
    }
}

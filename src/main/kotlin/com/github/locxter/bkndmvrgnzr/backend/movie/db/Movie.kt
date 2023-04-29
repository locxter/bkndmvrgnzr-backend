package com.github.locxter.bkndmvrgnzr.backend.movie.db

import com.github.locxter.bkndmvrgnzr.backend.genre.db.Genre
import com.github.locxter.bkndmvrgnzr.backend.movie.api.MovieResponseBriefDto
import com.github.locxter.bkndmvrgnzr.backend.movie.api.MovieResponseDto
import com.github.locxter.bkndmvrgnzr.backend.moviecontributor.db.MovieContributor
import com.github.locxter.bkndmvrgnzr.backend.user.db.User
import jakarta.persistence.*

@Entity
data class Movie(
    @Id
    @AttributeOverride(name = "value", column = Column(name = "isan"))
    val isan: Isan = Isan("0000-0000-0000-0000-A-0000-0000-A"),
    val title: String = "",
    val description: String = "",
    val year: Int = 0,
    val playTime: Int = 0,
    val ageRestriction: Int = 0,
    @ManyToMany
    @JoinTable(
        name = "movie_genre",
        joinColumns = [JoinColumn(name = "isan", referencedColumnName = "isan")],
        inverseJoinColumns = [JoinColumn(name = "genre_id", referencedColumnName = "id")]
    )
    val genres: List<Genre> = ArrayList(),
    @ManyToMany
    @JoinTable(
        name = "movie_movie_contributor",
        joinColumns = [JoinColumn(name = "isan", referencedColumnName = "isan")],
        inverseJoinColumns = [JoinColumn(name = "movie_contributor_id", referencedColumnName = "id")]
    )
    val movieContributors: List<MovieContributor> = ArrayList(),
    @ManyToMany
    @JoinTable(
        name = "user_movie",
        joinColumns = [JoinColumn(name = "isan", referencedColumnName = "isan")],
        inverseJoinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")]
    )
    val users: List<User> = ArrayList(),
) {
    fun toDto(): MovieResponseDto = MovieResponseDto(
        isan.value,
        title,
        description,
        year,
        playTime,
        ageRestriction,
        genres.sortedBy { it.name }.map { it.toBriefDto() },
        movieContributors.sortedBy { it.contributor.lastName + it.contributor.firstName + it.movieRole.name }.map { it.toBriefDto() }
    )

    fun toBriefDto(): MovieResponseBriefDto = MovieResponseBriefDto(
        isan.value,
        title,
        description,
        year,
        playTime,
        ageRestriction,
    )
}

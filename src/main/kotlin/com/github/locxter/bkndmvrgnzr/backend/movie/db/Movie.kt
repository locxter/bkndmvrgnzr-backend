package com.github.locxter.bkndmvrgnzr.backend.movie.db

import com.github.locxter.bkndmvrgnzr.backend.genre.db.Genre
import com.github.locxter.bkndmvrgnzr.backend.movie.api.MovieResponseBriefDto
import com.github.locxter.bkndmvrgnzr.backend.movie.api.MovieResponseDto
import com.github.locxter.bkndmvrgnzr.backend.moviecontributor.db.MovieContributor
import com.github.locxter.bkndmvrgnzr.backend.user.db.User
import jakarta.persistence.*
import org.springframework.data.domain.Sort

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
        name = "movie_x_genre",
        joinColumns = [JoinColumn(name = "isan", referencedColumnName = "isan")],
        inverseJoinColumns = [JoinColumn(name = "genre_id", referencedColumnName = "id")]
    )
    val genres: List<Genre> = listOf(),
    @ManyToMany
    @JoinTable(
        name = "movie_x_movie_contributor",
        joinColumns = [JoinColumn(name = "isan", referencedColumnName = "isan")],
        inverseJoinColumns = [JoinColumn(name = "movie_contributor_id", referencedColumnName = "id")]
    )
    val movieContributors: List<MovieContributor> = listOf(),
    @ManyToMany
    @JoinTable(
        name = "user_x_movie",
        joinColumns = [JoinColumn(name = "isan", referencedColumnName = "isan")],
        inverseJoinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")]
    )
    val users: List<User> = listOf(),
) {
    fun toDto(): MovieResponseDto = MovieResponseDto(
        isan.value,
        title,
        description,
        year,
        playTime,
        ageRestriction,
        genres.sortedWith(Genre).map { it.toBriefDto() },
        movieContributors.sortedWith(MovieContributor).map { it.toBriefDto() }
    )

    fun toBriefDto(): MovieResponseBriefDto = MovieResponseBriefDto(
        isan.value,
        title,
        description,
        year,
        playTime,
        ageRestriction,
    )

    companion object : Comparator<Movie> {
        override fun compare(o1: Movie, o2: Movie): Int {
            val s1 = o1.title
            val s2 = o1.title
            return s1.compareTo(s2)
        }

        fun getSort(): Sort {
            return Sort.by(
                Sort.Direction.ASC,
                "title",
            )
        }
    }
}

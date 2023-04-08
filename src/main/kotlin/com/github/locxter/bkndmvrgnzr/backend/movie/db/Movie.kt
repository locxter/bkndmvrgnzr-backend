package com.github.locxter.bkndmvrgnzr.backend.movie.db

import com.github.locxter.bkndmvrgnzr.backend.genre.db.Genre
import com.github.locxter.bkndmvrgnzr.backend.movie.api.MovieResponseBriefDto
import com.github.locxter.bkndmvrgnzr.backend.movie.api.MovieResponseDto
import com.github.locxter.bkndmvrgnzr.backend.moviecontributor.db.MovieContributor
import com.github.locxter.bkndmvrgnzr.backend.user.db.User
import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction

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
    @ManyToMany(cascade = [CascadeType.ALL])
    @JoinTable(
        name = "movie_genre",
        joinColumns = [JoinColumn(name = "isan", referencedColumnName = "isan")],
        inverseJoinColumns = [JoinColumn(name = "genre_id", referencedColumnName = "id")]
    )
    val genres: List<Genre> = ArrayList(),
    @ManyToMany(cascade = [CascadeType.ALL])
    @JoinTable(
        name = "movie_movie_contributor",
        joinColumns = [JoinColumn(name = "isan", referencedColumnName = "isan")],
        inverseJoinColumns = [JoinColumn(name = "movie_contributor_id", referencedColumnName = "id")]
    )
    val movieContributors: List<MovieContributor> = ArrayList(),
    @ManyToMany(mappedBy = "movies")
    @OnDelete(action = OnDeleteAction.CASCADE)
    val users: List<User> = ArrayList(),
) {
    /*
    constructor(movieCreateDto: MovieCreateDto) : this(
        Isan(movieCreateDto.isan),
        movieCreateDto.title,
        movieCreateDto.description,
        movieCreateDto.year,
        movieCreateDto.playTime,
        movieCreateDto.ageRestriction,
        genres = movieCreateDto.genres.map { Genre(it) }
    )

    constructor(movieResponseDto: MovieResponseDto) : this(
        Isan(movieResponseDto.isan),
        movieResponseDto.title,
        movieResponseDto.description,
        movieResponseDto.year,
        movieResponseDto.playTime,
        movieResponseDto.ageRestriction,
        genres = movieResponseDto.genres.map { Genre(it) },
        movieContributors = movieResponseDto.movieContributors.map { MovieContributor(it) }
    )

    fun updateWithDto(movieUpdateDto: MovieUpdateDto): Movie = Movie(
        isan,
        movieUpdateDto.title,
        movieUpdateDto.description,
        movieUpdateDto.year,
        movieUpdateDto.playTime,
        movieUpdateDto.ageRestriction,
        users,
        movieUpdateDto.genres.map { Genre(it) },
        movieUpdateDto.movieContributors.map { MovieContributor(it) }
    )
     */

    fun toDto(): MovieResponseDto = MovieResponseDto(
        isan.value,
        title,
        description,
        year,
        playTime,
        ageRestriction,
        genres.map { it.toBriefDto() },
        movieContributors.map { it.toBriefDto() }
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
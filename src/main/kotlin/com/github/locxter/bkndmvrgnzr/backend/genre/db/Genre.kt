package com.github.locxter.bkndmvrgnzr.backend.genre.db

import com.github.locxter.bkndmvrgnzr.backend.book.db.Book
import com.github.locxter.bkndmvrgnzr.backend.genre.api.GenreResponseBriefDto
import com.github.locxter.bkndmvrgnzr.backend.genre.api.GenreResponseDto
import com.github.locxter.bkndmvrgnzr.backend.movie.db.Movie
import jakarta.persistence.*

@Entity
data class Genre(
    @Id
    @AttributeOverride(name = "value", column = Column(name = "id"))
    val id: GenreId = GenreId(),
    val name: String = "",
    @ManyToMany
    @JoinTable(
        name = "book_genre",
        joinColumns = [JoinColumn(name = "genre_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "isbn", referencedColumnName = "isbn")]
    )
    val books: List<Book> = ArrayList(),
    @ManyToMany
    @JoinTable(
        name = "movie_genre",
        joinColumns = [JoinColumn(name = "genre_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "isan", referencedColumnName = "isan")]
    )
    val movies: List<Movie> = ArrayList()
) {
    fun toDto(): GenreResponseDto = GenreResponseDto(
        id.value,
        name,
        books.sortedBy { it.isbn.value }.map { it.toBriefDto() },
        movies.sortedBy { it.isan.value }.map { it.toBriefDto() }
    )

    fun toBriefDto(): GenreResponseBriefDto = GenreResponseBriefDto(
        id.value,
        name
    )
}

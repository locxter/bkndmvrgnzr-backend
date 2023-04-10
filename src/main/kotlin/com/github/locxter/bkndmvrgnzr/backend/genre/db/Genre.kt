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
    @ManyToMany(mappedBy = "genres")
    val books: List<Book> = ArrayList(),
    @ManyToMany(mappedBy = "genres")
    val movies: List<Movie> = ArrayList()
) {
    fun toDto(): GenreResponseDto = GenreResponseDto(
        id.value,
        name,
        books.map { it.toBriefDto() },
        movies.map { it.toBriefDto() }
    )

    fun toBriefDto(): GenreResponseBriefDto = GenreResponseBriefDto(
        id.value,
        name
    )
}

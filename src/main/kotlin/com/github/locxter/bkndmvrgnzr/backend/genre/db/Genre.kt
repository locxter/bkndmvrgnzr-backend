package com.github.locxter.bkndmvrgnzr.backend.genre.db

import com.github.locxter.bkndmvrgnzr.backend.book.db.Book
import com.github.locxter.bkndmvrgnzr.backend.bookrole.db.BookRole
import com.github.locxter.bkndmvrgnzr.backend.genre.api.GenreResponseBriefDto
import com.github.locxter.bkndmvrgnzr.backend.genre.api.GenreResponseDto
import com.github.locxter.bkndmvrgnzr.backend.movie.db.Movie
import jakarta.persistence.*
import org.springframework.data.domain.Sort

@Entity
data class Genre(
    @Id
    @AttributeOverride(name = "value", column = Column(name = "id"))
    val id: GenreId = GenreId(),
    val name: String = "",
    @ManyToMany
    @JoinTable(
        name = "book_x_genre",
        joinColumns = [JoinColumn(name = "genre_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "isbn", referencedColumnName = "isbn")]
    )
    val books: List<Book> = ArrayList(),
    @ManyToMany
    @JoinTable(
        name = "movie_x_genre",
        joinColumns = [JoinColumn(name = "genre_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "isan", referencedColumnName = "isan")]
    )
    val movies: List<Movie> = ArrayList()
) {
    fun toDto(): GenreResponseDto = GenreResponseDto(
        id.value,
        name,
        books.sortedWith(Book).map { it.toBriefDto() },
        movies.sortedWith(Movie).map { it.toBriefDto() }
    )

    fun toBriefDto(): GenreResponseBriefDto = GenreResponseBriefDto(
        id.value,
        name
    )

    companion object : Comparator<Genre> {
        override fun compare(o1: Genre, o2: Genre): Int {
            val s1 = o1.name
            val s2 = o1.name
            return s1.compareTo(s2)
        }

        fun getSort() : Sort {
            return Sort.by(
                Sort.Direction.ASC,
                "name",
            )
        }
    }
}

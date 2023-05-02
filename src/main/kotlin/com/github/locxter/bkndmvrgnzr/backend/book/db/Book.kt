package com.github.locxter.bkndmvrgnzr.backend.book.db

import com.github.locxter.bkndmvrgnzr.backend.book.api.BookResponseBriefDto
import com.github.locxter.bkndmvrgnzr.backend.book.api.BookResponseDto
import com.github.locxter.bkndmvrgnzr.backend.bookcontributor.db.BookContributor
import com.github.locxter.bkndmvrgnzr.backend.genre.db.Genre
import com.github.locxter.bkndmvrgnzr.backend.publishinghouse.db.PublishingHouse
import com.github.locxter.bkndmvrgnzr.backend.user.db.User
import jakarta.persistence.*
import org.springframework.data.domain.Sort

@Entity
data class Book(
    @Id
    @AttributeOverride(name = "value", column = Column(name = "isbn"))
    val isbn: Isbn = Isbn("000-0-000-00000-0"),
    val title: String = "",
    val subtitle: String = "",
    val description: String = "",
    val year: Int = 0,
    val pages: Int = 0,
    @ManyToOne
    @JoinColumn(name = "publishing_house_id")
    val publishingHouse: PublishingHouse = PublishingHouse(),
    @ManyToMany
    @JoinTable(
        name = "book_x_genre",
        joinColumns = [JoinColumn(name = "isbn", referencedColumnName = "isbn")],
        inverseJoinColumns = [JoinColumn(name = "genre_id", referencedColumnName = "id")]
    )
    val genres: List<Genre> = ArrayList(),
    @ManyToMany
    @JoinTable(
        name = "book_x_book_contributor",
        joinColumns = [JoinColumn(name = "isbn", referencedColumnName = "isbn")],
        inverseJoinColumns = [JoinColumn(name = "book_contributor_id", referencedColumnName = "id")]
    )
    val bookContributors: List<BookContributor> = ArrayList(),
    @ManyToMany
    @JoinTable(
        name = "user_x_book",
        joinColumns = [JoinColumn(name = "isbn", referencedColumnName = "isbn")],
        inverseJoinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")]
    )
    val users: List<User> = ArrayList(),
) {
    fun toDto(): BookResponseDto = BookResponseDto(
        isbn.value,
        title,
        subtitle,
        description,
        year,
        pages,
        publishingHouse.toBriefDto(),
        genres.sortedWith(Genre).map { it.toBriefDto() },
        bookContributors.sortedWith(BookContributor).map { it.toBriefDto() }
    )

    fun toBriefDto(): BookResponseBriefDto = BookResponseBriefDto(
        isbn.value,
        title,
        subtitle,
        description,
        year,
        pages,
        publishingHouse.toBriefDto()
    )

    companion object : Comparator<Book> {
        override fun compare(o1: Book, o2: Book): Int {
            val s1 = o1.title + o1.subtitle
            val s2 = o1.title + o1.subtitle
            return s1.compareTo(s2)
        }

        fun getSort() : Sort {
            return Sort.by(
                Sort.Direction.ASC,
                "title",
                "subtitle",
            )
        }
    }
}

package com.github.locxter.bkndmvrgnzr.backend.book.db

import com.github.locxter.bkndmvrgnzr.backend.book.api.BookResponseBriefDto
import com.github.locxter.bkndmvrgnzr.backend.book.api.BookResponseDto
import com.github.locxter.bkndmvrgnzr.backend.bookcontributor.db.BookContributor
import com.github.locxter.bkndmvrgnzr.backend.genre.db.Genre
import com.github.locxter.bkndmvrgnzr.backend.publishinghouse.db.PublishingHouse
import com.github.locxter.bkndmvrgnzr.backend.user.db.User
import jakarta.persistence.*

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
        name = "book_genre",
        joinColumns = [JoinColumn(name = "isbn", referencedColumnName = "isbn")],
        inverseJoinColumns = [JoinColumn(name = "genre_id", referencedColumnName = "id")]
    )
    val genres: List<Genre> = ArrayList(),
    @ManyToMany
    @JoinTable(
        name = "book_book_contributor",
        joinColumns = [JoinColumn(name = "isbn", referencedColumnName = "isbn")],
        inverseJoinColumns = [JoinColumn(name = "book_contributor_id", referencedColumnName = "id")]
    )
    val bookContributors: List<BookContributor> = ArrayList(),
    @ManyToMany
    @JoinTable(
        name = "user_book",
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
        genres.sortedBy { it.name }.map { it.toBriefDto() },
        bookContributors.sortedBy { it.contributor.id.value }.map { it.toBriefDto() }
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
}

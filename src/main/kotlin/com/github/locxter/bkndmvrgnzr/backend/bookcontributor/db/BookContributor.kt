package com.github.locxter.bkndmvrgnzr.backend.bookcontributor.db

import com.github.locxter.bkndmvrgnzr.backend.book.db.Book
import com.github.locxter.bkndmvrgnzr.backend.bookcontributor.api.BookContributorResponseBriefDto
import com.github.locxter.bkndmvrgnzr.backend.bookcontributor.api.BookContributorResponseDto
import com.github.locxter.bkndmvrgnzr.backend.bookrole.db.BookRole
import com.github.locxter.bkndmvrgnzr.backend.contributor.db.Contributor
import jakarta.persistence.*

@Entity
data class BookContributor(
    @Id
    @AttributeOverride(name = "value", column = Column(name = "id"))
    val id: BookContributorId = BookContributorId(),
    @ManyToOne
    @JoinColumn(name = "contributor_id")
    val contributor: Contributor = Contributor(),
    @ManyToOne
    @JoinColumn(name = "book_role_id")
    val bookRole: BookRole = BookRole(),
    @ManyToMany(mappedBy = "bookContributors")
    val books: List<Book> = ArrayList(),
) {
    fun toDto(): BookContributorResponseDto = BookContributorResponseDto(
        id.value,
        contributor.toBriefDto(),
        bookRole.toBriefDto(),
        books.sortedBy { it.isbn.value }.map { it.toBriefDto() }
    )

    fun toBriefDto(): BookContributorResponseBriefDto = BookContributorResponseBriefDto(
        id.value,
        contributor.toBriefDto(),
        bookRole.toBriefDto()
    )
}

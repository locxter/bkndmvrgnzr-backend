package com.github.locxter.bkndmvrgnzr.backend.bookcontributor.db

import com.github.locxter.bkndmvrgnzr.backend.book.db.Book
import com.github.locxter.bkndmvrgnzr.backend.bookcontributor.api.BookContributorResponseBriefDto
import com.github.locxter.bkndmvrgnzr.backend.bookcontributor.api.BookContributorResponseDto
import com.github.locxter.bkndmvrgnzr.backend.bookrole.db.BookRole
import com.github.locxter.bkndmvrgnzr.backend.contributor.db.Contributor
import jakarta.persistence.*
import org.springframework.data.domain.Sort

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
    @ManyToMany
    @JoinTable(
        name = "book_x_book_contributor",
        joinColumns = [JoinColumn(name = "book_contributor_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "isbn", referencedColumnName = "isbn")]
    )
    val books: List<Book> = ArrayList(),
) {
    fun toDto(): BookContributorResponseDto = BookContributorResponseDto(
        id.value,
        contributor.toBriefDto(),
        bookRole.toBriefDto(),
        books.sortedWith(Book).map { it.toBriefDto() }
    )

    fun toBriefDto(): BookContributorResponseBriefDto = BookContributorResponseBriefDto(
        id.value,
        contributor.toBriefDto(),
        bookRole.toBriefDto()
    )

    companion object : Comparator<BookContributor> {
        override fun compare(o1: BookContributor, o2: BookContributor): Int {
            val s1 = o1.contributor.lastName + o1.contributor.firstName + o1.bookRole.name
            val s2 = o2.contributor.lastName + o2.contributor.firstName + o2.bookRole.name
            return s1.compareTo(s2)
        }

        fun getSort(): Sort {
            return Sort.by(
                Sort.Direction.ASC,
                "contributor.lastName",
                "contributor.firstName",
                "bookRole.name"
            )
        }
    }
}

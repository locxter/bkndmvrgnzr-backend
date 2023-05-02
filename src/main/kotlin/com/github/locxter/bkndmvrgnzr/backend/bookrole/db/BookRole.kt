package com.github.locxter.bkndmvrgnzr.backend.bookrole.db

import com.github.locxter.bkndmvrgnzr.backend.book.db.Book
import com.github.locxter.bkndmvrgnzr.backend.bookcontributor.db.BookContributor
import com.github.locxter.bkndmvrgnzr.backend.bookrole.api.BookRoleResponseBriefDto
import com.github.locxter.bkndmvrgnzr.backend.bookrole.api.BookRoleResponseDto
import jakarta.persistence.*
import org.springframework.data.domain.Sort

@Entity
data class BookRole(
    @Id
    @AttributeOverride(name = "value", column = Column(name = "id"))
    val id: BookRoleId = BookRoleId(),
    val name: String = "",
    @OneToMany(mappedBy = "bookRole")
    val bookContributors: List<BookContributor> = ArrayList()
) {
    fun toDto(): BookRoleResponseDto = BookRoleResponseDto(
        id.value,
        name,
        bookContributors.sortedWith(BookContributor).map { it.toBriefDto() }
    )

    fun toBriefDto(): BookRoleResponseBriefDto = BookRoleResponseBriefDto(
        id.value,
        name
    )

    companion object : Comparator<BookRole> {
        override fun compare(o1: BookRole, o2: BookRole): Int {
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

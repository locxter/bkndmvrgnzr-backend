package com.github.locxter.bkndmvrgnzr.backend.bookrole.db

import com.github.locxter.bkndmvrgnzr.backend.bookcontributor.db.BookContributor
import com.github.locxter.bkndmvrgnzr.backend.bookrole.api.BookRoleResponseBriefDto
import com.github.locxter.bkndmvrgnzr.backend.bookrole.api.BookRoleResponseDto
import jakarta.persistence.*

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
        bookContributors.sortedBy { it.contributor.id.value }.map { it.toBriefDto() }
    )

    fun toBriefDto(): BookRoleResponseBriefDto = BookRoleResponseBriefDto(
        id.value,
        name
    )
}

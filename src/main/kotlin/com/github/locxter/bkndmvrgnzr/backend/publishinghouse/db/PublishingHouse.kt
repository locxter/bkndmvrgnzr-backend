package com.github.locxter.bkndmvrgnzr.backend.publishinghouse.db

import com.github.locxter.bkndmvrgnzr.backend.book.db.Book
import com.github.locxter.bkndmvrgnzr.backend.publishinghouse.api.PublishingHouseResponseBriefDto
import com.github.locxter.bkndmvrgnzr.backend.publishinghouse.api.PublishingHouseResponseDto
import jakarta.persistence.*
import org.springframework.data.domain.Sort

@Entity
data class PublishingHouse(
    @Id
    @AttributeOverride(name = "value", column = Column(name = "id"))
    val id: PublishingHouseId = PublishingHouseId(),
    val name: String = "",
    val country: String = "",
    val city: String = "",
    @OneToMany(mappedBy = "publishingHouse")
    val books: List<Book> = listOf()
) {
    fun toDto(): PublishingHouseResponseDto = PublishingHouseResponseDto(
        id.value,
        name,
        country,
        city,
        books.sortedWith(Book).map { it.toBriefDto() }
    )

    fun toBriefDto(): PublishingHouseResponseBriefDto = PublishingHouseResponseBriefDto(
        id.value,
        name,
        country,
        city
    )

    companion object : Comparator<PublishingHouse> {
        override fun compare(o1: PublishingHouse, o2: PublishingHouse): Int {
            val s1 = o1.name
            val s2 = o1.name
            return s1.compareTo(s2)
        }

        fun getSort(): Sort {
            return Sort.by(
                Sort.Direction.ASC,
                "name",
            )
        }
    }
}

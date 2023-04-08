package com.github.locxter.bkndmvrgnzr.backend.publishinghouse.db

import com.github.locxter.bkndmvrgnzr.backend.book.db.Book
import com.github.locxter.bkndmvrgnzr.backend.publishinghouse.api.PublishingHouseResponseBriefDto
import com.github.locxter.bkndmvrgnzr.backend.publishinghouse.api.PublishingHouseResponseDto
import jakarta.persistence.*

@Entity
data class PublishingHouse(
    @Id
    @AttributeOverride(name = "value", column = Column(name = "id"))
    val id: PublishingHouseId = PublishingHouseId(),
    val name: String = "",
    val country: String = "",
    val city: String = "",
    @OneToMany(mappedBy = "publishingHouse")
    val books: List<Book> = ArrayList()
) {
    fun toDto(): PublishingHouseResponseDto = PublishingHouseResponseDto(
        id.value,
        name,
        country,
        city,
        books.map { it.toBriefDto() }
    )

    fun toBriefDto(): PublishingHouseResponseBriefDto = PublishingHouseResponseBriefDto(
        id.value,
        name,
        country,
        city
    )
}
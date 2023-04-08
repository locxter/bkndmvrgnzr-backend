package com.github.locxter.bkndmvrgnzr.backend.publishinghouse.db

import com.github.locxter.bkndmvrgnzr.backend.book.db.Isbn
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PublishingHouseRepository : JpaRepository<PublishingHouse, PublishingHouseId> {
    fun findByName(name: String): List<PublishingHouse>
    fun findByCountry(country: String): List<PublishingHouse>
    fun findByCity(city: String): List<PublishingHouse>
    fun findByBooksIsbn(isbn: Isbn): PublishingHouse?
}
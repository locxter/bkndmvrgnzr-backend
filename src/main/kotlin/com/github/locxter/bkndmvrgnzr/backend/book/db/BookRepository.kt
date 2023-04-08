package com.github.locxter.bkndmvrgnzr.backend.book.db

import com.github.locxter.bkndmvrgnzr.backend.bookcontributor.db.BookContributorId
import com.github.locxter.bkndmvrgnzr.backend.genre.db.GenreId
import com.github.locxter.bkndmvrgnzr.backend.publishinghouse.db.PublishingHouseId
import com.github.locxter.bkndmvrgnzr.backend.user.db.UserId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BookRepository : JpaRepository<Book, Isbn> {
    fun findByTitle(title: String): List<Book>
    fun findBySubtitle(subtitle: String): List<Book>
    fun findByDescription(description: String): List<Book>
    fun findByYear(year: Int): List<Book>
    fun findByPages(pages: Int): List<Book>
    fun findByPublishingHouseId(publishingHouseId: PublishingHouseId): List<Book>
    fun findByGenresId(genreId: GenreId): List<Book>
    fun findByBookContributorsId(bookContributorId: BookContributorId): List<Book>
    fun findByUsersId(userId: UserId): List<Book>
}
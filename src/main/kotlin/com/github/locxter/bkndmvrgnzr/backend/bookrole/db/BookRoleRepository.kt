package com.github.locxter.bkndmvrgnzr.backend.bookrole.db

import com.github.locxter.bkndmvrgnzr.backend.bookcontributor.db.BookContributorId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BookRoleRepository : JpaRepository<BookRole, BookRoleId> {
    fun findByName(name: String): List<BookRole>
    fun findByBookContributorsId(bookContributorId: BookContributorId): BookRole?
}
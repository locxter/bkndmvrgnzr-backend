package com.github.locxter.bkndmvrgnzr.backend.bookcontributor.db

import com.github.locxter.bkndmvrgnzr.backend.book.db.Isbn
import com.github.locxter.bkndmvrgnzr.backend.bookrole.db.BookRoleId
import com.github.locxter.bkndmvrgnzr.backend.contributor.db.ContributorId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BookContributorRepository : JpaRepository<BookContributor, BookContributorId> {
    fun findByContributorId(contributorId: ContributorId): List<BookContributor>
    fun findByBookRoleId(bookRoleId: BookRoleId): List<BookContributor>
    fun findByBooksIsbn(isbn: Isbn): List<BookContributor>
}
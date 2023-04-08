package com.github.locxter.bkndmvrgnzr.backend.user.db

import com.github.locxter.bkndmvrgnzr.backend.book.db.Isbn
import com.github.locxter.bkndmvrgnzr.backend.movie.db.Isan
import com.github.locxter.bkndmvrgnzr.backend.role.db.RoleId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, UserId> {
    fun findByUsername(username: String): User?
    fun existsByUsername(username: String): Boolean
    fun findByFirstName(firstName: String): List<User>
    fun findByLastName(lastName: String): List<User>
    fun findByRolesId(roleId: RoleId): List<User>
    fun findByBooksIsbn(isbn: Isbn): List<User>
    fun findByMoviesIsan(isan: Isan): List<User>
}
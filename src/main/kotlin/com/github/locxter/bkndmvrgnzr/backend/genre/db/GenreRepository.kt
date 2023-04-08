package com.github.locxter.bkndmvrgnzr.backend.genre.db

import com.github.locxter.bkndmvrgnzr.backend.book.db.Isbn
import com.github.locxter.bkndmvrgnzr.backend.movie.db.Isan
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GenreRepository : JpaRepository<Genre, GenreId> {
    fun findByName(name: String): List<Genre>
    fun findByBooksIsbn(isbn: Isbn): List<Genre>
    fun findByMoviesIsan(isan: Isan): List<Genre>
}
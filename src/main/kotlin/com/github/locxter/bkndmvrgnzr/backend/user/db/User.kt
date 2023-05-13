package com.github.locxter.bkndmvrgnzr.backend.user.db

import com.github.locxter.bkndmvrgnzr.backend.book.db.Book
import com.github.locxter.bkndmvrgnzr.backend.movie.db.Movie
import com.github.locxter.bkndmvrgnzr.backend.role.db.Role
import com.github.locxter.bkndmvrgnzr.backend.user.api.UserResponseDto
import jakarta.persistence.*
import org.springframework.data.domain.Sort

@Entity
data class User(
    @Id
    @AttributeOverride(name = "value", column = Column(name = "id"))
    val id: UserId = UserId(),
    @Column(unique = true)
    val username: String = "",
    @AttributeOverride(name = "value", column = Column(name = "password"))
    val password: Password = Password(),
    val firstName: String = "",
    val lastName: String = "",
    @ManyToMany
    @JoinTable(
        name = "user_x_role",
        joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")]
    )
    val roles: List<Role> = mutableListOf(),
    @ManyToMany
    @JoinTable(
        name = "user_x_book",
        joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "isbn", referencedColumnName = "isbn")]
    )
    val books: List<Book> = mutableListOf(),
    @ManyToMany
    @JoinTable(
        name = "user_x_movie",
        joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "isan", referencedColumnName = "isan")]
    )
    val movies: List<Movie> = mutableListOf()
) {
    fun toDto(): UserResponseDto = UserResponseDto(
        id.value,
        username,
        firstName,
        lastName,
        roles.sortedWith(Role).map { it.toDto() },
        books.sortedWith(Book).map { it.toBriefDto() },
        movies.sortedWith(Movie).map { it.toBriefDto() }
    )

    fun toBriefDto(): UserResponseDto = UserResponseDto(
        id.value,
        username,
        firstName,
        lastName
    )

    companion object : Comparator<User> {
        override fun compare(o1: User, o2: User): Int {
            val s1 = o1.username
            val s2 = o1.username
            return s1.compareTo(s2)
        }

        fun getSort(): Sort {
            return Sort.by(
                Sort.Direction.ASC,
                "username",
            )
        }
    }
}

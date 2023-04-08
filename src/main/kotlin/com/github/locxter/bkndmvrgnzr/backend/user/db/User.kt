package com.github.locxter.bkndmvrgnzr.backend.user.db

import com.github.locxter.bkndmvrgnzr.backend.book.db.Book
import com.github.locxter.bkndmvrgnzr.backend.movie.db.Movie
import com.github.locxter.bkndmvrgnzr.backend.role.db.Role
import com.github.locxter.bkndmvrgnzr.backend.user.api.UserResponseDto
import jakarta.persistence.*

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
        name = "user_role",
        joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")]
    )
    val roles: List<Role> = ArrayList(),
    @ManyToMany(cascade = [CascadeType.ALL])
    @JoinTable(
        name = "user_book",
        joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "isbn", referencedColumnName = "isbn")]
    )
    val books: List<Book> = ArrayList(),
    @ManyToMany(cascade = [CascadeType.ALL])
    @JoinTable(
        name = "user_movie",
        joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "isan", referencedColumnName = "isan")]
    )
    val movies: List<Movie> = ArrayList()
) {
    fun toDto(): UserResponseDto = UserResponseDto(
        id.value,
        username,
        firstName,
        lastName,
        roles.map { it.toDto() },
        books.map { it.toBriefDto() },
        movies.map { it.toBriefDto() }
    )

    fun toBriefDto(): UserResponseDto = UserResponseDto(
        id.value,
        username,
        firstName,
        lastName
    )
}
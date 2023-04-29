package com.github.locxter.bkndmvrgnzr.backend.lib

import com.github.locxter.bkndmvrgnzr.backend.book.db.Book
import com.github.locxter.bkndmvrgnzr.backend.book.db.BookRepository
import com.github.locxter.bkndmvrgnzr.backend.book.db.Isbn
import com.github.locxter.bkndmvrgnzr.backend.bookcontributor.db.BookContributor
import com.github.locxter.bkndmvrgnzr.backend.bookcontributor.db.BookContributorRepository
import com.github.locxter.bkndmvrgnzr.backend.bookrole.db.BookRole
import com.github.locxter.bkndmvrgnzr.backend.bookrole.db.BookRoleRepository
import com.github.locxter.bkndmvrgnzr.backend.contributor.db.Contributor
import com.github.locxter.bkndmvrgnzr.backend.contributor.db.ContributorRepository
import com.github.locxter.bkndmvrgnzr.backend.genre.db.Genre
import com.github.locxter.bkndmvrgnzr.backend.genre.db.GenreRepository
import com.github.locxter.bkndmvrgnzr.backend.movie.db.MovieRepository
import com.github.locxter.bkndmvrgnzr.backend.moviecontributor.db.MovieContributorRepository
import com.github.locxter.bkndmvrgnzr.backend.movierole.db.MovieRole
import com.github.locxter.bkndmvrgnzr.backend.movierole.db.MovieRoleRepository
import com.github.locxter.bkndmvrgnzr.backend.publishinghouse.db.PublishingHouse
import com.github.locxter.bkndmvrgnzr.backend.publishinghouse.db.PublishingHouseRepository
import com.github.locxter.bkndmvrgnzr.backend.role.db.ERole
import com.github.locxter.bkndmvrgnzr.backend.role.db.Role
import com.github.locxter.bkndmvrgnzr.backend.role.db.RoleRepository
import com.github.locxter.bkndmvrgnzr.backend.user.db.Password
import com.github.locxter.bkndmvrgnzr.backend.user.db.User
import com.github.locxter.bkndmvrgnzr.backend.user.db.UserRepository
import org.springframework.stereotype.Component


@Component
class DataLoader(
    private val bookRoleRepository: BookRoleRepository,
    private val genreRepository: GenreRepository,
    private val movieRoleRepository: MovieRoleRepository,
    private val roleRepository: RoleRepository,
    private val userRepository: UserRepository
) {

    fun loadData() {
        val bookRoleAuthor = bookRoleRepository.save<BookRole>(BookRole(name = "Author"))
        val bookRoleTranslator = bookRoleRepository.save<BookRole>(BookRole(name = "Translator"))
        val bookRoleEditor = bookRoleRepository.save<BookRole>(BookRole(name = "Editor"))
        val bookRolePublisher = bookRoleRepository.save<BookRole>(BookRole(name = "Publisher"))
        val genreAction = genreRepository.save<Genre>(Genre(name = "Action"))
        val genreAdventure = genreRepository.save<Genre>(Genre(name = "Adventure"))
        val genreAnimation = genreRepository.save<Genre>(Genre(name = "Animation"))
        val genreComedy = genreRepository.save<Genre>(Genre(name = "Comedy"))
        val genreDrama = genreRepository.save<Genre>(Genre(name = "Drama"))
        val genreFantasy = genreRepository.save<Genre>(Genre(name = "Fantasy"))
        val genreHistory = genreRepository.save<Genre>(Genre(name = "History"))
        val genreHorror = genreRepository.save<Genre>(Genre(name = "Horror"))
        val genreMusical = genreRepository.save<Genre>(Genre(name = "Musical"))
        val genreNoir = genreRepository.save<Genre>(Genre(name = "Noir"))
        val genreRomance = genreRepository.save<Genre>(Genre(name = "Romance"))
        val genreScienceFiction = genreRepository.save<Genre>(Genre(name = "Science fiction"))
        val genreThriller = genreRepository.save<Genre>(Genre(name = "Thriller"))
        val genreWestern = genreRepository.save<Genre>(Genre(name = "Western"))
        val movieRoleDirector = movieRoleRepository.save<MovieRole>(MovieRole(name = "Director"))
        val movieRoleScreenwriter = movieRoleRepository.save<MovieRole>(MovieRole(name = "Screenwriter"))
        val movieRoleProducer = movieRoleRepository.save<MovieRole>(MovieRole(name = "Producer"))
        val movieRoleComposer = movieRoleRepository.save<MovieRole>(MovieRole(name = "Composer"))
        val movieRoleCinematographer = movieRoleRepository.save<MovieRole>(MovieRole(name = "Cinematographer"))
        val movieRoleCutter = movieRoleRepository.save<MovieRole>(MovieRole(name = "Cutter"))
        val movieRoleActor = movieRoleRepository.save<MovieRole>(MovieRole(name = "Actor"))
        val roleUser = roleRepository.save(Role(type = ERole.ROLE_USER))
        val roleEditor = roleRepository.save(Role(type = ERole.ROLE_EDITOR))
        val roleAdmin = roleRepository.save(Role(type = ERole.ROLE_ADMIN))
        val userUser = userRepository.save(
            User(
                username = "user",
                password = Password("password"),
                roles = arrayListOf(
                    roleRepository.findByType(ERole.ROLE_USER) ?: throw Exception("ROlE_USER not found")
                )
            )
        )
        val userEditor = userRepository.save(
            User(
                username = "editor",
                password = Password("password"),
                roles = arrayListOf(
                    roleRepository.findByType(ERole.ROLE_USER) ?: throw Exception("ROlE_USER not found"),
                    roleRepository.findByType(ERole.ROLE_EDITOR) ?: throw Exception("ROLE_EDITOR not found")
                )
            )
        )
        val userAdmin = userRepository.save(
            User(
                username = "admin",
                password = Password("password"),
                roles = roleRepository.findAll()
            )
        )
    }
}

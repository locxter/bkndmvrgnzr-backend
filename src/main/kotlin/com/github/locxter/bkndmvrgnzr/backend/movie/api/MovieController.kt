package com.github.locxter.bkndmvrgnzr.backend.movie.api

import com.github.locxter.bkndmvrgnzr.backend.contributor.db.ContributorId
import com.github.locxter.bkndmvrgnzr.backend.contributor.db.ContributorRepository
import com.github.locxter.bkndmvrgnzr.backend.genre.db.Genre
import com.github.locxter.bkndmvrgnzr.backend.genre.db.GenreId
import com.github.locxter.bkndmvrgnzr.backend.genre.db.GenreRepository
import com.github.locxter.bkndmvrgnzr.backend.movie.db.Isan
import com.github.locxter.bkndmvrgnzr.backend.movie.db.Movie
import com.github.locxter.bkndmvrgnzr.backend.movie.db.MovieRepository
import com.github.locxter.bkndmvrgnzr.backend.moviecontributor.db.MovieContributor
import com.github.locxter.bkndmvrgnzr.backend.moviecontributor.db.MovieContributorId
import com.github.locxter.bkndmvrgnzr.backend.moviecontributor.db.MovieContributorRepository
import com.github.locxter.bkndmvrgnzr.backend.user.db.UserRepository
import me.xdrop.fuzzywuzzy.FuzzySearch
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/movie")
class MovieController(
    private val movieRepository: MovieRepository,
    private val movieContributorRepository: MovieContributorRepository,
    private val userRepository: UserRepository,
    private val genreRepository: GenreRepository,
    private val contributorRepository: ContributorRepository
) {
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    fun getAllMovies(): List<MovieResponseDto> {
        val movies = movieRepository.findAll(Movie.getSort())
        return movies.map { it.toDto() }
    }

    @PostMapping
    @PreAuthorize("hasRole('EDITOR')")
    fun createMovie(@RequestBody movieCreateDto: MovieCreateDto): MovieResponseDto {
        if (movieCreateDto.isan.isBlank() || movieCreateDto.title.isBlank() || movieCreateDto.year < 0 ||
            movieCreateDto.playTime < 0 || movieCreateDto.ageRestriction < 0 || movieCreateDto.ageRestriction > 18
        ) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Sent movie not valid")
        }
        try {
            Isan(movieCreateDto.isan)
        } catch (exception: IllegalArgumentException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Sent movie not valid")
        }
        if (movieRepository.existsById(Isan(movieCreateDto.isan))) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Sent movie not valid")
        }
        val genres: ArrayList<Genre> = ArrayList()
        for (genreId in movieCreateDto.genreIds) {
            val genre = genreRepository.findById(GenreId(genreId)).orElse(null) ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND, "Requested genre not found"
            )
            genres.add(genre)
        }
        val movieContributors: ArrayList<MovieContributor> = ArrayList()
        for (movieContributorId in movieCreateDto.movieContributorIds) {
            val movieContributor =
                movieContributorRepository.findById(MovieContributorId(movieContributorId)).orElse(null)
                    ?: throw ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Requested movie contributor not found"
                    )
            movieContributors.add(movieContributor)
        }
        val movie = Movie(
            Isan(movieCreateDto.isan),
            movieCreateDto.title,
            movieCreateDto.description,
            movieCreateDto.year,
            movieCreateDto.playTime,
            movieCreateDto.ageRestriction,
            genres,
            movieContributors
        )
        movieRepository.save(movie)
        return movie.toDto()
    }

    @GetMapping("/{isan}")
    @PreAuthorize("hasRole('USER')")
    fun getMovie(@PathVariable(name = "isan") isan: String): MovieResponseDto {
        val movie = movieRepository.findById(Isan(isan)).orElse(null)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Requested movie not found")
        return movie.toDto()
    }

    @PutMapping("/{isan}")
    @PreAuthorize("hasRole('EDITOR')")
    fun updateMovie(
        @PathVariable(name = "isan") isan: String,
        @RequestBody movieUpdateDto: MovieUpdateDto
    ): MovieResponseDto {
        val movie = movieRepository.findById(Isan(isan)).orElse(null)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Requested movie not found")
        if (movieUpdateDto.year < 0 || movieUpdateDto.playTime < 0 ||
            (movieUpdateDto.ageRestriction != -1 && (movieUpdateDto.ageRestriction < 0 || movieUpdateDto.ageRestriction > 18))
        ) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Sent movie not valid")
        }
        val genres: ArrayList<Genre> = ArrayList()
        for (genreId in movieUpdateDto.genreIds) {
            val genre = genreRepository.findById(GenreId(genreId)).orElse(null) ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND, "Requested genre not found"
            )
            genres.add(genre)
        }
        val movieContributors: ArrayList<MovieContributor> = ArrayList()
        for (movieContributorId in movieUpdateDto.movieContributorIds) {
            val movieContributor =
                movieContributorRepository.findById(MovieContributorId(movieContributorId)).orElse(null)
                    ?: throw ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Requested movie contributor not found"
                    )
            movieContributors.add(movieContributor)
        }
        val updatedMovie = movie.copy(
            title = movieUpdateDto.title.ifBlank { movie.title },
            description = movieUpdateDto.description.ifBlank { movie.description },
            year = if (movieUpdateDto.year != 0) movieUpdateDto.year else movie.year,
            playTime = if (movieUpdateDto.playTime != 0) movieUpdateDto.playTime else movie.playTime,
            ageRestriction = if (movieUpdateDto.ageRestriction != -1) movieUpdateDto.ageRestriction else movie.ageRestriction,
            genres = genres.ifEmpty { movie.genres },
            movieContributors = movieContributors.ifEmpty { movie.movieContributors }
        )
        movieRepository.save(updatedMovie)
        return updatedMovie.toDto()
    }

    @DeleteMapping("/{isan}")
    @PreAuthorize("hasRole('EDITOR')")
    fun deleteMovie(@PathVariable(name = "isan") isan: String): MovieResponseDto {
        val movie = movieRepository.findById(Isan(isan)).orElse(null)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Requested movie not found")
        val movieDto = movie.toDto()
        movieRepository.delete(movie)
        return movieDto
    }

    @GetMapping("/genre/{genreId}")
    @PreAuthorize("hasRole('USER')")
    fun getAllMoviesOfGenre(@PathVariable(name = "genreId") genreId: String): List<MovieResponseDto> {
        val genre = genreRepository.findById(GenreId(genreId)).orElse(null) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Requested genre not found"
        )
        val movies = movieRepository.findByGenresId(genre.id, Movie.getSort())
        return movies.map { it.toDto() }
    }

    @GetMapping("/movie-contributor/{movieContributorId}")
    @PreAuthorize("hasRole('USER')")
    fun getAllMoviesOfMovieContributor(@PathVariable(name = "movieContributorId") movieContributorId: String): List<MovieResponseDto> {
        val movieContributor =
            movieContributorRepository.findById(MovieContributorId(movieContributorId)).orElse(null)
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Requested movie contributor not found")
        val movies =
            movieRepository.findByMovieContributorsId(movieContributor.id, Movie.getSort())
        return movies.map { it.toDto() }
    }

    @GetMapping("/contributor/{contributorId}")
    @PreAuthorize("hasRole('USER')")
    fun getAllMoviesOfContributor(@PathVariable(name = "contributorId") contributorId: String): List<MovieResponseDto> {
        val contributor = contributorRepository.findById(ContributorId(contributorId)).orElse(null)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Requested contributor not found")
        val movieContributors = movieContributorRepository.findByContributorId(contributor.id)
        val movies: ArrayList<Movie> = ArrayList()
        for (movieContributor in movieContributors) {
            movies.addAll(movieRepository.findByMovieContributorsId(movieContributor.id))
        }
        movies.sortWith(Movie)
        return movies.map { it.toDto() }
    }

    @GetMapping("/search/{query}")
    @PreAuthorize("hasRole('USER')")
    fun getAllMoviesOfSearchQuery(@PathVariable(name = "query") query: String): List<MovieResponseDto> {
        val movies = movieRepository.findAll(Movie.getSort())
        val iterator = movies.iterator()
        while (iterator.hasNext()) {
            val movie = iterator.next()
            var containsQuery = false
            if (FuzzySearch.weightedRatio(movie.title, query) >= 90 ||
                FuzzySearch.weightedRatio(movie.description, query) >= 90 ||
                movie.year == query.toIntOrNull() || movie.playTime == query.toIntOrNull() ||
                movie.ageRestriction == query.toIntOrNull()
            ) {
                containsQuery = true
            } else {
                for (genre in movie.genres) {
                    if (FuzzySearch.weightedRatio(genre.name, query) >= 90) {
                        containsQuery = true
                        break
                    }
                }
                for (movieContributor in movie.movieContributors) {
                    if (FuzzySearch.weightedRatio(movieContributor.contributor.firstName, query) >= 90 ||
                        FuzzySearch.weightedRatio(movieContributor.contributor.lastName, query) >= 90 ||
                        FuzzySearch.weightedRatio(
                            movieContributor.contributor.firstName + ' ' + movieContributor.contributor.lastName,
                            query
                        ) >= 90 ||
                        FuzzySearch.weightedRatio(movieContributor.movieRole.name, query) >= 90
                    ) {
                        containsQuery = true
                        break
                    }
                }
            }
            if (!containsQuery) {
                iterator.remove()
            }
        }
        return movies.map { it.toDto() }
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    fun getAllMoviesOfUser(authentication: Authentication): List<MovieResponseDto> {
        val user = userRepository.findByUsername(authentication.name) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Requested user not found"
        )
        val movies = movieRepository.findByUsersId(user.id, Movie.getSort())
        return movies.map { it.toDto() }
    }

    @PostMapping("/user/{isan}")
    @PreAuthorize("hasRole('USER')")
    fun addMovieToUser(
        authentication: Authentication,
        @PathVariable(name = "isan") isan: String
    ): List<MovieResponseDto> {
        val user = userRepository.findByUsername(authentication.name) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Requested user not found"
        )
        val movie = movieRepository.findById(Isan(isan)).orElse(null)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Requested movie not found")
        val userMovies = user.movies.sortedWith(Movie).toMutableList()
        if (!userMovies.contains(movie)) {
            userMovies.add(movie)
            userMovies.sortWith(Movie)
            val updatedUser = user.copy(movies = userMovies)
            userRepository.save(updatedUser)
        }
        return userMovies.map { it.toDto() }
    }

    @DeleteMapping("/user/{isan}")
    @PreAuthorize("hasRole('USER')")
    fun removeMovieFromUser(
        authentication: Authentication,
        @PathVariable(name = "isan") isan: String
    ): List<MovieResponseDto> {
        val user = userRepository.findByUsername(authentication.name) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Requested user not found"
        )
        val movie = movieRepository.findById(Isan(isan)).orElse(null)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Requested movie not found")
        val userMovies = user.movies.sortedWith(Movie).toMutableList()
        if (userMovies.contains(movie)) {
            userMovies.remove(movie)
            val updatedUser = user.copy(movies = userMovies)
            userRepository.save(updatedUser)
        }
        return userMovies.map { it.toDto() }
    }

    @GetMapping("/user/genre/{genreId}")
    @PreAuthorize("hasRole('USER')")
    fun getAllMoviesOfGenreFromUser(
        authentication: Authentication,
        @PathVariable(name = "genreId") genreId: String
    ): List<MovieResponseDto> {
        val user = userRepository.findByUsername(authentication.name) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Requested user not found"
        )
        val genre = genreRepository.findById(GenreId(genreId)).orElse(null) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Requested genre not found"
        )
        val movies = movieRepository.findByUsersId(user.id, Movie.getSort()).toMutableList()
        val iterator = movies.iterator()
        while (iterator.hasNext()) {
            val movie = iterator.next()
            if (!movie.genres.contains(genre)) {
                iterator.remove()
            }
        }
        return movies.map { it.toDto() }
    }

    @GetMapping("/user/movie-contributor/{movieContributorId}")
    @PreAuthorize("hasRole('USER')")
    fun getAllMoviesOfMoviesContributorFromUser(
        authentication: Authentication,
        @PathVariable(name = "movieContributorId") movieContributorId: String
    ): List<MovieResponseDto> {
        val user = userRepository.findByUsername(authentication.name) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Requested user not found"
        )
        val movieContributor =
            movieContributorRepository.findById(MovieContributorId(movieContributorId)).orElse(null)
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Requested movie contributor not found")
        val movies = movieRepository.findByUsersId(user.id, Movie.getSort()).toMutableList()
        val iterator = movies.iterator()
        while (iterator.hasNext()) {
            val movie = iterator.next()
            if (!movie.movieContributors.contains(movieContributor)) {
                iterator.remove()
            }
        }
        return movies.map { it.toDto() }
    }

    @GetMapping("/user/contributor/{contributorId}")
    @PreAuthorize("hasRole('USER')")
    fun getAllMoviesOfContributorFromUser(
        authentication: Authentication,
        @PathVariable(name = "contributorId") contributorId: String
    ): List<MovieResponseDto> {
        val user = userRepository.findByUsername(authentication.name) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Requested user not found"
        )
        val contributor = contributorRepository.findById(ContributorId(contributorId)).orElse(null)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Requested contributor not found")
        val movieContributors = movieContributorRepository.findByContributorId(contributor.id)
        val movies = movieRepository.findByUsersId(user.id, Movie.getSort()).toMutableList()
        val iterator = movies.iterator()
        while (iterator.hasNext()) {
            val movie = iterator.next()
            var containsMovieContributor = false
            for (movieContributor in movieContributors) {
                if (movie.movieContributors.contains(movieContributor)) {
                    containsMovieContributor = true
                    break
                }
            }
            if (!containsMovieContributor) {
                iterator.remove()
            }
        }
        return movies.map { it.toDto() }
    }

    @GetMapping("/user/search/{query}")
    @PreAuthorize("hasRole('USER')")
    fun getAllMoviesOfSearchQueryFromUser(
        authentication: Authentication,
        @PathVariable(name = "query") query: String
    ): List<MovieResponseDto> {
        val user = userRepository.findByUsername(authentication.name) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Requested user not found"
        )
        val movies = movieRepository.findByUsersId(user.id, Movie.getSort()).toMutableList()
        val iterator = movies.iterator()
        while (iterator.hasNext()) {
            val movie = iterator.next()
            var containsQuery = false
            if (FuzzySearch.weightedRatio(movie.title, query) >= 90 ||
                FuzzySearch.weightedRatio(movie.description, query) >= 90 ||
                movie.year == query.toIntOrNull() || movie.playTime == query.toIntOrNull() ||
                movie.ageRestriction == query.toIntOrNull()
            ) {
                containsQuery = true
            } else {
                for (genre in movie.genres) {
                    if (FuzzySearch.weightedRatio(genre.name, query) >= 90) {
                        containsQuery = true
                        break
                    }
                }
                for (movieContributor in movie.movieContributors) {
                    if (FuzzySearch.weightedRatio(movieContributor.contributor.firstName, query) >= 90 ||
                        FuzzySearch.weightedRatio(movieContributor.contributor.lastName, query) >= 90 ||
                        FuzzySearch.weightedRatio(
                            movieContributor.contributor.firstName + ' ' + movieContributor.contributor.lastName,
                            query
                        ) >= 90 ||
                        FuzzySearch.weightedRatio(movieContributor.movieRole.name, query) >= 90
                    ) {
                        containsQuery = true
                        break
                    }
                }
            }
            if (!containsQuery) {
                iterator.remove()
            }
        }
        return movies.map { it.toDto() }
    }
}

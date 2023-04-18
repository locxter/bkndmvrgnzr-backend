# bkndmvrgnzr backend

## Overview

> Please keep in mind that this is an **early work in progress**.

This repository includes the backend of my simple and open source book and movie organizer project bkndmvrgnzr (inofficially also BAMO), which I started building during my work placement at [CEWE](https://www.cewe.de/). It has the following features:

- [ ] Keep track of all your books and movie with advanced sorting options.
- [ ] Import information from reputable online providers.
- [ ] Group movies and book by genres, artists, year of publication and more.
- [ ] Allow different users to selectively interact with the database via permissions.

## Dependencies

I generally try to minimize dependencies, but I'm a one man crew and can therefore only support Debian-based Linux
distributions as I'm running one myself. Anyway, you need to have the following packages installed for everything to
work properly:

- JDK for running the bytecode. Install it with `sudo apt install openjdk-17-jdk`.
- Kotlin for developing the program. Install it with `sudo apt install kotlin`.
- Gradle for building the whole thing. Install it using [this PPA](https://launchpad.net/~cwchien/+archive/ubuntu/gradle) as the default package is outdated.
- MariaDB as a database for storage. Install it with `sudo apt install mariadb-server`.

## How to use it

First secure the mariaDB installation via `sudo mysql_secure_installation` (choose `Enter`, then `N` twice and finally `Y` for all following questions), login to mariaDB via `sudo mysql -u root`, create the needed database via `create database bkndmvrgnzr;` as well as user via `grant all privileges on bkndmvrgnzr.* to 'bkndmvrgnzr' identified by 'bkndmvrgnzr';`. Then build the JAR via `gradle clean bootJar` and then run it via `java -jar build/libs/bkndmvrgnzr-backend-1.0.jar`.

## Data providers

- [ ] [TMDB](https://www.themoviedb.org/documentation/api)
- [ ] [ISAN](https://support.isan.org/hc/en-us/sections/360005677039-ISAN-API-3-0-Guide)
- [ ] [OpenLibrary](https://openlibrary.org/developers/api)
- [ ] ...

## Data model

![Data model](data-model.svg)

## API

This project uses Swagger to programmatically generate an up-to-date API documentation, which can be visited at [`http://locahost:8080/docs/swagger-ui.html`](http://locahost:8080/docs/swagger-ui.html), but here is the rough API structure I designed during initial development.

### Auth

- [x] Login: POST /api/auth

### Book

User-agnostic:

- [x] Get all books: GET /api/book ROLE_USER
- [x] Add a book: POST /api/book ROLE_EDITOR
- [x] Get a specific book: GET /api/book/{isbn} ROLE_USER
- [x] Update a specific book: PUT /api/book/{isbn} ROLE_EDITOR
- [x] Delete a specific book: DELETE /api/book/{isbn} ROLE_EDITOR
- [x] Get all books of genre: GET /api/book/genre/{genre-id} ROLE_USER
- [x] Get all books of publishing house: GET /api/book/publishing-house/{publishing-house-id} ROLE_USER
- [x] Get all books of book contributor: GET /api/book/book-contributor/{book-contributor-id} ROLE_USER
- [x] Get all books of contributor: GET /api/book/contributor/{contributor-id} ROLE_USER
- [x] Get all books of search query: GET /api/book/search/{query}

User-specific:

- [x] Get all books of user: GET /api/book/user ROLE_USER
- [x] Add a book to user: POST /api/book/user/{isbn} ROLE_USER
- [x] Remove a book from user: DELETE /api/book/user/{isbn} ROLE_USER
- [x] Get all books of genre from user: GET /api/book/user/genre/{genre-id} ROLE_USER
- [x] Get all books of publishing house from user: GET /api/book/user/publishing-house/{publishing-house-id} ROLE_USER
- [x] Get all books of book contributor from user: GET /api/book/user/book-contributor/{book-contributor-id} ROLE_USER
- [x] Get all books of contributor from user: GET /api/book/user/contributor/{contributor-id} ROLE_USER

### Book contributor

- [x] Get all book contributors: GET /api/book-contributor ROLE_USER
- [x] Add a book contributor: POST /api/book-contributor ROLE_EDITOR
- [x] Get a specific book contributor: GET /api/book-contributor/{book-contributor-id} ROLE_USER
- [x] Update a specific book contributor: PUT /api/book-contributor/{book-contributor-id} ROLE_EDITOR
- [x] Delete a specific book contributor: DELETE /api/book-contributor/{book-contributor-id} ROLE_EDITOR
- [x] Get all book contributors of contributor: GET /api/book-contributor/contributor/{contributor-id} ROLE_USER
- [x] Get all book contributors of book role: GET /api/book-contributor/book-role/{book-role-id} ROLE_USER

### Book role

- [x] Get all book roles: GET /api/book-role ROLE_USER
- [x] Add a book role: POST /api/book-role ROLE_EDITOR
- [x] Get a specific book role: GET /api/book-role/{book-role-id} ROLE_USER
- [x] Update a specific book role: PUT /api/book-role/{book-role-id} ROLE_EDITOR
- [x] Delete a specific book role: DELETE /api/book-role/{book-role-id} ROLE_EDITOR

### Contributor

- [x] Get all contributors: GET /api/contributor ROLE_USER
- [x] Add a contributor: POST /api/contributor ROLE_EDITOR
- [x] Get a specific contributor: GET /api/contributor/{contributor-id} ROLE_USER
- [x] Update a specific contributor: PUT /api/contributor/{contributor-id} ROLE_EDITOR
- [x] Delete a specific contributor: DELETE /api/contributor/{contributor-id} ROLE_EDITOR

### Genre

- [x] Get all genres: GET /api/genre ROLE_USER
- [x] Add a genre: POST /api/genre ROLE_EDITOR
- [x] Get a specific genre: GET /api/genre/{genre-id} ROLE_USER
- [x] Update a specific genre: PUT /api/genre/{genre-id} ROLE_EDITOR
- [x] Delete a specific genre: DELETE /api/genre/{genre-id} ROLE_EDITOR

### Movie

User-agnostic:

- [x] Get all movies: GET /api/movie ROLE_USER
- [x] Add a movie: POST /api/movie ROLE_EDITOR
- [x] Get a specific movie: GET /api/movie/{isan} ROLE_USER
- [x] Update a specific movie: PUT /api/movie/{isan} ROLE_EDITOR
- [x] Delete a specific movie: DELETE /api/movie/{isan} ROLE_EDITOR
- [x] Get all movies of genre: GET /api/movie/genre/{genre-id} ROLE_USER+
- [x] Get all movies of movie contributor: GET /api/movie/movie-contributor/{movie-contributor-id} ROLE_USER
- [x] Get all movies of contributor: GET /api/movie/contributor/{contributor-id} ROLE_USER
- [x] Get all movies of search query: GET /api/movie/search/{query}

User-specific:

- [x] Get all movies of user: GET /api/movie/user ROLE_USER
- [x] Add a movie to user: POST /api/movie/user/{isan} ROLE_USER
- [x] Remove a movie from user: DELETE /api/movie/user/{isan} ROLE_USER
- [x] Get all movies of genre from user: GET /api/movie/user/genre/{id} ROLE_USER
- [x] Get all movies of movie contributor from user: GET /api/movie/user/movie-contributor/{movie-contributor-id}
  ROLE_USER
- [x] Get all movies of contributor from user: GET /api/movie/user/contributor/{id} ROLE_USER

### Movie contributor

- [x] Get all movie contributors: GET /api/movie-contributor ROLE_USER
- [x] Add a movie contributor: POST /api/movie-contributor ROLE_EDITOR
- [x] Get a specific movie contributor: GET /api/movie-contributor/{movie-contributor-id} ROLE_USER
- [x] Update a specific movie contributor: PUT /api/movie-contributor/{movie-contributor-id} ROLE_EDITOR
- [x] Delete a specific movie contributor: DELETE /api/movie-contributor/{movie-contributor-id} ROLE_EDITOR
- [x] Get all movie contributors of contributor: GET /api/movie-contributor/contributor/{contributor-id} ROLE_USER
- [x] Get all movie contributors of movie role: GET /api/movie-contributor/movie-role/{movie-role-id} ROLE_USER

### Movie role

- [x] Get all movies roles: GET /api/movie-role ROLE_USER
- [x] Add a movies role: POST /api/movie-role ROLE_EDITOR
- [x] Get a specific movies role: GET /api/movie-role/{movie-role-id} ROLE_USER
- [x] Update a specific movies role: PUT /api/movie-role/{movie-role-id} ROLE_EDITOR
- [x] Delete a specific movies role: DELETE /api/movie-role/{movie-role-id} ROLE_EDITOR

### Publishing house

- [x] Get all publishing houses: GET /api/publishing-house ROLE_USER
- [x] Add a publishing house: POST /api/publishing-house ROLE_EDITOR
- [x] Get a specific publishing house: GET /api/publishing-house/{publishing-house-id} ROLE_USER
- [x] Update a specific publishing house: PUT /api/publishing-house/{publishing-house-id} ROLE_EDITOR
- [x] Delete a specific publishing house: DELETE /api/publishing-house/{publishing-house-id} ROLE_EDITOR

### Role

User-agnostic:

- [x] Get all roles: GET /api/role ROLE_USER
- [x] Get a specific role: GET /api/role/{role-id} ROLE_USER

User-specific:

- [x] Get all roles of user: GET /api/role/user ROLE_USER
- [x] Get all roles of specific user: GET /api/role/user/{user-id} ROLE_USER
- [x] Add a role to specific user: POST /api/role/user/{user-id}/role/{role-id} ROLE_ADMIN
- [x] Remove a role from specific user: DELETE /api/role/user/{user-id}/role/{role-id} ROLE_ADMIN

### User

- [x] Get user: GET /api/user ROLE_USER
- [x] Get all users: GET /api/user/all ROLE_ADMIN
- [x] Get a specific user: GET /api/user/{user-id} ROLE_ADMIN
- [x] Add a user: POST /api/user
- [x] Update user: PUT /api/user ROLE_USER
- [x] Update a specific user: PUT /api/user/{user-id} ROLE_ADMIN
- [x] Delete user: DELETE /api/user ROLE_USER
- [x] Delete a specific user: DELETE /api/user/{user-id} ROLE_ADMIN

## Frontend API

### Auth

- [x] Log in: POST /api/auth
- [x] Log out: DELETE /api/auth

### Book

User-agnostic:

- [x] Get all books: GET /api/book ROLE_USER
- [x] Add a book: POST /api/book ROLE_EDITOR
- [x] Get a specific book: GET /api/book/{isbn} ROLE_USER
- [x] Update a specific book: PUT /api/book/{isbn} ROLE_EDITOR
- [x] Delete a specific book: DELETE /api/book/{isbn} ROLE_EDITOR
- [x] Get all books of genre: GET /api/book/genre/{genre-id} ROLE_USER
- [x] Get all books of publishing house: GET /api/book/publishing-house/{publishing-house-id} ROLE_USER
- [x] Get all books of book contributor: GET /api/book/book-contributor/{book-contributor-id} ROLE_USER
- [x] Get all books of contributor: GET /api/book/contributor/{contributor-id} ROLE_USER
- [x] Get all books of search query: GET /api/book/search/{query}

User-specific:

- [x] Get all books of user: GET /api/book/user ROLE_USER
- [x] Add a book to user: POST /api/book/user/{isbn} ROLE_USER
- [x] Remove a book from user: DELETE /api/book/user/{isbn} ROLE_USER
- [x] Get all books of genre from user: GET /api/book/user/genre/{genre-id} ROLE_USER
- [x] Get all books of publishing house from user: GET /api/book/user/publishing-house/{publishing-house-id} ROLE_USER
- [x] Get all books of book contributor from user: GET /api/book/user/book-contributor/{book-contributor-id} ROLE_USER
- [x] Get all books of contributor from user: GET /api/book/user/contributor/{contributor-id} ROLE_USER
- [x] Get all books of search query from user: GET /api/book/user/search/{query}

### Book contributor

- [x] Get all book contributors: GET /api/book-contributor ROLE_USER
- [x] Add a book contributor: POST /api/book-contributor ROLE_EDITOR
- [x] Get a specific book contributor: GET /api/book-contributor/{book-contributor-id} ROLE_USER
- [x] Update a specific book contributor: PUT /api/book-contributor/{book-contributor-id} ROLE_EDITOR
- [x] Delete a specific book contributor: DELETE /api/book-contributor/{book-contributor-id} ROLE_EDITOR
- [x] Get all book contributors of contributor: GET /api/book-contributor/contributor/{contributor-id} ROLE_USER
- [x] Get all book contributors of book role: GET /api/book-contributor/book-role/{book-role-id} ROLE_USER

### Book role

- [x] Get all book roles: GET /api/book-role ROLE_USER
- [x] Add a book role: POST /api/book-role ROLE_EDITOR
- [x] Get a specific book role: GET /api/book-role/{book-role-id} ROLE_USER
- [x] Update a specific book role: PUT /api/book-role/{book-role-id} ROLE_EDITOR
- [x] Delete a specific book role: DELETE /api/book-role/{book-role-id} ROLE_EDITOR

### Contributor

- [x] Get all contributors: GET /api/contributor ROLE_USER
- [x] Add a contributor: POST /api/contributor ROLE_EDITOR
- [x] Get a specific contributor: GET /api/contributor/{contributor-id} ROLE_USER
- [x] Update a specific contributor: PUT /api/contributor/{contributor-id} ROLE_EDITOR
- [x] Delete a specific contributor: DELETE /api/contributor/{contributor-id} ROLE_EDITOR

### Genre

- [x] Get all genres: GET /api/genre ROLE_USER
- [x] Add a genre: POST /api/genre ROLE_EDITOR
- [x] Get a specific genre: GET /api/genre/{genre-id} ROLE_USER
- [x] Update a specific genre: PUT /api/genre/{genre-id} ROLE_EDITOR
- [x] Delete a specific genre: DELETE /api/genre/{genre-id} ROLE_EDITOR

### Movie

User-agnostic:

- [x] Get all movies: GET /api/movie ROLE_USER
- [x] Add a movie: POST /api/movie ROLE_EDITOR
- [x] Get a specific movie: GET /api/movie/{isan} ROLE_USER
- [x] Update a specific movie: PUT /api/movie/{isan} ROLE_EDITOR
- [x] Delete a specific movie: DELETE /api/movie/{isan} ROLE_EDITOR
- [x] Get all movies of genre: GET /api/movie/genre/{genre-id} ROLE_USER+
- [x] Get all movies of movie contributor: GET /api/movie/movie-contributor/{movie-contributor-id} ROLE_USER
- [x] Get all movies of contributor: GET /api/movie/contributor/{contributor-id} ROLE_USER
- [x] Get all movies of search query: GET /api/movie/search/{query}

User-specific:

- [x] Get all movies of user: GET /api/movie/user ROLE_USER
- [x] Add a movie to user: POST /api/movie/user/{isan} ROLE_USER
- [x] Remove a movie from user: DELETE /api/movie/user/{isan} ROLE_USER
- [x] Get all movies of genre from user: GET /api/movie/user/genre/{id} ROLE_USER
- [x] Get all movies of movie contributor from user: GET /api/movie/user/movie-contributor/{movie-contributor-id}
  ROLE_USER
- [x] Get all movies of contributor from user: GET /api/movie/user/contributor/{id} ROLE_USER
- [x] Get all movies of search query from user: GET /api/user/movie/search/{query}

### Movie contributor

- [x] Get all movie contributors: GET /api/movie-contributor ROLE_USER
- [x] Add a movie contributor: POST /api/movie-contributor ROLE_EDITOR
- [x] Get a specific movie contributor: GET /api/movie-contributor/{movie-contributor-id} ROLE_USER
- [x] Update a specific movie contributor: PUT /api/movie-contributor/{movie-contributor-id} ROLE_EDITOR
- [x] Delete a specific movie contributor: DELETE /api/movie-contributor/{movie-contributor-id} ROLE_EDITOR
- [x] Get all movie contributors of contributor: GET /api/movie-contributor/contributor/{contributor-id} ROLE_USER
- [x] Get all movie contributors of movie role: GET /api/movie-contributor/movie-role/{movie-role-id} ROLE_USER

### Movie role

- [x] Get all movies roles: GET /api/movie-role ROLE_USER
- [x] Add a movies role: POST /api/movie-role ROLE_EDITOR
- [x] Get a specific movies role: GET /api/movie-role/{movie-role-id} ROLE_USER
- [x] Update a specific movies role: PUT /api/movie-role/{movie-role-id} ROLE_EDITOR
- [x] Delete a specific movies role: DELETE /api/movie-role/{movie-role-id} ROLE_EDITOR

### Publishing house

- [x] Get all publishing houses: GET /api/publishing-house ROLE_USER
- [x] Add a publishing house: POST /api/publishing-house ROLE_EDITOR
- [x] Get a specific publishing house: GET /api/publishing-house/{publishing-house-id} ROLE_USER
- [x] Update a specific publishing house: PUT /api/publishing-house/{publishing-house-id} ROLE_EDITOR
- [x] Delete a specific publishing house: DELETE /api/publishing-house/{publishing-house-id} ROLE_EDITOR

### Role

User-agnostic:

- [x] Get all roles: GET /api/role ROLE_USER
- [x] Get a specific role: GET /api/role/{role-id} ROLE_USER

User-specific:

- [x] Get all roles of user: GET /api/role/user ROLE_USER
- [x] Get all roles of specific user: GET /api/role/user/{user-id} ROLE_USER
- [x] Add a role to specific user: POST /api/role/user/{user-id}/role/{role-id} ROLE_ADMIN
- [x] Remove a role from specific user: DELETE /api/role/user/{user-id}/role/{role-id} ROLE_ADMIN

### User

- [x] Get user: GET /api/user ROLE_USER
- [x] Get all users: GET /api/user/all ROLE_ADMIN
- [x] Get a specific user: GET /api/user/{user-id} ROLE_ADMIN
- [x] Add a user: POST /api/user
- [x] Update user: PUT /api/user ROLE_USER
- [x] Update a specific user: PUT /api/user/{user-id} ROLE_ADMIN
- [x] Delete user: DELETE /api/user ROLE_USER
- [x] Delete a specific user: DELETE /api/user/{user-id} ROLE_ADMIN

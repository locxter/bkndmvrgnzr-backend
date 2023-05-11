# bkndmvrgnzr backend

## Overview

> Please keep in mind that this is a **work in progress**.

This repository includes the backend of my simple and open source book and movie organizer project bkndmvrgnzr (inofficially also BAMO), which I started building during my work placement at [CEWE](https://www.cewe.de/). It has the following features:

- [x] Keep track of all your books and movies
- [x] Import information from reputable online providers
- [x] Find movies and books by genres, contributors, year of publication and more
- [x] Allow different users to selectively interact with the database via permissions

## Dependencies

I generally try to minimize dependencies, but I'm a one man crew and can therefore only support Debian-based Linux
distributions as I'm running one myself. Anyway, you need to have the following packages installed for everything to
work properly:

- JDK for running the bytecode. Install it with `sudo apt install openjdk-17-jdk`.
- Kotlin for developing the program. Install it with `sudo apt install kotlin`.
- Gradle for building the whole thing. Install it using [this PPA](https://launchpad.net/~cwchien/+archive/ubuntu/gradle) as the default package is outdated.
- MariaDB as a database for storage. Install it with `sudo apt install mariadb-server`.

## How to use it

First secure the mariaDB installation via `sudo mysql_secure_installation` (choose `Enter`, then `N` twice and finally `Y` for all following questions), login to mariaDB via `sudo mysql -u root`, create the needed database via `create database bkndmvrgnzr;` as well as user via `grant all privileges on bkndmvrgnzr.* to 'bkndmvrgnzr' identified by 'bkndmvrgnzr';`. Then build the JAR via `gradle clean bootJar` and then run it via `java -jar build/libs/bkndmvrgnzr-backend-1.0.jar`. If this is your first setup, you should add the `-i` option to initialize a new database. Alternatively, you can export your database from another instance via `mysqldump -u bkndmvrgnzr -p bkndmvrgnzr > bkndmvrgnzr-dump.sql` and import it to the new instance via `mysql -u bkndmvrgnzr -p bkndmvrgnzr < bkndmvrgnzr-dump.sql`.

## Data model

![Data model](data-model.svg)

## API

This project uses Swagger to programmatically generate an up-to-date API documentation, which can be visited at [`http://locahost:8080/docs/swagger-ui.html`](http://locahost:8080/docs/swagger-ui.html), but here is the rough API structure I designed during initial development.

### Auth

- [x] Login: POST /api/auth

### Book

User-agnostic:

- [x] Get all books: GET /api/book ROLE_USER
- [x] Create a book: POST /api/book ROLE_EDITOR
- [x] Get a specific book: GET /api/book/{isbn} ROLE_USER
- [x] Update a specific book: PUT /api/book/{isbn} ROLE_EDITOR
- [x] Delete a specific book: DELETE /api/book/{isbn} ROLE_EDITOR
- [x] Get all books of genre: GET /api/book/genre/{genreId} ROLE_USER
- [x] Get all books of publishing house: GET /api/book/publishing-house/{publishingHouseId} ROLE_USER
- [x] Get all books of book contributor: GET /api/book/book-contributor/{bookContributorId} ROLE_USER
- [x] Get all books of contributor: GET /api/book/contributor/{contributorId} ROLE_USER
- [x] Get all books of search query: GET /api/book/search/{query} ROLE_USER

User-specific:

- [x] Get all books of user: GET /api/book/user ROLE_USER
- [x] Add a book to user: POST /api/book/user/{isbn} ROLE_USER
- [x] Remove a book from user: DELETE /api/book/user/{isbn} ROLE_USER
- [x] Get all books of genre from user: GET /api/book/user/genre/{genreId} ROLE_USER
- [x] Get all books of publishing house from user: GET /api/book/user/publishing-house/{publishingHouseId} ROLE_USER
- [x] Get all books of book contributor from user: GET /api/book/user/book-contributor/{bookContributorId} ROLE_USER
- [x] Get all books of contributor from user: GET /api/book/user/contributor/{contributorId} ROLE_USER
- [x] Get all books of search query from user: GET /api/book/user/search/{query} ROLE_USER

### Book contributor

- [x] Get all book contributors: GET /api/book-contributor ROLE_USER
- [x] Create a book contributor: POST /api/book-contributor ROLE_EDITOR
- [x] Get a specific book contributor: GET /api/book-contributor/{bookContributorId} ROLE_USER
- [x] Update a specific book contributor: PUT /api/book-contributor/{bookContributorId} ROLE_EDITOR
- [x] Delete a specific book contributor: DELETE /api/book-contributor/{bookContributorId} ROLE_EDITOR
- [x] Get all book contributors of contributor: GET /api/book-contributor/contributor/{contributorId} ROLE_USER
- [x] Get all book contributors of book role: GET /api/book-contributor/book-role/{bookRoleId} ROLE_USER
- [x] Get all book contributors of search query: GET /api/book-contributor/search/{query} ROLE_USER

### Book import

- [x] Import book: GET /api/book-import/{isbn} ROLE_EDITOR

### Book role

- [x] Get all book roles: GET /api/book-role ROLE_USER
- [x] Create a book role: POST /api/book-role ROLE_EDITOR
- [x] Get a specific book role: GET /api/book-role/{bookRoleId} ROLE_USER
- [x] Update a specific book role: PUT /api/book-role/{bookRoleId} ROLE_EDITOR
- [x] Delete a specific book role: DELETE /api/book-role/{bookRoleId} ROLE_EDITOR
- [x] Get all book roles of contributor: GET /api/book-role/contributor/{contributorId} ROLE_USER
- [x] Get all book roles of search query: GET /api/book-role/search/{query} ROLE_USER

### Contributor

- [x] Get all contributors: GET /api/contributor ROLE_USER
- [x] Create a contributor: POST /api/contributor ROLE_EDITOR
- [x] Get a specific contributor: GET /api/contributor/{contributorId} ROLE_USER
- [x] Update a specific contributor: PUT /api/contributor/{contributorId} ROLE_EDITOR
- [x] Delete a specific contributor: DELETE /api/contributor/{contributorId} ROLE_EDITOR
- [x] Get all contributors of search query: GET /api/contributor/search/{query} ROLE_USER

### Genre

- [x] Get all genres: GET /api/genre ROLE_USER
- [x] Create a genre: POST /api/genre ROLE_EDITOR
- [x] Get a specific genre: GET /api/genre/{genreId} ROLE_USER
- [x] Update a specific genre: PUT /api/genre/{genreId} ROLE_EDITOR
- [x] Delete a specific genre: DELETE /api/genre/{genreId} ROLE_EDITOR
- [x] Get all genres of search query: GET /api/genre/search/{query} ROLE_USER

### Movie

User-agnostic:

- [x] Get all movies: GET /api/movie ROLE_USER
- [x] Create a movie: POST /api/movie ROLE_EDITOR
- [x] Get a specific movie: GET /api/movie/{isan} ROLE_USER
- [x] Update a specific movie: PUT /api/movie/{isan} ROLE_EDITOR
- [x] Delete a specific movie: DELETE /api/movie/{isan} ROLE_EDITOR
- [x] Get all movies of genre: GET /api/movie/genre/{genreId} ROLE_USER
- [x] Get all movies of movie contributor: GET /api/movie/movie-contributor/{movieContributorId} ROLE_USER
- [x] Get all movies of contributor: GET /api/movie/contributor/{contributorId} ROLE_USER
- [x] Get all movies of search query: GET /api/movie/search/{query} ROLE_USER

User-specific:

- [x] Get all movies of user: GET /api/movie/user ROLE_USER
- [x] Add a movie to user: POST /api/movie/user/{isan} ROLE_USER
- [x] Remove a movie from user: DELETE /api/movie/user/{isan} ROLE_USER
- [x] Get all movies of genre from user: GET /api/movie/user/genre/{genreId} ROLE_USER
- [x] Get all movies of movie contributor from user: GET /api/movie/user/movie-contributor/{movieContributorId} ROLE_USER
- [x] Get all movies of contributor from user: GET /api/movie/user/contributor/{contributorId} ROLE_USER
- [x] Get all books of search query from user: GET /api/movie/user/search/{query} ROLE_USER

### Movie contributor

- [x] Get all movie contributors: GET /api/movie-contributor ROLE_USER
- [x] Create a movie contributor: POST /api/movie-contributor ROLE_EDITOR
- [x] Get a specific movie contributor: GET /api/movie-contributor/{movieContributorId} ROLE_USER
- [x] Update a specific movie contributor: PUT /api/movie-contributor/{movieContributorId} ROLE_EDITOR
- [x] Delete a specific movie contributor: DELETE /api/movie-contributor/{movieContributorId} ROLE_EDITOR
- [x] Get all movie contributors of contributor: GET /api/movie-contributor/contributor/{contributorId} ROLE_USER
- [x] Get all movie contributors of movie role: GET /api/movie-contributor/movie-role/{movieRoleId} ROLE_USER
- [x] Get all movie contributors of search query: GET /api/movie-contributor/search/{query} ROLE_USER

### Movie import

- [x] Import movie: GET /api/movie-import/{isan} ROLE_EDITOR
- [x] Search movie: GET /api/movie-import/search/{query} ROLE_EDITOR

### Movie role

- [x] Get all movies roles: GET /api/movie-role ROLE_USER
- [x] Create a movies role: POST /api/movie-role ROLE_EDITOR
- [x] Get a specific movies role: GET /api/movie-role/{movieRoleId} ROLE_USER
- [x] Update a specific movies role: PUT /api/movie-role/{movieRoleId} ROLE_EDITOR
- [x] Delete a specific movies role: DELETE /api/movie-role/{movieRoleId} ROLE_EDITOR
- [x] Get all movie roles of contributor: GET /api/movie-role/contributor/{contributorId} ROLE_USER
- [x] Get all movie roles of search query: GET /api/movie-role/search/{query} ROLE_USER

### Publishing house

- [x] Get all publishing houses: GET /api/publishing-house ROLE_USER
- [x] Create a publishing house: POST /api/publishing-house ROLE_EDITOR
- [x] Get a specific publishing house: GET /api/publishing-house/{publishingHouseId} ROLE_USER
- [x] Update a specific publishing house: PUT /api/publishing-house/{publishingHouseId} ROLE_EDITOR
- [x] Delete a specific publishing house: DELETE /api/publishing-house/{publishingHouseId} ROLE_EDITOR
- [x] Get all publishing houses of search query: GET /api/publishing-house/search/{query} ROLE_USER

### Role

User-agnostic:

- [x] Get all roles: GET /api/role ROLE_USER
- [x] Get a specific role: GET /api/role/{roleId} ROLE_USER
- [x] Get all roles of search query: GET /api/role/search/{query} ROLE_USER

User-specific:

- [x] Get all roles of user: GET /api/role/user ROLE_USER
- [x] Get all roles of specific user: GET /api/role/user/{userId} ROLE_USER
- [x] Create a role to specific user: POST /api/role/user/{userId}/role/{roleId} ROLE_ADMIN
- [x] Remove a role from specific user: DELETE /api/role/user/{userId}/role/{roleId} ROLE_ADMIN

### User

- [x] Get user: GET /api/user ROLE_USER
- [x] Get all users: GET /api/user/all ROLE_ADMIN
- [x] Get a specific user: GET /api/user/{userId} ROLE_ADMIN
- [x] Create a user: POST /api/user
- [x] Update user: PUT /api/user ROLE_USER
- [x] Update a specific user: PUT /api/user/{userId} ROLE_ADMIN
- [x] Update password: PUT /api/user/password ROLE_USER
- [x] Update a specific user's password: PUT /api/user/{userId}/password ROLE_ADMIN
- [x] Delete user: DELETE /api/user ROLE_USER
- [x] Delete a specific user: DELETE /api/user/{userId} ROLE_ADMIN
- [x] Get all users of search query: GET /api/user/search/{query}  ROLE_ADMIN

## Data providers

### ISAN

- Search movie by query: https://web.isan.org/public/en/search?title={query}
- Find movie by ISAN: https://web.isan.org/public/en/isan/{isan}

Examples:
- User call: https://web.isan.org/public/en/search?title=Mortal%20engines
- Internal API call: `https://web.isan.org/public/en/search/unitary/data?draw=1&columns[0][data]=&columns[0][name]=&columns[0][searchable]=false&columns[0][orderable]=false&columns[0][search][value]=&columns[0][search][regex]=false&columns[1][data]=isan&columns[1][name]=isan&columns[1][searchable]=true&columns[1][orderable]=true&columns[1][search][value]=&columns[1][search][regex]=false&columns[2][data]=registrant_private_id&columns[2][name]=registrant_private_id&columns[2][searchable]=true&columns[2][orderable]=true&columns[2][search][value]=&columns[2][search][regex]=false&columns[3][data]=retrieve_title_txt&columns[3][name]=titles_ss.title_value_txt&columns[3][searchable]=true&columns[3][orderable]=false&columns[3][search][value]=Mortal%20engines&columns[3][search][regex]=false&columns[4][data]=main_group_map&columns[4][name]=main_group_map.group_reference_nbr_txt&columns[4][searchable]=true&columns[4][orderable]=false&columns[4][search][value]=&columns[4][search][regex]=false&columns[5][data]=main_group_map&columns[5][name]=main_group_map.episode_number_txt&columns[5][searchable]=true&columns[5][orderable]=false&columns[5][search][value]=&columns[5][search][regex]=false&columns[6][data]=year_of_ref_i&columns[6][name]=year_of_ref_i&columns[6][searchable]=true&columns[6][orderable]=true&columns[6][search][value]=&columns[6][search][regex]=false&columns[7][data]=duration&columns[7][name]=duration_value_f&columns[7][searchable]=true&columns[7][orderable]=false&columns[7][search][value]=&columns[7][search][regex]=false&columns[8][data]=retrieve_director_txt&columns[8][name]=participants_ss.participant_name_txt&columns[8][searchable]=true&columns[8][orderable]=false&columns[8][search][value]=&columns[8][search][regex]=false&columns[9][data]=sub_type_s&columns[9][name]=sub_type_s&columns[9][searchable]=true&columns[9][orderable]=true&columns[9][search][value]=&columns[9][search][regex]=false&columns[10][data]=participants_ss&columns[10][name]=participants_ss.role_s&columns[10][searchable]=true&columns[10][orderable]=false&columns[10][search][value]=&columns[10][search][regex]=false&columns[11][data]=titles_ss&columns[11][name]=titles_ss.title_value_txt&columns[11][searchable]=true&columns[11][orderable]=false&columns[11][search][value]=&columns[11][search][regex]=false&columns[12][data]=titles_ss&columns[12][name]=titles_ss.title_value_txt&columns[12][searchable]=true&columns[12][orderable]=false&columns[12][search][value]=&columns[12][search][regex]=false&columns[13][data]=countries_ss&columns[13][name]=countries_ss.country_code_s&columns[13][searchable]=true&columns[13][orderable]=false&columns[13][search][value]=&columns[13][search][regex]=false&columns[14][data]=languages_ss&columns[14][name]=languages_ss.language_code_s&columns[14][searchable]=true&columns[14][orderable]=false&columns[14][search][value]=&columns[14][search][regex]=false&columns[15][data]=participants_ss&columns[15][name]=participants_ss.participant_name_txt&columns[15][searchable]=true&columns[15][orderable]=false&columns[15][search][value]=&columns[15][search][regex]=false&columns[16][data]=descriptive_names_ss&columns[16][name]=descriptive_names_ss&columns[16][searchable]=true&columns[16][orderable]=false&columns[16][search][value]=&columns[16][search][regex]=false&columns[17][data]=properties_ss&columns[17][name]=properties_ss&columns[17][searchable]=true&columns[17][orderable]=false&columns[17][search][value]=&columns[17][search][regex]=false&columns[18][data]=distribution_intentions_ss&columns[18][name]=distribution_intentions_ss&columns[18][searchable]=true&columns[18][orderable]=true&columns[18][search][value]=&columns[18][search][regex]=false&columns[19][data]=media_fixation_s&columns[19][name]=media_fixation_s&columns[19][searchable]=true&columns[19][orderable]=true&columns[19][search][value]=&columns[19][search][regex]=false&columns[20][data]=parent_isan&columns[20][name]=parent_isan&columns[20][searchable]=true&columns[20][orderable]=false&columns[20][search][value]=&columns[20][search][regex]=false&columns[21][data]=parent_registrant_private_id&columns[21][name]=parent_registration_private_id&columns[21][searchable]=true&columns[21][orderable]=false&columns[21][search][value]=&columns[21][search][regex]=false&columns[22][data]=companies_ss&columns[22][name]=companies_ss.company_name_txt&columns[22][searchable]=true&columns[22][orderable]=false&columns[22][search][value]=&columns[22][search][regex]=false&columns[23][data]=companies_ss&columns[23][name]=companies_ss.company_kind_s&columns[23][searchable]=true&columns[23][orderable]=false&columns[23][search][value]=&columns[23][search][regex]=false&columns[24][data]=type_code_s&columns[24][name]=type_code_s&columns[24][searchable]=true&columns[24][orderable]=false&columns[24][search][value]=&columns[24][search][regex]=false&columns[25][data]=kind_code_s&columns[25][name]=kind_code_s&columns[25][searchable]=true&columns[25][orderable]=false&columns[25][search][value]=&columns[25][search][regex]=false&columns[26][data]=color_code_s&columns[26][name]=color_code_s&columns[26][searchable]=true&columns[26][orderable]=false&columns[26][search][value]=&columns[26][search][regex]=false&columns[27][data]=retrieve_director_txt&columns[27][name]=dirs_exact&columns[27][searchable]=true&columns[27][orderable]=false&columns[27][search][value]=&columns[27][search][regex]=false&columns[28][data]=year_min_search_value&columns[28][name]=year_of_ref_i_from&columns[28][searchable]=true&columns[28][orderable]=false&columns[28][search][value]=&columns[28][search][regex]=false&columns[29][data]=year_max_search_value&columns[29][name]=year_of_ref_i_to&columns[29][searchable]=true&columns[29][orderable]=false&columns[29][search][value]=&columns[29][search][regex]=false&columns[30][data]=year_gap_search_value&columns[30][name]=year_gap_search_value&columns[30][searchable]=true&columns[30][orderable]=false&columns[30][search][value]=&columns[30][search][regex]=false&columns[31][data]=created_dt&columns[31][name]=created_dt&columns[31][searchable]=true&columns[31][orderable]=false&columns[31][search][value]=&columns[31][search][regex]=false&columns[32][data]=last_updated_dt&columns[32][name]=last_updated_dt&columns[32][searchable]=true&columns[32][orderable]=false&columns[32][search][value]=&columns[32][search][regex]=false&columns[33][data]=score&columns[33][name]=score&columns[33][searchable]=false&columns[33][orderable]=false&columns[33][search][value]=&columns[33][search][regex]=false&order[0][column]=33&order[0][dir]=desc&start=0&length=25&search[value]=&search[regex]=false`
- User call: https://web.isan.org/public/en/isan/0000-0004-7BAA-0000-5-0000-0000-M
- User call: https://web.isan.org/public/en/isan/0000-0001-DB15-0000-X-0000-0000-C
- User call: https://web.isan.org/public/en/isan/0000-0006-D680-0000-P-0000-0000-0

### DNB

- Search book by query: https://portal.dnb.de/opac/simpleSearch?query={query}
- Find book by ISBN: https://portal.dnb.de/opac/simpleSearch?query={isbn}

Examples:
- User call: https://portal.dnb.de/opac.htm?method=simpleSearch&query=Factfulness
- User call: https://portal.dnb.de/opac/simpleSearch?query=978-3-548-06522-9
- User call: https://portal.dnb.de/opac/simpleSearch?query=978-3-442-47944-3
- User call: https://portal.dnb.de/opac/simpleSearch?query=978-3-596-29661-3
- User call: https://portal.dnb.de/opac/simpleSearch?query=978-3-492-28168-3
- User call: https://portal.dnb.de/opac/simpleSearch?query=978-3-8415-0134-9

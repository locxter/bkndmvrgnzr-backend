package com.github.locxter.bkndmvrgnzr.backend.bookimport.api

import com.github.locxter.bkndmvrgnzr.backend.book.db.Isbn
import it.skrape.core.htmlDocument
import it.skrape.fetcher.HttpFetcher
import it.skrape.fetcher.extractIt
import it.skrape.fetcher.skrape
import it.skrape.selects.html5.td
import it.skrape.selects.html5.tr
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/book-import")
class BookImportController {
    @GetMapping("{isbn}")
    @PreAuthorize("hasRole('EDITOR')")
    fun importBook(
        authentication: Authentication,
        @PathVariable(name = "isbn") isbn: String
    ): BookImportResponseDto {
        try {
            Isbn(isbn)
        } catch (exception: IllegalArgumentException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Sent book not valid")
        }
        return skrape(HttpFetcher) {
            request {
                url = "https://portal.dnb.de/opac/simpleSearch?query=" + isbn
            }

            extractIt<BookImportResponseDto> { result ->
                status {
                    result.httpStatus = code
                    result.httpMessage = message
                }
                result.isbn = isbn
                htmlDocument {
                    relaxed = true
                    val rows = "#fullRecordTable" {
                        tr {
                            findAll {
                                this
                            }
                        }
                    }
                    for (row in rows.drop(1)) {
                        row.td {
                            if (findFirst { text } == "Titel") {
                                findSecond {
                                    val fullTitle = text.substringBefore(" / ")
                                    val fullName =
                                        text.substringAfter(" / ").substringBefore(". ").substringBefore(", ")
                                            .substringBefore("; ")
                                    result.title = fullTitle.substringBefore(" : ").trim()
                                    result.subtitle = fullTitle.substringAfter(" : ", "").trim()
                                    result.authorFirstName = fullName.substringBefore(' ', "").trim()
                                    result.authorLastName = fullName.substringAfter(' ').trim()
                                }
                            } else if (findFirst { text } == "Zeitliche Einordnung") {
                                findSecond {
                                    result.year = text.substringAfterLast(' ').toIntOrNull() ?: 0
                                }
                            } else if (findFirst { text } == "Umfang/Format") {
                                findSecond {
                                    result.pages = text.substringBefore(' ').toIntOrNull() ?: 0
                                }
                            } else if (findFirst { text } == "Verlag") {
                                findSecond {
                                    result.publishingHouseName = text.substringAfter(" : ").trim()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

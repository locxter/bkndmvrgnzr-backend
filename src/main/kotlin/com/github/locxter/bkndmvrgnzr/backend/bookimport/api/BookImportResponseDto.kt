package com.github.locxter.bkndmvrgnzr.backend.bookimport.api

data class BookImportResponseDto(
    var httpStatus: Int = 200,
    var httpMessage: String = "OK",
    var isbn: String = "",
    var title: String = "",
    var subtitle: String = "",
    var year: Int = 0,
    var pages: Int = 0,
    var publishingHouseName: String = "",
    var authorFirstName: String = "",
    var authorLastName: String = ""
)

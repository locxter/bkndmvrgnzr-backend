package com.github.locxter.bkndmvrgnzr.backend.movieimport.api

data class MovieImportResponseDto(
    var httpStatus: Int = 200,
    var httpMessage: String = "OK",
    var isan: String = "",
    var title: String = "",
    var year: Int = 0,
    var playTime: Int = 0,
    var directorFirstName: String = "",
    var directorLastName: String = ""
)

package com.github.locxter.bkndmvrgnzr.backend.movieimport.api

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class MovieImportSearchResponseDto(
    @JsonAlias("status")
    var httpStatus: Int = 200,
    @JsonAlias("message")
    var httpMessage: String = "OK",
    @JsonAlias("data")
    var movies: List<MovieImportResponseBriefDto> = mutableListOf()
)

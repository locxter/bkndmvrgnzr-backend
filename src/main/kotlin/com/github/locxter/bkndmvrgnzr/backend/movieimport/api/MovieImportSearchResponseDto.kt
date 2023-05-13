package com.github.locxter.bkndmvrgnzr.backend.movieimport.api

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class MovieImportSearchResponseDto(
    @JsonAlias("status")
    val httpStatus: Int = 200,
    @JsonAlias("message")
    val httpMessage: String = "OK",
    @JsonAlias("data")
    val movies: List<MovieImportResponseBriefDto> = listOf()
)

package com.github.locxter.bkndmvrgnzr.backend.movieimport.api

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class MovieImportResponseBriefDto(
    @JsonAlias("isan")
    var isan: String = "",
    @JsonAlias("retrieve_title_txt")
    var title: String = "",
    @JsonAlias("year_of_ref_i")
    var year: Int = 0,
    @JsonAlias("retrieve_director_txt")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    var directorFullName: String = "",
    var directorFirstName: String = "",
    var directorLastName: String = "",
)

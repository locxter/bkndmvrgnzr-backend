package com.github.locxter.bkndmvrgnzr.backend.movieimport.api

import it.skrape.core.htmlDocument
import it.skrape.fetcher.HttpFetcher
import it.skrape.fetcher.extractIt
import it.skrape.fetcher.skrape
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate

@RestController
@RequestMapping("/api/movie-import")
class MovieImportController {
    @GetMapping("{isan}")
    @PreAuthorize("hasRole('EDITOR')")
    fun importMovie(
        authentication: Authentication,
        @PathVariable(name = "isan") isan: String
    ): MovieImportResponseDto {
        return skrape(HttpFetcher) {
            request {
                url = "https://web.isan.org/public/en/isan/" + isan
            }

            extractIt<MovieImportResponseDto> { result ->
                status {
                    result.httpStatus = code
                    result.httpMessage = message
                }
                result.isan = isan
                htmlDocument {
                    relaxed = true
                    "#registration_titles_0_title" {
                        findFirst {
                            result.title = text.trim()
                        }
                    }
                    "#registration_yearOfRef" {
                        findFirst {
                            result.year = text.toIntOrNull() ?: 0
                        }
                    }
                    "#registration_duration_durationValue" {
                        findFirst {
                            result.playTime = text.substringBefore(' ').toIntOrNull() ?: 0
                        }
                    }
                    "#registration_participants_0_name" {
                        findFirst {
                            result.directorFirstName = text.substringBefore(' ', "").trim()
                            result.directorLastName = text.substringAfter(' ').trim()
                        }
                    }
                }
            }
        }
    }

    @GetMapping("/search/{query}")
    @PreAuthorize("hasRole('EDITOR')")
    fun searchMovie(
        authentication: Authentication,
        @PathVariable(name = "query") query: String
    ): MovieImportSearchResponseDto {
        val url =
            "https://web.isan.org/public/en/search/unitary/data?draw=1&columns[0][data]=&columns[0][name]=&columns[0][searchable]=false&columns[0][orderable]=false&columns[0][search][value]=&columns[0][search][regex]=false&columns[1][data]=isan&columns[1][name]=isan&columns[1][searchable]=true&columns[1][orderable]=true&columns[1][search][value]=&columns[1][search][regex]=false&columns[2][data]=registrant_private_id&columns[2][name]=registrant_private_id&columns[2][searchable]=true&columns[2][orderable]=true&columns[2][search][value]=&columns[2][search][regex]=false&columns[3][data]=retrieve_title_txt&columns[3][name]=titles_ss.title_value_txt&columns[3][searchable]=true&columns[3][orderable]=false&columns[3][search][value]=" + query + "&columns[3][search][regex]=false&columns[4][data]=main_group_map&columns[4][name]=main_group_map.group_reference_nbr_txt&columns[4][searchable]=true&columns[4][orderable]=false&columns[4][search][value]=&columns[4][search][regex]=false&columns[5][data]=main_group_map&columns[5][name]=main_group_map.episode_number_txt&columns[5][searchable]=true&columns[5][orderable]=false&columns[5][search][value]=&columns[5][search][regex]=false&columns[6][data]=year_of_ref_i&columns[6][name]=year_of_ref_i&columns[6][searchable]=true&columns[6][orderable]=true&columns[6][search][value]=&columns[6][search][regex]=false&columns[7][data]=duration&columns[7][name]=duration_value_f&columns[7][searchable]=true&columns[7][orderable]=false&columns[7][search][value]=&columns[7][search][regex]=false&columns[8][data]=retrieve_director_txt&columns[8][name]=participants_ss.participant_name_txt&columns[8][searchable]=true&columns[8][orderable]=false&columns[8][search][value]=&columns[8][search][regex]=false&columns[9][data]=sub_type_s&columns[9][name]=sub_type_s&columns[9][searchable]=true&columns[9][orderable]=true&columns[9][search][value]=&columns[9][search][regex]=false&columns[10][data]=participants_ss&columns[10][name]=participants_ss.role_s&columns[10][searchable]=true&columns[10][orderable]=false&columns[10][search][value]=&columns[10][search][regex]=false&columns[11][data]=titles_ss&columns[11][name]=titles_ss.title_value_txt&columns[11][searchable]=true&columns[11][orderable]=false&columns[11][search][value]=&columns[11][search][regex]=false&columns[12][data]=titles_ss&columns[12][name]=titles_ss.title_value_txt&columns[12][searchable]=true&columns[12][orderable]=false&columns[12][search][value]=&columns[12][search][regex]=false&columns[13][data]=countries_ss&columns[13][name]=countries_ss.country_code_s&columns[13][searchable]=true&columns[13][orderable]=false&columns[13][search][value]=&columns[13][search][regex]=false&columns[14][data]=languages_ss&columns[14][name]=languages_ss.language_code_s&columns[14][searchable]=true&columns[14][orderable]=false&columns[14][search][value]=&columns[14][search][regex]=false&columns[15][data]=participants_ss&columns[15][name]=participants_ss.participant_name_txt&columns[15][searchable]=true&columns[15][orderable]=false&columns[15][search][value]=&columns[15][search][regex]=false&columns[16][data]=descriptive_names_ss&columns[16][name]=descriptive_names_ss&columns[16][searchable]=true&columns[16][orderable]=false&columns[16][search][value]=&columns[16][search][regex]=false&columns[17][data]=properties_ss&columns[17][name]=properties_ss&columns[17][searchable]=true&columns[17][orderable]=false&columns[17][search][value]=&columns[17][search][regex]=false&columns[18][data]=distribution_intentions_ss&columns[18][name]=distribution_intentions_ss&columns[18][searchable]=true&columns[18][orderable]=true&columns[18][search][value]=&columns[18][search][regex]=false&columns[19][data]=media_fixation_s&columns[19][name]=media_fixation_s&columns[19][searchable]=true&columns[19][orderable]=true&columns[19][search][value]=&columns[19][search][regex]=false&columns[20][data]=parent_isan&columns[20][name]=parent_isan&columns[20][searchable]=true&columns[20][orderable]=false&columns[20][search][value]=&columns[20][search][regex]=false&columns[21][data]=parent_registrant_private_id&columns[21][name]=parent_registration_private_id&columns[21][searchable]=true&columns[21][orderable]=false&columns[21][search][value]=&columns[21][search][regex]=false&columns[22][data]=companies_ss&columns[22][name]=companies_ss.company_name_txt&columns[22][searchable]=true&columns[22][orderable]=false&columns[22][search][value]=&columns[22][search][regex]=false&columns[23][data]=companies_ss&columns[23][name]=companies_ss.company_kind_s&columns[23][searchable]=true&columns[23][orderable]=false&columns[23][search][value]=&columns[23][search][regex]=false&columns[24][data]=type_code_s&columns[24][name]=type_code_s&columns[24][searchable]=true&columns[24][orderable]=false&columns[24][search][value]=&columns[24][search][regex]=false&columns[25][data]=kind_code_s&columns[25][name]=kind_code_s&columns[25][searchable]=true&columns[25][orderable]=false&columns[25][search][value]=&columns[25][search][regex]=false&columns[26][data]=color_code_s&columns[26][name]=color_code_s&columns[26][searchable]=true&columns[26][orderable]=false&columns[26][search][value]=&columns[26][search][regex]=false&columns[27][data]=retrieve_director_txt&columns[27][name]=dirs_exact&columns[27][searchable]=true&columns[27][orderable]=false&columns[27][search][value]=&columns[27][search][regex]=false&columns[28][data]=year_min_search_value&columns[28][name]=year_of_ref_i_from&columns[28][searchable]=true&columns[28][orderable]=false&columns[28][search][value]=&columns[28][search][regex]=false&columns[29][data]=year_max_search_value&columns[29][name]=year_of_ref_i_to&columns[29][searchable]=true&columns[29][orderable]=false&columns[29][search][value]=&columns[29][search][regex]=false&columns[30][data]=year_gap_search_value&columns[30][name]=year_gap_search_value&columns[30][searchable]=true&columns[30][orderable]=false&columns[30][search][value]=&columns[30][search][regex]=false&columns[31][data]=created_dt&columns[31][name]=created_dt&columns[31][searchable]=true&columns[31][orderable]=false&columns[31][search][value]=&columns[31][search][regex]=false&columns[32][data]=last_updated_dt&columns[32][name]=last_updated_dt&columns[32][searchable]=true&columns[32][orderable]=false&columns[32][search][value]=&columns[32][search][regex]=false&columns[33][data]=score&columns[33][name]=score&columns[33][searchable]=false&columns[33][orderable]=false&columns[33][search][value]=&columns[33][search][regex]=false&order[0][column]=33&order[0][dir]=desc&start=0&length=25&search[value]=&search[regex]=false"
        val restTemplate = RestTemplate()
        val movieImportSearchResponseDto = restTemplate.getForObject(url, MovieImportSearchResponseDto::class.java)
            ?: MovieImportSearchResponseDto()
        for (movie in movieImportSearchResponseDto.movies) {
            movie.title = movie.title.replace("<b>", "").replace("</b>", "")
            val fullName = movie.directorFullName.replace("Not Available", "")
            movie.directorFirstName = fullName.substringBefore(' ', "")
            movie.directorLastName = fullName.substringAfter(' ')
        }
        return movieImportSearchResponseDto
    }
}

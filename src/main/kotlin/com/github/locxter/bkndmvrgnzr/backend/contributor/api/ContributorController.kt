package com.github.locxter.bkndmvrgnzr.backend.contributor.api

import com.github.locxter.bkndmvrgnzr.backend.contributor.db.Contributor
import com.github.locxter.bkndmvrgnzr.backend.contributor.db.ContributorId
import com.github.locxter.bkndmvrgnzr.backend.contributor.db.ContributorRepository
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/contributor")
class ContributorController(private val contributorRepository: ContributorRepository) {
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    fun getAllContributors(): List<ContributorResponseDto> {
        val contributors = contributorRepository.findAll(Contributor.getSort())
        return contributors.map { it.toDto() }
    }

    @PostMapping
    @PreAuthorize("hasRole('EDITOR')")
    fun createContributor(@RequestBody contributorCreateDto: ContributorCreateDto): ContributorResponseDto {
        if (contributorCreateDto.firstName.isBlank() || contributorCreateDto.lastName.isBlank() ||
            contributorCreateDto.birthYear < 0 || contributorCreateDto.birthMonth < 0 ||
            contributorCreateDto.birthMonth > 12 || contributorCreateDto.birthDay < 0 ||
            contributorCreateDto.birthDay > 31
        ) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Sent contributor not valid")
        }
        val contributor = Contributor(
            firstName = contributorCreateDto.firstName,
            lastName = contributorCreateDto.lastName,
            birthYear = contributorCreateDto.birthYear,
            birthMonth = contributorCreateDto.birthMonth,
            birthDay = contributorCreateDto.birthDay
        )
        contributorRepository.save(contributor)
        return contributor.toDto()
    }

    @GetMapping("/{contributorId}")
    @PreAuthorize("hasRole('USER')")
    fun getContributor(@PathVariable(name = "contributorId") contributorId: String): ContributorResponseDto {
        val contributor = contributorRepository.findById(ContributorId(contributorId)).orElse(null)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Requested contributor not found")
        return contributor.toDto()
    }

    @PutMapping("/{contributorId}")
    @PreAuthorize("hasRole('EDITOR')")
    fun updateContributor(
        @PathVariable(name = "contributorId") contributorId: String,
        @RequestBody contributorUpdateDto: ContributorUpdateDto
    ): ContributorResponseDto {
        val contributor = contributorRepository.findById(ContributorId(contributorId)).orElse(null)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Requested contributor not found")
        if (contributorUpdateDto.birthYear < 0 || contributorUpdateDto.birthMonth < 0 || contributorUpdateDto.birthMonth > 12 || contributorUpdateDto.birthDay < 0 || contributorUpdateDto.birthDay > 31) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Sent book not valid")
        }
        val updatedContributor = contributor.copy(
            firstName = contributorUpdateDto.firstName.ifBlank { contributor.firstName },
            lastName = contributorUpdateDto.lastName.ifBlank { contributor.lastName },
            birthYear = if (contributorUpdateDto.birthYear != 0) contributorUpdateDto.birthYear else contributor.birthYear,
            birthMonth = if (contributorUpdateDto.birthMonth != 0) contributorUpdateDto.birthMonth else contributor.birthMonth,
            birthDay = if (contributorUpdateDto.birthDay != 0) contributorUpdateDto.birthDay else contributor.birthDay
        )
        contributorRepository.save(updatedContributor)
        return updatedContributor.toDto()
    }

    @DeleteMapping("/{contributorId}")
    @PreAuthorize("hasRole('EDITOR')")
    fun deleteContributor(@PathVariable(name = "contributorId") contributorId: String): ContributorResponseDto {
        val contributor = contributorRepository.findById(ContributorId(contributorId)).orElse(null)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Requested contributor not found")
        val contributorDto = contributor.toDto()
        contributorRepository.delete(contributor)
        return contributorDto
    }

    @GetMapping("/search/{query}")
    @PreAuthorize("hasRole('USER')")
    fun getAllContributorsOfSearchQuery(@PathVariable(name = "query") query: String): List<ContributorResponseDto> {
        val contributors = contributorRepository.findAll(Contributor.getSort())
        val iterator = contributors.iterator()
        while (iterator.hasNext()) {
            val contributor = iterator.next()
            var containsQuery = false
            if (contributor.firstName.contains(query, true) || contributor.lastName.contains(query, true) ||
                contributor.birthYear == query.toIntOrNull() || contributor.birthMonth == query.toIntOrNull() ||
                contributor.birthDay == query.toIntOrNull()
            ) {
                containsQuery = true
            } else {
                for (bookContributor in contributor.bookContributors) {
                    if (bookContributor.bookRole.name.contains(query, true)) {
                        containsQuery = true
                        break
                    }
                }
                for (movieContributor in contributor.movieContributors) {
                    if (movieContributor.movieRole.name.contains(query, true)) {
                        containsQuery = true
                        break
                    }
                }
            }
            if (!containsQuery) {
                iterator.remove()
            }
        }
        return contributors.map { it.toDto() }
    }
}

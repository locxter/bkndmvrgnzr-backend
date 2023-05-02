package com.github.locxter.bkndmvrgnzr.backend.publishinghouse.api

import com.github.locxter.bkndmvrgnzr.backend.publishinghouse.db.PublishingHouse
import com.github.locxter.bkndmvrgnzr.backend.publishinghouse.db.PublishingHouseId
import com.github.locxter.bkndmvrgnzr.backend.publishinghouse.db.PublishingHouseRepository
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/publishing-house")
class PublishingHouseController(private val publishingHouseRepository: PublishingHouseRepository) {
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    fun getAllPublishingHouses(): List<PublishingHouseResponseDto> {
        val publishingHouses = publishingHouseRepository.findAll(PublishingHouse.getSort())
        return publishingHouses.map { it.toDto() }
    }

    @PostMapping
    @PreAuthorize("hasRole('EDITOR')")
    fun createPublishingHouse(@RequestBody publishingHouseCreateDto: PublishingHouseCreateDto): PublishingHouseResponseDto {
        if (publishingHouseCreateDto.name.isBlank()) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Sent publishing house not valid")
        }
        val publishingHouse = PublishingHouse(
            name = publishingHouseCreateDto.name,
            country = publishingHouseCreateDto.country,
            city = publishingHouseCreateDto.city
        )
        publishingHouseRepository.save(publishingHouse)
        return publishingHouse.toDto()
    }

    @GetMapping("/{publishingHouseId}")
    @PreAuthorize("hasRole('USER')")
    fun getPublishingHouse(@PathVariable(name = "publishingHouseId") publishingHouseId: String): PublishingHouseResponseDto {
        val publishingHouse = publishingHouseRepository.findById(PublishingHouseId(publishingHouseId)).orElse(null)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Requested publishing house not found")
        return publishingHouse.toDto()
    }

    @PutMapping("/{publishingHouseId}")
    @PreAuthorize("hasRole('EDITOR')")
    fun updatePublishingHouse(
        @PathVariable(name = "publishingHouseId") publishingHouseId: String,
        @RequestBody publishingHouseUpdateDto: PublishingHouseUpdateDto
    ): PublishingHouseResponseDto {
        val publishingHouse = publishingHouseRepository.findById(PublishingHouseId(publishingHouseId)).orElse(null)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Requested publishing house not found")
        val updatedPublishingHouse = publishingHouse.copy(
            name = publishingHouseUpdateDto.name.ifBlank { publishingHouse.name },
            country = publishingHouseUpdateDto.country.ifBlank { publishingHouse.country },
            city = publishingHouseUpdateDto.city.ifBlank { publishingHouse.city },
        )
        publishingHouseRepository.save(updatedPublishingHouse)
        return updatedPublishingHouse.toDto()
    }

    @DeleteMapping("/{publishingHouseId}")
    @PreAuthorize("hasRole('EDITOR')")
    fun deletePublishingHouse(@PathVariable(name = "publishingHouseId") publishingHouseId: String): PublishingHouseResponseDto {
        val publishingHouse = publishingHouseRepository.findById(PublishingHouseId(publishingHouseId)).orElse(null)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Requested publishing house not found")
        val publishingHouseDto = publishingHouse.toDto()
        publishingHouseRepository.delete(publishingHouse)
        return publishingHouseDto
    }

    @GetMapping("/search/{query}")
    @PreAuthorize("hasRole('USER')")
    fun getAllPublishingHousesOfSearchQuery(@PathVariable(name = "query") query: String): List<PublishingHouseResponseDto> {
        val publishingHouses = publishingHouseRepository.findAll(PublishingHouse.getSort())
        val iterator = publishingHouses.iterator()
        while (iterator.hasNext()) {
            val publishingHouse = iterator.next()
            var containsQuery = false
            if (publishingHouse.name.contains(query, true) || publishingHouse.city.contains(query, true) ||
                publishingHouse.country.contains(query, true)
            ) {
                containsQuery = true
            } else {
                for (book in publishingHouse.books) {
                    if (book.title.contains(query, true) || book.subtitle.contains(query, true)) {
                        containsQuery = true
                        break
                    }
                }
            }
            if (!containsQuery) {
                iterator.remove()
            }
        }
        return publishingHouses.map { it.toDto() }
    }
}

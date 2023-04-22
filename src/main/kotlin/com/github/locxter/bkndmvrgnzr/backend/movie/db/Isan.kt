package com.github.locxter.bkndmvrgnzr.backend.movie.db

import jakarta.persistence.Embeddable
import jakarta.persistence.Transient
import java.io.Serializable

@Embeddable
data class Isan(
    val value: String
) : Serializable {
    @Transient
    val root: String

    @Transient
    val episode: String

    @Transient
    val firstChecksum: String

    @Transient
    val version: String

    @Transient
    val secondChecksum: String

    init {
        val parts = value.split('-')
        require(parts.size == 8) {
            "ISAN invalid: Requires 8 parts separated by hyphens"
        }
        require(parts[0].length + parts[1].length + parts[2].length == 12) {
            "ISAN invalid: Root needs 12 digits"
        }
        root = parts[0] + "-" + parts[2] + "-" + parts[3]
        require(parts[3].length == 4) {
            "ISAN invalid: Episode needs 4 digits"
        }
        episode = parts[3]
        require(parts[4].length == 1) {
            "ISAN invalid: First checksum needs 1 digit"
        }
        firstChecksum = parts[4]
        require(parts[5].length + parts[6].length == 8) {
            "ISAN invalid: Version need 8 digits"
        }
        version = parts[5] + "-" + parts[6]
        require(parts[7].length == 1) {
            "ISAN invalid: Second checksum needs 1 digit"
        }
        secondChecksum = parts[7]
    }
}

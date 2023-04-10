package com.github.locxter.bkndmvrgnzr.backend.book.db

import jakarta.persistence.Embeddable
import jakarta.persistence.Transient
import java.io.Serializable

@Embeddable
data class Isbn(
    val value: String
) : Serializable {
    @Transient
    val prefix: String
    @Transient
    val registrantGroup: String
    @Transient
    val registrant: String
    @Transient
    val publication: String
    @Transient
    val checksum: String

    init {
        val parts = value.split('-')
        require(parts.size == 5) {
            "ISBN invalid: Requires 5 parts separated by hyphens"
        }
        require(parts[0].length == 3) {
            "ISBN invalid: Prefix needs 3 digits"
        }
        prefix = parts[0]
        require(parts[1].length + parts[2].length + parts[3].length == 9) {
            "ISBN invalid: Registrant group + registrant + publication needs 9 digits"
        }
        registrantGroup = parts[1]
        registrant = parts[2]
        publication = parts[3]
        require(parts[4].length == 1) {
            "ISBN invalid: Checksum needs 1 digit"
        }
        checksum = parts[4]
    }
}

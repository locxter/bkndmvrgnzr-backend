package com.github.locxter.bkndmvrgnzr.backend.role.db

import com.github.locxter.bkndmvrgnzr.backend.role.api.RoleResponseDto
import com.github.locxter.bkndmvrgnzr.backend.user.db.User
import jakarta.persistence.*

@Entity
data class Role(
    @Id
    @AttributeOverride(name = "value", column = Column(name = "id"))
    val id: RoleId = RoleId(),
    @Enumerated(EnumType.STRING)
    @Column(unique = true)
    val type: ERole = ERole.ROLE_USER,
    @ManyToMany
    @JoinTable(
        name = "user_role",
        joinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")]
    )
    val users: List<User> = ArrayList()
) {
    fun toDto(): RoleResponseDto = RoleResponseDto(
        id.value,
        type
    )
}

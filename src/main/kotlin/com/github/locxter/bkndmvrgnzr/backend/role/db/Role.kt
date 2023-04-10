package com.github.locxter.bkndmvrgnzr.backend.role.db

import com.github.locxter.bkndmvrgnzr.backend.role.api.RoleResponseDto
import com.github.locxter.bkndmvrgnzr.backend.user.db.User
import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction

@Entity
data class Role(
    @Id
    @AttributeOverride(name = "value", column = Column(name = "id"))
    val id: RoleId = RoleId(),
    @Enumerated(EnumType.STRING)
    @Column(unique = true)
    val type: ERole = ERole.ROLE_USER,
    @ManyToMany(mappedBy = "roles")
    @OnDelete(action = OnDeleteAction.CASCADE)
    val users: List<User> = ArrayList()
) {
    fun toDto(): RoleResponseDto = RoleResponseDto(
        id.value,
        type.name
    )
}

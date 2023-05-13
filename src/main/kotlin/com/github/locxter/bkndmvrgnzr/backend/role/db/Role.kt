package com.github.locxter.bkndmvrgnzr.backend.role.db

import com.github.locxter.bkndmvrgnzr.backend.role.api.RoleResponseDto
import com.github.locxter.bkndmvrgnzr.backend.user.db.User
import jakarta.persistence.*
import org.springframework.data.domain.Sort

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
        name = "user_x_role",
        joinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")]
    )
    val users: List<User> = mutableListOf()
) {
    fun toDto(): RoleResponseDto = RoleResponseDto(
        id.value,
        type.name
    )

    companion object : Comparator<Role> {
        override fun compare(o1: Role, o2: Role): Int {
            val s1 = o1.type.name
            val s2 = o1.type.name
            return s1.compareTo(s2)
        }

        fun getSort(): Sort {
            return Sort.by(
                Sort.Direction.ASC,
                "type",
            )
        }
    }
}

package com.github.locxter.bkndmvrgnzr.backend.role.db

import com.github.locxter.bkndmvrgnzr.backend.user.db.UserId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RoleRepository : JpaRepository<Role, RoleId> {
    fun findByType(type: ERole): Role?
    fun findByUsersId(userId: UserId): List<Role>
}
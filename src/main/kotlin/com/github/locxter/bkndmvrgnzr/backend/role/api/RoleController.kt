package com.github.locxter.bkndmvrgnzr.backend.role.api

import com.github.locxter.bkndmvrgnzr.backend.role.db.RoleId
import com.github.locxter.bkndmvrgnzr.backend.role.db.RoleRepository
import com.github.locxter.bkndmvrgnzr.backend.user.db.UserId
import com.github.locxter.bkndmvrgnzr.backend.user.db.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/role")
class RoleController(private val roleRepository: RoleRepository, private val userRepository: UserRepository) {
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    fun getAllRoles(): List<RoleResponseDto> {
        val roles = roleRepository.findAll()
        return roles.map { it.toDto() }
    }

    @GetMapping("/{role-id}")
    @PreAuthorize("hasRole('USER')")
    fun getRole(@PathVariable(name = "role-id") roleId: String): RoleResponseDto {
        val role = roleRepository.findById(RoleId(roleId)).orElse(null)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Requested role not found")
        return role.toDto()
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    fun getAllRolesOfUser(authentication: Authentication): List<RoleResponseDto> {
        val user = userRepository.findByUsername(authentication.name) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Requested user not found"
        )
        val roles = roleRepository.findByUsersId(user.id)
        return roles.map { it.toDto() }
    }

    @GetMapping("/user/{user-id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun getAllRolesOfSpecificUser(
        authentication: Authentication,
        @PathVariable(name = "user-id") userId: String
    ): List<RoleResponseDto> {
        val user = userRepository.findById(UserId(userId)).orElse(null)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Requested user not found")
        val roles = roleRepository.findByUsersId(user.id)
        return roles.map { it.toDto() }
    }

    @PostMapping("/user/{user-id}/role/{role-id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun addRoleToSpecificUser(
        authentication: Authentication,
        @PathVariable(name = "user-id") userId: String,
        @PathVariable(name = "role-id") roleId: String
    ): List<RoleResponseDto> {
        val user = userRepository.findById(UserId(userId)).orElse(null)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Requested user not found")
        val role = roleRepository.findById(RoleId(roleId)).orElse(null)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Requested role not found")
        val userRoles = user.roles.toMutableList()
        if (!userRoles.contains(role)) {
            userRoles.add(role)
            val updatedUser = user.copy(roles = userRoles)
            userRepository.save(updatedUser)
        }
        return userRoles.map { it.toDto() }
    }

    @DeleteMapping("/user/{user-id}/role/{role-id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun removeRoleFromSpecificUser(
        authentication: Authentication,
        @PathVariable(name = "user-id") userId: String,
        @PathVariable(name = "role-id") roleId: String
    ): List<RoleResponseDto> {
        val user = userRepository.findById(UserId(userId)).orElse(null)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Requested user not found")
        val role = roleRepository.findById(RoleId(roleId)).orElse(null)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Requested role not found")
        val userRoles = user.roles.toMutableList()
        if (userRoles.contains(role)) {
            userRoles.remove(role)
            val updatedUser = user.copy(roles = userRoles)
            userRepository.save(updatedUser)
        }
        return userRoles.map { it.toDto() }
    }
}
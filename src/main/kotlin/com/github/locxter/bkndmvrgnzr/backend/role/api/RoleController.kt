package com.github.locxter.bkndmvrgnzr.backend.role.api

import com.github.locxter.bkndmvrgnzr.backend.role.db.Role
import com.github.locxter.bkndmvrgnzr.backend.role.db.RoleId
import com.github.locxter.bkndmvrgnzr.backend.role.db.RoleRepository
import com.github.locxter.bkndmvrgnzr.backend.user.db.UserId
import com.github.locxter.bkndmvrgnzr.backend.user.db.UserRepository
import me.xdrop.fuzzywuzzy.FuzzySearch
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
        val roles = roleRepository.findAll(Role.getSort())
        return roles.map { it.toDto() }
    }

    @GetMapping("/{roleId}")
    @PreAuthorize("hasRole('USER')")
    fun getRole(@PathVariable(name = "roleId") roleId: String): RoleResponseDto {
        val role = roleRepository.findById(RoleId(roleId)).orElse(null)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Requested role not found")
        return role.toDto()
    }

    @GetMapping("/search/{query}")
    @PreAuthorize("hasRole('USER')")
    fun getAllRolesOfSearchQuery(@PathVariable(name = "query") query: String): List<RoleResponseDto> {
        val roles = roleRepository.findAll(Role.getSort())
        val iterator = roles.iterator()
        while (iterator.hasNext()) {
            val role = iterator.next()
            var containsQuery = false
            if (FuzzySearch.weightedRatio(role.type.name, query) >= 90) {
                containsQuery = true
            } else {
                for (user in role.users) {
                    if (FuzzySearch.weightedRatio(user.username, query) >= 90 ||
                        FuzzySearch.weightedRatio(user.firstName, query) >= 90 ||
                        FuzzySearch.weightedRatio(user.lastName, query) >= 90 ||
                        FuzzySearch.weightedRatio(user.firstName + ' ' + user.lastName, query) >= 90
                    ) {
                        containsQuery = true
                        break
                    }
                }
            }
            if (!containsQuery) {
                iterator.remove()
            }
        }
        return roles.map { it.toDto() }
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    fun getAllRolesOfUser(authentication: Authentication): List<RoleResponseDto> {
        val user = userRepository.findByUsername(authentication.name) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Requested user not found"
        )
        val roles = roleRepository.findByUsersId(user.id, Role.getSort())
        return roles.map { it.toDto() }
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    fun getAllRolesOfSpecificUser(
        authentication: Authentication,
        @PathVariable(name = "userId") userId: String
    ): List<RoleResponseDto> {
        val user = userRepository.findById(UserId(userId)).orElse(null)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Requested user not found")
        val roles = roleRepository.findByUsersId(user.id, Role.getSort())
        return roles.map { it.toDto() }
    }

    @PostMapping("/user/{userId}/role/{roleId}")
    @PreAuthorize("hasRole('ADMIN')")
    fun addRoleToSpecificUser(
        authentication: Authentication,
        @PathVariable(name = "userId") userId: String,
        @PathVariable(name = "roleId") roleId: String
    ): List<RoleResponseDto> {
        val user = userRepository.findById(UserId(userId)).orElse(null)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Requested user not found")
        val role = roleRepository.findById(RoleId(roleId)).orElse(null)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Requested role not found")
        val userRoles = user.roles.sortedWith(Role).toMutableList()
        if (!userRoles.contains(role)) {
            userRoles.add(role)
            userRoles.sortWith(Role)
            val updatedUser = user.copy(roles = userRoles)
            userRepository.save(updatedUser)
        }
        return userRoles.map { it.toDto() }
    }

    @DeleteMapping("/user/{userId}/role/{roleId}")
    @PreAuthorize("hasRole('ADMIN')")
    fun removeRoleFromSpecificUser(
        authentication: Authentication,
        @PathVariable(name = "userId") userId: String,
        @PathVariable(name = "roleId") roleId: String
    ): List<RoleResponseDto> {
        val user = userRepository.findById(UserId(userId)).orElse(null)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Requested user not found")
        val role = roleRepository.findById(RoleId(roleId)).orElse(null)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Requested role not found")
        val userRoles = user.roles.sortedWith(Role).toMutableList()
        if (userRoles.contains(role)) {
            userRoles.remove(role)
            val updatedUser = user.copy(roles = userRoles)
            userRepository.save(updatedUser)
        }
        return userRoles.map { it.toDto() }
    }
}

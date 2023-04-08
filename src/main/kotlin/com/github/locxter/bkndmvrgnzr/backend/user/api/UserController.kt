package com.github.locxter.bkndmvrgnzr.backend.user.api

import com.github.locxter.bkndmvrgnzr.backend.role.db.ERole
import com.github.locxter.bkndmvrgnzr.backend.role.db.RoleRepository
import com.github.locxter.bkndmvrgnzr.backend.user.db.Password
import com.github.locxter.bkndmvrgnzr.backend.user.db.User
import com.github.locxter.bkndmvrgnzr.backend.user.db.UserId
import com.github.locxter.bkndmvrgnzr.backend.user.db.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/user")
class UserController(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository
) {
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    fun getUser(authentication: Authentication): UserResponseDto {
        val user = userRepository.findByUsername(authentication.name) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Requested user not found"
        )
        return user.toDto()
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    fun getAllUsers(authentication: Authentication): List<UserResponseDto> {
        val users = userRepository.findAll()
        return users.map { it.toDto() }
    }

    @GetMapping("/{user-id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun getSpecificUser(
        authentication: Authentication,
        @PathVariable(name = "user-id") userId: String
    ): UserResponseDto {
        val user = userRepository.findById(UserId(userId)).orElse(null)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Requested user not found")
        return user.toDto()
    }

    @PostMapping
    fun createUser(@RequestBody userCreateDto: UserCreateDto): UserResponseDto {
        if (userCreateDto.username.length < 4 || userCreateDto.username.length > 32 ||
            userRepository.existsByUsername(userCreateDto.username) || userCreateDto.password.length < 8 ||
            userCreateDto.password.length > 64
        ) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Sent user not valid")
        }
        val user = User(
            username = userCreateDto.username,
            password = Password(userCreateDto.password),
            firstName = userCreateDto.firstName,
            lastName = userCreateDto.lastName,
            roles = arrayListOf(roleRepository.findByType(ERole.ROLE_USER) ?: throw Exception("ROlE_USER not found"))
        )
        userRepository.save(user)
        return user.toDto()
    }

    @PutMapping
    @PreAuthorize("hasRole('USER')")
    fun updateUser(
        authentication: Authentication,
        @RequestBody userUpdateDto: UserUpdateDto
    ): UserResponseDto {
        val user = userRepository.findByUsername(authentication.name) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Requested user not found"
        )
        if ((userUpdateDto.username.isNotBlank() && (userUpdateDto.username.length < 4 || userUpdateDto.username.length > 32 ||
                    userRepository.existsByUsername(userUpdateDto.username))) ||
            (userUpdateDto.password.isNotEmpty() && (userUpdateDto.password.length < 8 || userUpdateDto.password.length > 64))
        ) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Sent user not valid")
        }
        val updatedUser = user.copy(
            username = userUpdateDto.username.ifBlank { user.username },
            password = if (userUpdateDto.password.isNotBlank()) Password(userUpdateDto.password) else user.password,
            firstName = userUpdateDto.firstName.ifBlank { user.firstName },
            lastName = userUpdateDto.lastName.ifBlank { user.lastName },
        )
        userRepository.save(updatedUser)
        return updatedUser.toDto()
    }

    @PutMapping("/{user-id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun updateSpecificUser(
        authentication: Authentication,
        @PathVariable(name = "user-id") userId: String,
        @RequestBody userUpdateDto: UserUpdateDto
    ): UserResponseDto {
        val user = userRepository.findById(UserId(userId)).orElse(null) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Requested user not found"
        )
        if ((userUpdateDto.username.isNotBlank() && (userUpdateDto.username.length < 4 || userUpdateDto.username.length > 32 ||
                    userRepository.existsByUsername(userUpdateDto.username))) ||
            (userUpdateDto.password.isNotEmpty() && (userUpdateDto.password.length < 8 || userUpdateDto.password.length > 64))
        ) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Sent user not valid")
        }
        val updatedUser = user.copy(
            username = userUpdateDto.username.ifBlank { user.username },
            password = if (userUpdateDto.password.isNotBlank()) Password(userUpdateDto.password) else user.password,
            firstName = userUpdateDto.firstName.ifBlank { user.firstName },
            lastName = userUpdateDto.lastName.ifBlank { user.lastName },
        )
        userRepository.save(updatedUser)
        return updatedUser.toDto()
    }

    @DeleteMapping
    @PreAuthorize("hasRole('USER')")
    fun deleteUser(
        authentication: Authentication,
        @RequestBody userDeleteDto: UserDeleteDto
    ): UserResponseDto {
        val user = userRepository.findByUsername(authentication.name) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Requested user not found"
        )
        if (userDeleteDto.username != user.username || userDeleteDto.password != userDeleteDto.confirmPassword ||
            !user.password.matches(userDeleteDto.password)
        ) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Sent user deletion not valid")
        }
        val userDto = user.toDto()
        userRepository.delete(user)
        return userDto
    }

    @DeleteMapping("/{user-id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun deleteSpecificUser(
        authentication: Authentication,
        @PathVariable(name = "user-id") userId: String,
        @RequestBody userDeleteDto: UserDeleteDto
    ): UserResponseDto {
        val user = userRepository.findById(UserId(userId)).orElse(null)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Requested user not found")
        if (userDeleteDto.username != user.username || userDeleteDto.password != userDeleteDto.confirmPassword || !user.password.matches(
                userDeleteDto.password
            )
        ) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Sent user deletion not valid")
        }
        val userDto = user.toDto()
        userRepository.delete(user)
        return userDto
    }
}
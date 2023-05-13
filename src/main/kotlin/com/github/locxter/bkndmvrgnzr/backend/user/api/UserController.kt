package com.github.locxter.bkndmvrgnzr.backend.user.api

import com.github.locxter.bkndmvrgnzr.backend.role.db.ERole
import com.github.locxter.bkndmvrgnzr.backend.role.db.RoleRepository
import com.github.locxter.bkndmvrgnzr.backend.user.db.Password
import com.github.locxter.bkndmvrgnzr.backend.user.db.User
import com.github.locxter.bkndmvrgnzr.backend.user.db.UserId
import com.github.locxter.bkndmvrgnzr.backend.user.db.UserRepository
import me.xdrop.fuzzywuzzy.FuzzySearch
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
        val users = userRepository.findAll(User.getSort())
        return users.map { it.toDto() }
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    fun getSpecificUser(
        authentication: Authentication,
        @PathVariable(name = "userId") userId: String
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
            roles = listOf(roleRepository.findByType(ERole.ROLE_USER) ?: throw Exception("ROlE_USER not found"))
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
        if ((userUpdateDto.username.isNotBlank() && userUpdateDto.username != user.username &&
                    (userUpdateDto.username.length < 4 || userUpdateDto.username.length > 32 ||
                            userRepository.existsByUsername(userUpdateDto.username)))
        ) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Sent user not valid")
        }
        val updatedUser = user.copy(
            username = userUpdateDto.username.ifBlank { user.username },
            firstName = userUpdateDto.firstName.ifBlank { user.firstName },
            lastName = userUpdateDto.lastName.ifBlank { user.lastName },
        )
        userRepository.save(updatedUser)
        return updatedUser.toDto()
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    fun updateSpecificUser(
        authentication: Authentication,
        @PathVariable(name = "userId") userId: String,
        @RequestBody userUpdateDto: UserUpdateDto
    ): UserResponseDto {
        val user = userRepository.findById(UserId(userId)).orElse(null) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Requested user not found"
        )
        if ((userUpdateDto.username.isNotBlank() && userUpdateDto.username != user.username &&
                    (userUpdateDto.username.length < 4 || userUpdateDto.username.length > 32 ||
                            userRepository.existsByUsername(userUpdateDto.username)))
        ) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Sent user not valid")
        }
        val updatedUser = user.copy(
            username = userUpdateDto.username.ifBlank { user.username },
            firstName = userUpdateDto.firstName.ifBlank { user.firstName },
            lastName = userUpdateDto.lastName.ifBlank { user.lastName },
        )
        userRepository.save(updatedUser)
        return updatedUser.toDto()
    }

    @PutMapping("/password")
    @PreAuthorize("hasRole('USER')")
    fun updatePassword(
        authentication: Authentication,
        @RequestBody passwordUpdateDto: PasswordUpdateDto
    ): UserResponseDto {
        val user = userRepository.findByUsername(authentication.name) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Requested user not found"
        )
        if (!user.password.matches(passwordUpdateDto.password) ||
            (passwordUpdateDto.newPassword.isNotEmpty() && (passwordUpdateDto.newPassword.length < 8 ||
                    passwordUpdateDto.newPassword.length > 64)) ||
            passwordUpdateDto.newPassword != passwordUpdateDto.confirmNewPassword
        ) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Sent password update not valid")
        }
        val updatedUser = user.copy(
            password = Password(passwordUpdateDto.newPassword),
        )
        userRepository.save(updatedUser)
        return updatedUser.toDto()
    }

    @PutMapping("/{userId}/password")
    @PreAuthorize("hasRole('ADMIN')")
    fun updateSpecificUsersPassword(
        authentication: Authentication,
        @PathVariable(name = "userId") userId: String,
        @RequestBody passwordUpdateAdminDto: PasswordUpdateAdminDto
    ): UserResponseDto {
        val user = userRepository.findById(UserId(userId)).orElse(null)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Requested user not found")
        if ((passwordUpdateAdminDto.newPassword.isNotEmpty() && (passwordUpdateAdminDto.newPassword.length < 8 ||
                    passwordUpdateAdminDto.newPassword.length > 64)) ||
            passwordUpdateAdminDto.newPassword != passwordUpdateAdminDto.confirmNewPassword
        ) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Sent password update not valid")
        }
        val updatedUser = user.copy(
            password = Password(passwordUpdateAdminDto.newPassword),
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

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    fun deleteSpecificUser(
        authentication: Authentication,
        @PathVariable(name = "userId") userId: String,
        @RequestBody userDeleteAdminDto: UserDeleteAdminDto
    ): UserResponseDto {
        val user = userRepository.findById(UserId(userId)).orElse(null)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Requested user not found")
        if (userDeleteAdminDto.username != user.username) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Sent user deletion not valid")
        }
        val userDto = user.toDto()
        userRepository.delete(user)
        return userDto
    }

    @GetMapping("/search/{query}")
    @PreAuthorize("hasRole('USER')")
    fun getAllUsersOfSearchQuery(@PathVariable(name = "query") query: String): List<UserResponseDto> {
        val users = userRepository.findAll(User.getSort())
        val iterator = users.iterator()
        while (iterator.hasNext()) {
            val user = iterator.next()
            var containsQuery = false
            if (FuzzySearch.weightedRatio(user.username, query) >= 90 ||
                FuzzySearch.weightedRatio(user.firstName, query) >= 90 ||
                FuzzySearch.weightedRatio(user.lastName, query) >= 90 ||
                FuzzySearch.weightedRatio(user.firstName + ' ' + user.lastName, query) >= 90
            ) {
                containsQuery = true
            } else {
                for (book in user.books) {
                    if (FuzzySearch.weightedRatio(book.title, query) >= 90 ||
                        FuzzySearch.weightedRatio(book.subtitle, query) >= 90
                    ) {
                        containsQuery = true
                        break
                    }
                }
                for (movie in user.movies) {
                    if (FuzzySearch.weightedRatio(movie.title, query) >= 90) {
                        containsQuery = true
                        break
                    }
                }
            }
            if (!containsQuery) {
                iterator.remove()
            }
        }
        return users.map { it.toDto() }
    }
}

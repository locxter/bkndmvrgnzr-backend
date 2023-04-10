package com.github.locxter.bkndmvrgnzr.backend.auth.api

import com.github.locxter.bkndmvrgnzr.backend.auth.UserDetailsImpl
import com.github.locxter.bkndmvrgnzr.backend.lib.JwtUtils
import com.github.locxter.bkndmvrgnzr.backend.user.db.UserRepository
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val userRepository: UserRepository,
    private val jwtUtils: JwtUtils
) {
    @PostMapping
    fun login(@RequestBody authLoginDto: AuthLoginDto): ResponseEntity<*> {
        if (authLoginDto.username.length < 4 || authLoginDto.username.length > 32 ||
            !userRepository.existsByUsername(authLoginDto.username) || authLoginDto.password.length < 8 ||
            authLoginDto.password.length > 64
        ) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Sent login not valid")
        }
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                authLoginDto.username,
                authLoginDto.password
            )
        )
        SecurityContextHolder.getContext().authentication = authentication
        val userDetails = authentication?.principal as UserDetailsImpl
        val jwtCookie = jwtUtils.createJwtCookie(userDetails)
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString()).body(jwtCookie.toString())
    }

    @DeleteMapping
    fun logout(): ResponseEntity<*> {
        val cookie = jwtUtils.createCleanCookie()
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(cookie.toString())
    }
}

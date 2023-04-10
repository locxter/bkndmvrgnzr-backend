package com.github.locxter.bkndmvrgnzr.backend.auth

import com.github.locxter.bkndmvrgnzr.backend.lib.JwtUtils
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter

class AuthFilter(
    private val jwtUtils: JwtUtils,
    private val userDetailsService: UserDetailsServiceImpl
) : OncePerRequestFilter() {
    private val logger = LoggerFactory.getLogger(AuthFilter::class.java)

    @Throws(Exception::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val jwt = jwtUtils.getJwtFromRequest(request)
            if (jwt != null && jwtUtils.validateJwt(jwt)) {
                val username = jwtUtils.getUsernameFromJwt(jwt)
                val userDetails = userDetailsService.loadUserByUsername(username)
                val authentication = UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.authorities
                )
                authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authentication
            }
        } catch (exception: Exception) {
            logger.error("Cannot set user authentication: {}", exception)
        }
        filterChain.doFilter(request, response)
    }
}

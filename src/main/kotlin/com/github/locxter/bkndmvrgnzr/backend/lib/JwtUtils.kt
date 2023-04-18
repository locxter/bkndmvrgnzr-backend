package com.github.locxter.bkndmvrgnzr.backend.lib

import com.github.locxter.bkndmvrgnzr.backend.auth.UserDetailsImpl
import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SecurityException
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseCookie
import org.springframework.stereotype.Component
import org.springframework.web.util.WebUtils
import java.security.Key
import java.util.*


@Component
class JwtUtils {
    private val jwtSecret: String =
        "EjRe2sffUh5F9EMqTlfK2ops6zpuOPam7Y1gawJ3EGvOK9TozoJcpXuoken+FysaDebyQv9oR8ZJ0mZBxlYWsA=="
    private val jwtExpirationSeconds: Long = 28800
    private val jwtKey: Key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret))
    private val logger = LoggerFactory.getLogger(JwtUtils::class.java)

    fun createJwtFromUsername(username: String): String {
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(Date())
            .setExpiration(Date(Date().time + (jwtExpirationSeconds * 1000)))
            .signWith(jwtKey, SignatureAlgorithm.HS512)
            .compact()
    }

    fun getJwtFromRequest(request: HttpServletRequest): String? {
        return request.getHeader("Authorization")?.substringAfter("Bearer ")
    }

    fun validateJwt(jwt: String?): Boolean {
        try {
            Jwts.parserBuilder().setSigningKey(jwtKey).build().parseClaimsJws(jwt)
            return true
        } catch (e: SecurityException) {
            logger.error("Invalid JWT signature: {}", e.message)
        } catch (e: MalformedJwtException) {
            logger.error("Invalid JWT token: {}", e.message)
        } catch (e: ExpiredJwtException) {
            logger.error("JWT token is expired: {}", e.message)
        } catch (e: UnsupportedJwtException) {
            logger.error("JWT token is unsupported: {}", e.message)
        } catch (e: IllegalArgumentException) {
            logger.error("JWT claims string is empty: {}", e.message)
        }
        return false
    }

    fun getUsernameFromJwt(jwt: String?): String {
        return Jwts.parserBuilder().setSigningKey(jwtKey).build().parseClaimsJws(jwt).body.subject
    }
}

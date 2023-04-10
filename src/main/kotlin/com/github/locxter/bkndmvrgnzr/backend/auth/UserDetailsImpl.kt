package com.github.locxter.bkndmvrgnzr.backend.auth

import com.github.locxter.bkndmvrgnzr.backend.role.db.Role
import com.github.locxter.bkndmvrgnzr.backend.user.db.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.stream.Collectors

class UserDetailsImpl(
    private val username: String,
    private val password: String,
    private val authorities: Collection<GrantedAuthority>
) : UserDetails {
    constructor(user: User) : this(
        user.username,
        user.password.value,
        user.roles.stream().map { role: Role -> SimpleGrantedAuthority(role.type.name) }.collect(Collectors.toList())
    )

    override fun getAuthorities(): Collection<GrantedAuthority> = authorities

    override fun getPassword(): String = password

    override fun getUsername(): String = username

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}

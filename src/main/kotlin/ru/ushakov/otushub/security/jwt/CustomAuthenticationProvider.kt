package ru.ushakov.otushub.security.jwt

import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component
import ru.ushakov.otushub.exception.InvalidCredentialsException
import ru.ushakov.otushub.exception.UserNotFoundException
import ru.ushakov.otushub.user.controller.USER_NOT_FOUND_MESSAGE
import ru.ushakov.otushub.user.repository.UserRepository
import java.util.*

@Component
class CustomAuthenticationProvider(
    private val userRepository: UserRepository,
    private val passwordEncoder: BCryptPasswordEncoder
) : AuthenticationProvider {

    override fun authenticate(authentication: Authentication): Authentication {
        val userId = authentication.name
        val password = authentication.credentials.toString()

        val user = userRepository.findByUserId(UUID.fromString(userId))
            ?: throw UserNotFoundException(USER_NOT_FOUND_MESSAGE)

        if (!passwordEncoder.matches(password, user.password)) {
            throw InvalidCredentialsException("Невалидные данные")
        }

        return UsernamePasswordAuthenticationToken(user, password, user.roles.map { SimpleGrantedAuthority(it) })
    }

    override fun supports(authentication: Class<*>): Boolean {
        return authentication == UsernamePasswordAuthenticationToken::class.java
    }
}
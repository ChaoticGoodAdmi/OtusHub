package ru.ushakov.otushub.security.jwt

import org.slf4j.Logger
import org.slf4j.LoggerFactory
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

@Component
class CustomAuthenticationProvider(
    private val userRepository: UserRepository,
    private val passwordEncoder: BCryptPasswordEncoder
) : AuthenticationProvider {

    companion object {
        private val log: Logger = LoggerFactory.getLogger(this::class.java)
    }

    override fun authenticate(authentication: Authentication): Authentication {
        val userId = authentication.name
        val password = authentication.credentials.toString()

        val user = userRepository.findByUserId(userId)
            ?: throw UserNotFoundException(USER_NOT_FOUND_MESSAGE)

        if (!passwordEncoder.matches(password, user.password)) {
            log.error("Password doesn't match for user: {}", userId)
            throw InvalidCredentialsException("Невалидные данные")
        }

        return UsernamePasswordAuthenticationToken(user, password, user.roles.map { SimpleGrantedAuthority(it) })
    }

    override fun supports(authentication: Class<*>): Boolean {
        return authentication == UsernamePasswordAuthenticationToken::class.java
    }
}
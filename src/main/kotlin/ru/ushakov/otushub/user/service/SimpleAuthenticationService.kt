package ru.ushakov.otushub.user.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import ru.ushakov.otushub.security.jwt.TokenManager
import ru.ushakov.otushub.user.controller.request.UserLoginRequest

@Service
class SimpleAuthenticationService(
    private val authenticationManager: AuthenticationManager,
    private val tokenManager: TokenManager
) : AuthenticationService {

    companion object {
        private val log: Logger = LoggerFactory.getLogger(this::class.java)
    }

    override fun authenticate(loginRequest: UserLoginRequest): String {
        val authentication: Authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(loginRequest.userId, loginRequest.password)
        )
        SecurityContextHolder.getContext().authentication = authentication

        log.info("User {} authenticated successfully", loginRequest.userId)
        return tokenManager.generateToken(authentication)
    }
}
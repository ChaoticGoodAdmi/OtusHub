package ru.ushakov.otushub.user.service

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

    override fun authenticate(loginRequest: UserLoginRequest): String {
        val authentication: Authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(loginRequest.userId, loginRequest.password)
        )
        SecurityContextHolder.getContext().authentication = authentication

        return tokenManager.generateToken(authentication)
    }
}
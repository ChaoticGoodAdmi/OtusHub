package ru.ushakov.otushub.security.jwt

import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.core.Authentication

interface TokenManager {
    fun generateToken(authentication: Authentication): String
    fun validateToken(authToken: String): Boolean
    fun parseJwt(request: HttpServletRequest): String?
    fun getUsernameFromToken(token: String): String
}
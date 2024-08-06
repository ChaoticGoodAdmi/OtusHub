package ru.ushakov.otushub.security.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import io.vavr.control.Try
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import ru.ushakov.otushub.user.domain.User
import java.util.*

@Component
class JwtTokenManager(
    @Value("\${jwt.secret}") private val jwtSecret: String,
    @Value("\${jwt.expirationMs}") private val jwtExpirationMs: Long,
) : TokenManager {

    override fun generateToken(authentication: Authentication): String {
        val userPrincipal: User = authentication.principal as User
        return Jwts.builder()
            .subject(userPrincipal.id.toString())
            .issuedAt(Date())
            .expiration(Date(Date().time + jwtExpirationMs))
            .signWith(Keys.hmacShaKeyFor(jwtSecret.toByteArray()), Jwts.SIG.HS512)
            .compact()
    }

    override fun validateToken(authToken: String): Boolean {
        return Try.of {
            Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(jwtSecret.toByteArray()))
                .build()
                .parseSignedClaims(authToken)
            true
        }.getOrElse(false)
    }

    override fun parseJwt(request: HttpServletRequest): String? {
        val headerAuth = request.getHeader("Authorization")
        return if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            headerAuth.substring(7, headerAuth.length)
        } else null
    }

    override fun getUsernameFromToken(token: String): String {
        return Jwts.parser()
            .verifyWith(Keys.hmacShaKeyFor(jwtSecret.toByteArray()))
            .build()
            .parseSignedClaims(token)
            .payload.subject
    }

    override fun extractUserId(jwtToken: String): String {
        val claims = extractAllClaims(jwtToken)
        return claims.subject
    }

    private fun extractAllClaims(token: String): Claims {
        return Jwts.parser()
            .verifyWith(Keys.hmacShaKeyFor(jwtSecret.toByteArray()))
            .build()
            .parseSignedClaims(token)
            .payload
    }
}
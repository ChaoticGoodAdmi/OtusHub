package ru.ushakov.otushub.user.controller

import io.vavr.control.Try
import jakarta.validation.Valid
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.ushakov.otushub.exception.InvalidCredentialsException
import ru.ushakov.otushub.exception.UserNotFoundException
import ru.ushakov.otushub.health.aspect.HealthCheck
import ru.ushakov.otushub.user.controller.request.UserLoginRequest
import ru.ushakov.otushub.user.service.AuthenticationService

@RestController
@RequestMapping("/login")
@HealthCheck
class LoginController(
    private val authenticationService: AuthenticationService
) {

    companion object {
        private val log: Logger = LoggerFactory.getLogger(this::class.java)
    }

    @PostMapping
    fun login(@Valid @RequestBody loginRequest: UserLoginRequest): ResponseEntity<Map<String, String>> {
        log.info("Request for login user [{}]", loginRequest.userId)
        return Try.of {
            authenticationService.authenticate(loginRequest)
        }.map { token ->
            ResponseEntity.ok(mapOf("token" to token))
        }.recover { ex ->
            when (ex) {
                is InvalidCredentialsException -> ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(mapOf("description" to ex.message))
                is UserNotFoundException -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(mapOf("description" to ex.message))
                else -> throw RuntimeException()
            }
        }.get()
    }
}
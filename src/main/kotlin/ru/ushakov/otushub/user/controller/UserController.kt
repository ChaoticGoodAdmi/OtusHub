package ru.ushakov.otushub.user.controller

import jakarta.validation.Valid
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import ru.ushakov.otushub.exception.ErrorResponse
import ru.ushakov.otushub.health.aspect.HealthCheck
import ru.ushakov.otushub.user.controller.request.RegisterUserRequest
import ru.ushakov.otushub.user.domain.Sex
import ru.ushakov.otushub.user.service.RegistrationService
import ru.ushakov.otushub.user.service.UserService
import java.time.LocalDate
import java.util.*

const val USER_NOT_FOUND_MESSAGE = "Пользователь не найден"

private const val INVALID_DATA_MESSAGE = "Невалидные данные ввода"

data class RegisterUserResponse(val userId: String)

data class UserResponse(
    val id: String,
    val firstName: String,
    val secondName: String,
    val birthDate: LocalDate,
    val sex: Sex,
    val biography: String,
    val city: String
)


@RestController
@RequestMapping("/user")
@HealthCheck
class UserController(
    private val registrationService: RegistrationService,
    private val userService: UserService
) {

    companion object {
        private val log: Logger = LoggerFactory.getLogger(this::class.java)
    }

    @PostMapping("/register")
    fun registerUser(
        @Valid @RequestBody request: RegisterUserRequest,
        bindingResult: BindingResult
    ): ResponseEntity<Any> {
        if (bindingResult.hasErrors()) {
            val errors = bindingResult.fieldErrors.map { it.defaultMessage ?: INVALID_DATA_MESSAGE }
            return ResponseEntity(ErrorResponse(errors), HttpStatus.BAD_REQUEST)
        }
        log.info("Request for registration of User: {} {}", request.firstName, request.secondName)
        val savedUser = registrationService.registerUser(UserMapper.mapToUser(request))
        return ResponseEntity.ok(RegisterUserResponse(savedUser.id.toString()))
    }

    @GetMapping("/get/{id}")
    fun getUserById(@PathVariable id: String): ResponseEntity<Any> {
        log.info("Request for user info of User: {}", id)
        val user = userService.getUserById(id)
        log.info("Found user by id {}: {}", id, user)
        return if (user != null) {
            ResponseEntity.ok(UserMapper.mapToUserResponse(user))
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse(listOf("Анкета не найдена")))
        }
    }

    @GetMapping("/search")
    fun searchUsers(
        @RequestParam("first_name", required = false, defaultValue = "") firstName: String?,
        @RequestParam("last_name", required = false, defaultValue = "") secondName: String?
    ): ResponseEntity<Any> {
        return if (firstName == null || secondName == null || firstName.isBlank() || secondName.isBlank()) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse(listOf(INVALID_DATA_MESSAGE)))
        } else ResponseEntity.ok(
            userService.searchUsers(firstName, secondName).map { user -> UserMapper.mapToUserResponse(user) }
        )
    }
}

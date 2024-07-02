package ru.ushakov.otushub.user.controller.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Past
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import java.time.LocalDate

data class RegisterUserRequest(
    @field:NotBlank(message = "First name is required.")
    var firstName: String?,

    @field:NotBlank(message = "Second name is required.")
    var secondName: String?,

    @field:Past(message = "Birth date must be in the past.")
    var birthDate: LocalDate?,

    @field:Pattern(regexp = "^(MALE|FEMALE)$", message = "Sex must be either 'MALE' or 'FEMALE'")
    var sex: String?,

    @field:NotBlank(message = "Biography is required.")
    @field:Size(max = 200, message = "Biography cannot exceed 200 characters.")
    var biography: String?,

    @field:NotBlank(message = "City is required.")
    var city: String?,

    @field:NotBlank(message = "Password is required.")
    var password: String?
)
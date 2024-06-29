package ru.ushakov.otushub.user.controller.request

import jakarta.validation.constraints.NotBlank

class UserLoginRequest(
    @field:NotBlank(message = "Username must not be blank")
    val userId: String,
    @field:NotBlank(message = "Password must not be blank")
    val password: String,
)
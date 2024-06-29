package ru.ushakov.otushub.user.service

import ru.ushakov.otushub.user.controller.request.UserLoginRequest

interface AuthenticationService {

    fun authenticate(loginRequest: UserLoginRequest): String
}

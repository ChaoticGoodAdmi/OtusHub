package ru.ushakov.otushub.user.service

import ru.ushakov.otushub.user.domain.User

fun interface RegistrationService {

    fun registerUser(user: User): User
}

package ru.ushakov.otushub.user.service

import ru.ushakov.otushub.user.domain.User

fun interface UserService {

    fun getUserById(id: String): User?
}
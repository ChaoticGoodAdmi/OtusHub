package ru.ushakov.otushub.user.service

import ru.ushakov.otushub.user.domain.User

interface UserService {

    fun getUserById(id: String): User?
}
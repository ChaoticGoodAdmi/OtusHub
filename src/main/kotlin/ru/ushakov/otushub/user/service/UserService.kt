package ru.ushakov.otushub.user.service

import ru.ushakov.otushub.user.domain.User
import java.util.*

interface UserService {

    fun getUserById(id: UUID): User?
}
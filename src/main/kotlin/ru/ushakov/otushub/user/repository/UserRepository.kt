package ru.ushakov.otushub.user.repository

import ru.ushakov.otushub.user.domain.User
import java.util.*

interface UserRepository {

    fun save(user: User): User

    fun findByUserId(userId: UUID): User?
}
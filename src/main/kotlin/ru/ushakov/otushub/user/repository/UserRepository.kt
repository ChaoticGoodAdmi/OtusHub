package ru.ushakov.otushub.user.repository

import ru.ushakov.otushub.user.domain.User

interface UserRepository {

    fun save(user: User): User

    fun findByUserId(userId: String): User?

    fun searchByFirstNameOrSecondName(firstName: String, secondName: String): List<User>
}
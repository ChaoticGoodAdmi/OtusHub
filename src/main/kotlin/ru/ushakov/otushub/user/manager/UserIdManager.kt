package ru.ushakov.otushub.user.manager

fun interface UserIdManager {

    fun generateUserId(): String
}

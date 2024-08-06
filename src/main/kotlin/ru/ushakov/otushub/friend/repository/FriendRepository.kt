package ru.ushakov.otushub.friend.repository

interface FriendRepository {

    fun isFriend(userId: String, friendId: String): Boolean

    fun addFriend(userId: String, friendId: String)

    fun removeFriend(userId: String, friendId: String)
}

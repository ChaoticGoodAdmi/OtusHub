package ru.ushakov.otushub.friend.service

interface FriendService {

    fun addFriend(userId: String, friendId: String)

    fun removeFriend(userId: String, friendId: String)
}

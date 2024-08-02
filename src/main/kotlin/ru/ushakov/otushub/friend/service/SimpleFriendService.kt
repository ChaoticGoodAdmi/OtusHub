package ru.ushakov.otushub.friend.service

import org.springframework.stereotype.Service
import ru.ushakov.otushub.exception.FriendAlterationException
import ru.ushakov.otushub.friend.repository.FriendRepository
import ru.ushakov.otushub.user.repository.UserRepository

@Service
class SimpleFriendService(
    private val userRepository: UserRepository,
    private val friendRepository: FriendRepository
) : FriendService {

    override fun addFriend(userId: String, friendId: String) {
        if (userRepository.findByUserId(friendId) == null) {
            throw FriendAlterationException("User with id $friendId does not exist")
        }
        if (friendRepository.isFriend(userId, friendId)) {
            throw FriendAlterationException("User with id $friendId is already in your friend list")
        }
        return friendRepository.addFriend(userId, friendId)
    }

    override fun removeFriend(userId: String, friendId: String) {
        if (userRepository.findByUserId(friendId) == null) {
            throw FriendAlterationException("User with id $friendId does not exist")
        }
        if (!friendRepository.isFriend(userId, friendId)) {
            throw FriendAlterationException("User with id $friendId is not in your friend list")
        }
        friendRepository.removeFriend(userId, friendId)
    }
}
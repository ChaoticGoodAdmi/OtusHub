package ru.ushakov.otushub.user.service

import org.springframework.stereotype.Service
import ru.ushakov.otushub.user.domain.User
import ru.ushakov.otushub.user.repository.UserRepository
import org.springframework.transaction.annotation.Transactional;

@Service
class SimpleUserService(
    private val userRepository: UserRepository
) : UserService {

    @Transactional(readOnly = true)
    override fun getUserById(id: String): User? {
        return userRepository.findByUserId(id)
    }

    @Transactional(readOnly = true)
    override fun searchUsers(firstName: String, secondName: String): List<User> {
        return userRepository.searchByFirstNameOrSecondName(firstName, secondName)
    }
}


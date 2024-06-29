package ru.ushakov.otushub.user.service

import org.springframework.stereotype.Service
import ru.ushakov.otushub.user.domain.User
import ru.ushakov.otushub.user.repository.UserRepository
import java.util.*

@Service
class SimpleUserService(
    private val userRepository: UserRepository
) : UserService {

    override fun getUserById(id: UUID): User? {
        return userRepository.findByUserId(id)
    }
}


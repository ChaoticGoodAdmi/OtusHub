package ru.ushakov.otushub.user.service

import io.vavr.control.Try
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import ru.ushakov.otushub.user.domain.User
import ru.ushakov.otushub.user.manager.UserIdManager
import ru.ushakov.otushub.user.repository.JdbcUserRepository

@Service
class SimpleRegistrationService(
    private val userRepository: JdbcUserRepository,
    private val passwordEncoder: BCryptPasswordEncoder,
    private val userIdManager: UserIdManager
) : RegistrationService {

    override fun registerUser(user: User): User {
        val hashedPassword = passwordEncoder.encode(user.password)
        val fullSecureUser = user.copy(
            id = userIdManager.generateUserId(),
            password = hashedPassword
        )

        return Try.of {
            userRepository.save(fullSecureUser)
        }.getOrElseThrow { _ -> RuntimeException("Невалидные данные") }
    }
}
package ru.ushakov.otushub.user.service

import io.vavr.control.Try
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import ru.ushakov.otushub.user.domain.User
import ru.ushakov.otushub.user.repository.JdbcUserRepository

@Service
class SimpleRegistrationService(
    private val userRepository: JdbcUserRepository,
    private val passwordEncoder: BCryptPasswordEncoder,
) : RegistrationService {

    override fun registerUser(user: User): User {
        val hashedPassword = passwordEncoder.encode(user.password)
        val userWithHashedPassword = user.copy(password = hashedPassword)

        return Try.of {
            userRepository.save(userWithHashedPassword)
        }.getOrElseThrow { _ -> RuntimeException("Невалидные данные") }
    }
}
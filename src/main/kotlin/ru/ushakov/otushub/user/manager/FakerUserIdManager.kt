package ru.ushakov.otushub.user.manager

import net.datafaker.Faker
import org.springframework.stereotype.Component
import ru.ushakov.otushub.user.repository.UserRepository

@Component
class FakerUserIdManager(
    private val userRepository: UserRepository,
    private val faker: Faker
) : UserIdManager {
    override fun generateUserId(): String {
        var userId: String

        do {
            val randomWord = faker.lorem().word()
            val randomDigits = faker.number().digits(4)
            userId = "$randomWord$randomDigits"
        } while (userRepository.findByUserId(userId) != null)

        return userId
    }
}

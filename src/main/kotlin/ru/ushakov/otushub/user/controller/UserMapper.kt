package ru.ushakov.otushub.user.controller

import ru.ushakov.otushub.user.controller.request.RegisterUserRequest
import ru.ushakov.otushub.user.domain.Sex
import ru.ushakov.otushub.user.domain.User

class UserMapper {
    companion object {
        fun mapToUser(request: RegisterUserRequest): User {
            return User(
                firstName = request.firstName,
                secondName = request.secondName,
                birthDate = request.birthDate,
                sex = Sex.valueOf(request.sex),
                biography = request.biography,
                city = request.city,
                password = request.password
            )
        }
    }
}
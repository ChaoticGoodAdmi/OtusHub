package ru.ushakov.otushub.exception

data class ErrorResponse(val errors: List<String>)

class InvalidCredentialsException(message: String) : RuntimeException(message)
class UserNotFoundException(message: String) : RuntimeException(message)
class FriendAlterationException(message: String) : RuntimeException(message)
class PostAlterationException(message: String) : RuntimeException(message)
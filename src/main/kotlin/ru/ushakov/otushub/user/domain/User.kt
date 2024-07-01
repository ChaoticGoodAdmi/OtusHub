package ru.ushakov.otushub.user.domain

import java.time.LocalDate

data class User(
    var id: String? = null,
    val firstName: String,
    val secondName: String,
    val birthDate: LocalDate,
    val sex: Sex,
    val biography: String,
    val city: String,
    val password: String,
    val roles: List<String> = listOf("ROLE_USER"),
)

enum class Sex(val shortName: String) {
    MALE("M"),
    FEMALE("F");

    companion object {
        fun fromShortName(shortName: String?): Sex {
            return if (shortName == MALE.shortName) MALE
            else FEMALE
        }
    }
}

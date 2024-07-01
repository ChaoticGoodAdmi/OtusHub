package ru.ushakov.otushub.user.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import ru.ushakov.otushub.user.controller.USER_NOT_FOUND_MESSAGE
import ru.ushakov.otushub.user.repository.UserRepository

@Service
class CustomUserDetailsService(@Autowired private val userRepository: UserRepository) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUserId(username)
            ?: throw UsernameNotFoundException(USER_NOT_FOUND_MESSAGE)

        return org.springframework.security.core.userdetails.User(
            user.id.toString(),
            user.password,
            user.roles.map { SimpleGrantedAuthority(it) }
        )
    }
}
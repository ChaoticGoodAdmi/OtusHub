package ru.ushakov.otushub.security.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.config.Customizer.withDefaults
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter
import ru.ushakov.otushub.security.jwt.CustomAuthenticationProvider
import ru.ushakov.otushub.security.jwt.JwtAuthenticationFilter
import ru.ushakov.otushub.user.repository.UserRepository


@Configuration
class SecurityConfig {

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity, jwtAuthenticationFilter: JwtAuthenticationFilter): SecurityFilterChain {
        http
            .csrf { csrf -> csrf.disable() }
            .httpBasic { httpBasic -> httpBasic.disable() }
            .authorizeHttpRequests { auth ->
                auth.requestMatchers(
                    "/user/register",
                    "/login",
                    "/actuator/**",
                    "/ping"
                ).permitAll()
                    .anyRequest().authenticated()
            }
            .cors(withDefaults())
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }

    @Bean
    fun corsFilter(): CorsFilter? {
        val source = UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration()
        config.allowCredentials = true
        config.addAllowedOrigin("http://localhost:3000")
        config.addAllowedHeader("*")
        config.addAllowedMethod("*")
        source.registerCorsConfiguration("/**", config)
        return CorsFilter(source)
    }

    @Bean
    fun authenticationManager(userRepository: UserRepository): AuthenticationManager? {
        val authProvider = CustomAuthenticationProvider(userRepository, passwordEncoder())
        return ProviderManager(authProvider)
    }
}
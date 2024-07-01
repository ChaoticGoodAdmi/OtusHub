package ru.ushakov.otushub

import net.datafaker.Faker
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AppConfig {

    @Bean
    fun faker(): Faker = Faker()
}
package ru.ushakov.otushub.cache

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer


@Configuration
@EnableCaching
class CacheConfig {

    @Bean
    fun redisConnectionFactory(): LettuceConnectionFactory {
        return LettuceConnectionFactory()
    }

    @Bean
    fun redisTemplate(): RedisTemplate<String, Any> {
        val template = RedisTemplate<String, Any>()
        template.connectionFactory = redisConnectionFactory()

        val objectMapper = ObjectMapper().apply {
            registerModule(JavaTimeModule())
            disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        }

        template.keySerializer = StringRedisSerializer()
        template.valueSerializer = GenericJackson2JsonRedisSerializer(objectMapper)

        return template
    }

    @Bean
    fun cacheManager(redisConnectionFactory: RedisConnectionFactory): RedisCacheManager {
        return RedisCacheManager.builder(redisConnectionFactory).build()
    }
}
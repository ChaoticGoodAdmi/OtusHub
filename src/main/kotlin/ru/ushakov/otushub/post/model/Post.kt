package ru.ushakov.otushub.post.model

import java.time.LocalDateTime

data class Post(
    val id: Long,
    val userId: String,
    val content: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
package ru.ushakov.otushub.post.controller

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank

data class PostRequest @JsonCreator constructor(
    @JsonProperty("content")
    @field:NotBlank
    val content: String
)
package ru.ushakov.otushub.post.controller

import jakarta.validation.Valid
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.ushakov.otushub.post.service.PostService
import ru.ushakov.otushub.security.jwt.JwtTokenManager
import java.time.LocalDateTime

@RestController
@RequestMapping("/post")
class PostController(
    private val postService: PostService,
    private val jwtTokenManager: JwtTokenManager
) {

    companion object {
        private val log: Logger = LoggerFactory.getLogger(this::class.java)
    }

    @PostMapping("/create")
    fun createPost(
        @RequestHeader("Authorization") authHeader: String,
        @Valid @RequestBody postRequest: PostRequest
    ): ResponseEntity<PostResponse> {
        val jwtToken = authHeader.substring(7)
        val userId = jwtTokenManager.extractUserId(jwtToken)
        log.info("User $userId is trying to publish a new post: ${postRequest.content}")
        val postId = postService.createPost(userId, postRequest.content)
        return ResponseEntity.ok(PostResponse(postId, userId))
    }

    @PutMapping("/update/{postId}")
    fun updatePost(
        @RequestHeader("Authorization") authHeader: String,
        @PathVariable postId: Long,
        @RequestBody postRequest: PostRequest
    ): ResponseEntity<PostResponse> {
        val jwtToken = authHeader.substring(7)
        val userId = jwtTokenManager.extractUserId(jwtToken)
        log.info("User $userId is trying to update a post: $postId")
        postService.updatePost(userId, postId, postRequest.content)
        return ResponseEntity.ok(PostResponse(postId, userId))
    }

    @DeleteMapping("/delete/{postId}")
    fun deletePost(
        @RequestHeader("Authorization") authHeader: String,
        @PathVariable postId: Long
    ): ResponseEntity<PostResponse> {
        val jwtToken = authHeader.substring(7)
        val userId = jwtTokenManager.extractUserId(jwtToken)
        log.info("User $userId is trying to delete a post: $postId")
        postService.deletePost(userId, postId)
        return ResponseEntity.ok(PostResponse(postId, userId))
    }

    @GetMapping("/get/{postId}")
    fun getPost(
        @RequestHeader("Authorization") authHeader: String,
        @PathVariable postId: Long
    ): ResponseEntity<PostResponse> {
        val jwtToken = authHeader.substring(7)
        val userId = jwtTokenManager.extractUserId(jwtToken)
        log.info("User $userId is trying to get a post: $postId")
        val post = postService.getPost(userId, postId)
        return ResponseEntity.ok(post)
    }

    @GetMapping("/feed")
    fun getFriendFeed(
        @RequestHeader("Authorization") authHeader: String,
        @RequestParam(defaultValue = "0") offset: Int,
        @RequestParam(defaultValue = "10") limit: Int
    ): ResponseEntity<List<PostResponse>> {
        val jwtToken = authHeader.substring(7)
        val userId = jwtTokenManager.extractUserId(jwtToken)
        val posts = postService.getFriendFeed(userId, offset, limit)
        return ResponseEntity.ok(posts)
    }
}

data class PostResponse(
    val postId: Long,
    val userId: String,
    var content: String? = null,
    var createdAt: LocalDateTime? = null,
    var updatedAt: LocalDateTime? = null
)
package ru.ushakov.otushub.post.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import ru.ushakov.otushub.exception.PostAlterationException
import ru.ushakov.otushub.post.controller.PostResponse
import ru.ushakov.otushub.post.repository.PostRepository

@Service
class CachingPostService(
    private val postRepository: PostRepository,
    private val redisTemplate: RedisTemplate<String, Any>,
    private val objectMapper: ObjectMapper
) : PostService {

    private val CACHE_KEY_PREFIX = "friend_feed:"
    private val MAX_CACHE_SIZE = 1000

    override fun createPost(userId: String, content: String): Long {
        val postId = postRepository.createPost(userId, content)
        invalidateFriendFeedCacheForUserFriends(userId)
        return postId
    }

    override fun updatePost(userId: String, postId: Long, content: String) {
        if (!postRepository.isPostOwner(userId, postId)) {
            throw PostAlterationException("User does not have permission to update this post")
        }
        postRepository.updatePost(postId, content)
        invalidateFriendFeedCacheForUserFriends(userId)
    }

    override fun deletePost(userId: String, postId: Long) {
        if (!postRepository.isPostOwner(userId, postId)) {
            throw PostAlterationException("User does not have permission to delete this post")
        }
        postRepository.deletePost(postId)
        invalidateFriendFeedCacheForUserFriends(userId)
    }

    override fun getPost(userId: String, postId: Long): PostResponse {
        if (!postRepository.isPostOwner(userId, postId)) {
            throw PostAlterationException("User does not have permission to delete this post")
        }
        val post = postRepository.getPost(postId)
        return if (post != null) {
            PostResponse(post.id, post.userId, post.content, post.createdAt, post.updatedAt)
        } else {
            throw PostAlterationException("Post not found")
        }
    }

    override fun getFriendFeed(userId: String, offset: Int, limit: Int): List<PostResponse> {
        val cacheKey = CACHE_KEY_PREFIX + userId
        val cachedFeed: List<PostResponse>? = redisTemplate.opsForList()
            .range(cacheKey, 0, (MAX_CACHE_SIZE - 1).toLong())
            ?.map { entry ->
                objectMapper.convertValue(entry, PostResponse::class.java)
            }

        if (!cachedFeed.isNullOrEmpty()) {
            val end = (offset + limit).coerceAtMost(cachedFeed.size)
            return if (offset in 0 until end) cachedFeed.subList(offset, end) else emptyList()
        }

        val posts = postRepository.findPostsByFriends(userId, MAX_CACHE_SIZE)
        val postResponses = posts.map { post ->
            PostResponse(post.id, post.userId, post.content, post.createdAt, post.updatedAt)
        }

        if (postResponses.isNotEmpty()) {
            redisTemplate.opsForList().rightPushAll(cacheKey, postResponses)
            redisTemplate.opsForList().trim(cacheKey, 0, (MAX_CACHE_SIZE - 1).toLong())
        }

        val end = (offset + limit).coerceAtMost(postResponses.size)
        return if (offset in 0 until end) postResponses.subList(offset, end) else emptyList()
    }

    private fun invalidateFriendFeedCacheForUserFriends(userId: String) {
        val friendIds = postRepository.findFriendIds(userId)
        friendIds.forEach { invalidateFriendFeedCache(it) }
    }

    private fun invalidateFriendFeedCache(userId: String) {
        val cacheKey = CACHE_KEY_PREFIX + userId
        redisTemplate.delete(cacheKey)
    }
}

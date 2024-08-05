package ru.ushakov.otushub.post.service

import org.springframework.stereotype.Service
import ru.ushakov.otushub.exception.PostAlterationException
import ru.ushakov.otushub.post.controller.PostResponse
import ru.ushakov.otushub.post.repository.PostRepository

@Service
class SimplePostService(
    private val postRepository: PostRepository
) : PostService {

    override fun createPost(userId: String, content: String): Long {
        return postRepository.createPost(userId, content)
    }

    override fun getFriendFeed(userId: String, offset: Int, limit: Int): List<PostResponse> {
        val posts = postRepository.findPostsByFriends(userId, offset, limit)
        return posts.map { post ->
            PostResponse(post.id, post.userId, post.content, post.createdAt, post.updatedAt)
        }
    }

    override fun updatePost(userId: String, postId: Long, content: String) {
        if (!postRepository.isPostOwner(userId, postId)) {
            throw PostAlterationException("User does not have permission to update this post")
        }
        postRepository.updatePost(postId, content)
    }

    override fun deletePost(userId: String, postId: Long) {
        if (!postRepository.isPostOwner(userId, postId)) {
            throw PostAlterationException("User does not have permission to delete this post")
        }
        postRepository.deletePost(postId)
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
}
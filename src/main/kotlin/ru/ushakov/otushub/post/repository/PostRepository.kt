package ru.ushakov.otushub.post.repository

import ru.ushakov.otushub.post.model.Post

interface PostRepository {

    fun createPost(userId: String, content: String): Long

    fun isPostOwner(userId: String, postId: Long): Boolean

    fun updatePost(postId: Long, content: String)

    fun deletePost(postId: Long)

    fun getPost(postId: Long): Post?

    fun findPostsByFriends(userId: String, limit: Int): List<Post>

    fun findFriendIds(userId: String): List<String>
}

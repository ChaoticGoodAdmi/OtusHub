package ru.ushakov.otushub.post.service

import ru.ushakov.otushub.post.controller.PostResponse

interface PostService {

    fun createPost(userId: String, content: String): Long

    fun updatePost(userId: String, postId: Long, content: String)

    fun deletePost(userId: String, postId: Long)

    fun getPost(userId: String, postId: Long): PostResponse?

    fun getFriendFeed(userId: String, offset: Int, limit: Int): List<PostResponse>?
}

package ru.ushakov.otushub.friend.repository

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class JdbcFriendRepository(
    private val jdbcTemplate: JdbcTemplate
) : FriendRepository {

    override fun addFriend(userId: String, friendId: String) {
        val sql = "INSERT INTO userdb.friends (user_id, friend_id) VALUES (?, ?)"
        jdbcTemplate.update(sql, userId, friendId)
    }

    override fun removeFriend(userId: String, friendId: String) {
        val sql = "DELETE FROM userdb.friends WHERE user_id = ? AND friend_id = ?"
        jdbcTemplate.update(sql, userId, friendId)
    }

    override fun isFriend(userId: String, friendId: String): Boolean {
        val sql = "SELECT COUNT(*) FROM userdb.friends WHERE user_id = ? AND friend_id = ?"
        return jdbcTemplate.queryForObject(sql, Int::class.java, userId, friendId) > 0
    }
}
package ru.ushakov.otushub.post.repository

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.PreparedStatementSetter
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import ru.ushakov.otushub.post.model.Post

@Repository
class JdbcPostRepository(
    private val jdbcTemplate: JdbcTemplate
) : PostRepository {

    private val postRowMapper = RowMapper { rs, _ ->
        Post(
            id = rs.getLong("id"),
            userId = rs.getString("user_id"),
            content = rs.getString("content"),
            createdAt = rs.getTimestamp("created_at").toLocalDateTime(),
            updatedAt = rs.getTimestamp("updated_at").toLocalDateTime()
        )
    }

    override fun createPost(userId: String, content: String): Long {
        val sql = "INSERT INTO userdb.posts (user_id, content) VALUES (?, ?) RETURNING id"
        return jdbcTemplate.queryForObject(sql, Long::class.java, userId, content)
    }

    override fun updatePost(postId: Long, content: String) {
        val sql = "UPDATE userdb.posts SET content = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?"
        jdbcTemplate.update(sql, content, postId)
    }

    override fun deletePost(postId: Long) {
        val sql = "DELETE FROM userdb.posts WHERE id = ?"
        jdbcTemplate.update(sql, postId)
    }

    override fun isPostOwner(userId: String, postId: Long): Boolean {
        val sql = "SELECT COUNT(*) FROM userdb.posts WHERE id = ? AND user_id = ?"
        return jdbcTemplate.queryForObject(sql, Int::class.java, postId, userId) > 0
    }

    override fun getPost(postId: Long): Post? {
        val sql = "SELECT * FROM userdb.posts WHERE id = ?"
        return jdbcTemplate.queryForObject(sql, postRowMapper, postId)
    }

    override fun findPostsByFriends(userId: String, limit: Int): List<Post> {
        val sql = """
            SELECT p.id, p.user_id, p.content, p.created_at, p.updated_at 
            FROM userdb.posts p
            INNER JOIN userdb.friends f ON p.user_id = f.friend_id
            WHERE f.user_id = ?
            ORDER BY p.created_at DESC
            LIMIT ?
        """
        return jdbcTemplate.query(
            sql,
            PreparedStatementSetter { ps ->
                ps.setString(1, userId)
                ps.setInt(2, limit)
            },
            postRowMapper
        )
    }

    override fun findFriendIds(userId: String): List<String> {
        val sql = """
            SELECT user_id 
            FROM userdb.friends 
            WHERE friend_id = ?
        """
        return jdbcTemplate.query(sql, { ps -> ps.setString(1, userId) }) { rs, _ ->
            rs.getString("user_id")
        }
    }
}
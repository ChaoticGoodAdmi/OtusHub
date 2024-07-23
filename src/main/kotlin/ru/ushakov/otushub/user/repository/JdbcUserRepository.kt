package ru.ushakov.otushub.user.repository

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.PreparedStatementSetter
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import ru.ushakov.otushub.user.domain.Sex
import ru.ushakov.otushub.user.domain.User

@Repository
class JdbcUserRepository(
    private val jdbcTemplate: JdbcTemplate
) : UserRepository {

    companion object {
        private val log: Logger = LoggerFactory.getLogger(this::class.java)
    }

    private val userRowMapper = RowMapper<User> { rs, _ ->
        User(
            id = rs.getString("id"),
            firstName = rs.getString("first_name"),
            secondName = rs.getString("second_name"),
            birthDate = rs.getDate("birth_date").toLocalDate(),
            sex = Sex.fromShortName(rs.getString("sex")),
            biography = rs.getString("biography"),
            city = rs.getString("city"),
            password = rs.getString("password_hash")
        )
    }

    override fun save(user: User): User {
        val sql = """
            INSERT INTO userdb.users (id, first_name, second_name, birth_date, sex, biography, city, password_hash) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """.trimIndent()

        jdbcTemplate.update(
            sql,
            user.id,
            user.firstName,
            user.secondName,
            user.birthDate,
            user.sex.shortName,
            user.biography,
            user.city,
            user.password
        )
        return user
    }

    override fun searchByFirstNameOrSecondName(firstName: String, secondName: String): List<User> {
        val sql = StringBuilder("SELECT * FROM userdb.users WHERE ")
        val params = mutableListOf<Any>()

        sql.append("LOWER(first_name) LIKE ? ")
        params.add("$firstName%")

        sql.append("AND LOWER(second_name) LIKE ? ;")
        params.add("$secondName%")

        return jdbcTemplate.query(sql.toString(), userRowMapper, *params.toTypedArray())
    }

    override fun findByUserId(userId: String): User? {
        val sql = """
            SELECT id, first_name, second_name, birth_date, sex, biography, city, password_hash 
            FROM userdb.users 
            WHERE id = ?
        """.trimIndent()

        return jdbcTemplate.query(sql, PreparedStatementSetter { ps ->
            ps.setObject(1, userId)
        }, userRowMapper).firstOrNull()
    }
}
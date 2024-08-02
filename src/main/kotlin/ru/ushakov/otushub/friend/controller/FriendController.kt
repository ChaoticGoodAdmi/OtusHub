package ru.ushakov.otushub.friend.controller

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.ushakov.otushub.exception.FriendAlterationException
import ru.ushakov.otushub.friend.service.FriendService
import ru.ushakov.otushub.health.aspect.HealthCheck
import ru.ushakov.otushub.security.jwt.JwtTokenManager

@RestController
@RequestMapping("/friend")
@HealthCheck
class FriendController(
    private val friendService: FriendService,
    private val jwtTokenManager: JwtTokenManager
) {

    companion object {
        private val log: Logger = LoggerFactory.getLogger(this::class.java)
    }

    @PutMapping("/set/{friendId}")
    fun addFriend(
        @RequestHeader("Authorization") authHeader: String,
        @PathVariable friendId: String
    ): ResponseEntity<FriendAlterationResult> {
        val jwtToken = authHeader.substring(7)
        val userId = jwtTokenManager.extractUserId(jwtToken)
        log.info("User $userId is trying to add $friendId to their friend-list")
        return try {
            friendService.addFriend(userId, friendId)
            ResponseEntity.ok(FriendAlterationResult(true, "Friend added successfully"))
        } catch (e: FriendAlterationException) {
            log.error("Problem with adding a friend of user $userId", e)
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(FriendAlterationResult(false, e.message ?: "Error adding friend"))
        }
    }

    @DeleteMapping("/delete/{friendId}")
    fun removeFriend(
        @RequestHeader("Authorization") authHeader: String,
        @PathVariable friendId: String
    ): ResponseEntity<FriendAlterationResult> {
        val jwtToken = authHeader.substring(7)
        val userId = jwtTokenManager.extractUserId(jwtToken)
        log.info("User $userId is trying to delete $friendId from their friend-list")
        try {
            friendService.removeFriend(userId, friendId)
            return ResponseEntity.ok(FriendAlterationResult(true, "Friend removed successfully"))
        } catch (e: FriendAlterationException) {
            log.error("Problem with deleting a friend of user $userId", e)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(FriendAlterationResult(false, e.message ?: "Error removing friend"))
        }
    }
}

data class FriendAlterationResult(val success: Boolean, val message: String)
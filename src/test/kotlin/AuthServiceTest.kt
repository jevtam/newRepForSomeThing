package domain.services

import domain.model.User
import domain.ports.Hasher
import domain.ports.UserRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class AuthServiceTest {

    @Test
    fun valid_password_returns_true() {
        val userRepo = mockk<UserRepository>()
        val hasher = mockk<Hasher>()
        val service = AuthService(userRepo, hasher)

        val user = User(
            login = "alice",
            salt = "salt",
            passwordHashHex = "hash"
        )

        every { userRepo.find("alice") } returns user
        every { hasher.sha256Hex("saltpwd") } returns "hash"

        val result = service.isPasswordValid("alice", "pwd")

        assertTrue(result)
    }

    @Test
    fun invalid_password_returns_false() {
        val userRepo = mockk<UserRepository>()
        val hasher = mockk<Hasher>()
        val service = AuthService(userRepo, hasher)

        val user = User("alice", "salt", "hash")

        every { userRepo.find("alice") } returns user
        every { hasher.sha256Hex(any()) } returns "other-hash"

        val result = service.isPasswordValid("alice", "pwd")

        assertFalse(result)
    }
}
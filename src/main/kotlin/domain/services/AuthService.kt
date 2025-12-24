package domain.services
import domain.ports.Hasher
import domain.ports.UserRepository

class AuthService(
    private val users: UserRepository,
    private val hasher: Hasher
) {
    fun isPasswordValid(login: String, rawPassword: String): Boolean {
        val u = users.find(login) ?: return false
        return hasher.sha256Hex(u.salt + rawPassword) == u.passwordHashHex
    }
}
package domain.services
import domain.ports.Hasher
import domain.ports.UserRepository

class AuthService(
    private val userRepository: UserRepository,
    private val hasher: Hasher
) {
    private val log = LoggerFactory.getLogger(AuthService::class.java)

    fun isPasswordValid(login: String, password: String): Boolean {
        val user = userRepository.find(login) ?: return false.also {
            log.warn("User {} not found", login)
        }
        val hash = hasher.sha256Hex(user.salt + password)
        return (hash == user.passwordHashHex).also {
            log.info("Auth result for {}: {}", login, it)
        }
    }
}
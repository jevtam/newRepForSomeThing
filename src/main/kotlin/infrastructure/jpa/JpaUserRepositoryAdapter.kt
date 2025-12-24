package infrastructure.jpa

import data.repo.UserJpaRepository
import domain.model.User
import domain.ports.UserRepository
import org.springframework.stereotype.Repository

class JpaUserRepositoryAdapter(
    private val repo: UserJpaRepository
) : UserRepository {

    override fun find(login: String): User? {
        val e = repo.findByUsername(login) ?: return null
        return User(
            login = e.username,
            salt = e.salt,
            passwordHashHex = e.passwordHashHex
        )
    }
}

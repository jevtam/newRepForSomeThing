package infrastructure.jpa

import data.repo.PermissionJpaRepository
import domain.model.Action
import domain.ports.PermissionService
import org.springframework.stereotype.Service

class JpaPermissionServiceAdapter(
    private val repo: PermissionJpaRepository
) : PermissionService {

    override fun allowed(login: String, path: String): Set<Action> {
        val rows = repo.findAllByUserUsernameAndResourcePath(login, path)
        if (rows.isEmpty()) return emptySet()

        return rows
            .asSequence()
            .flatMap { it.actions.split(",").asSequence() }
            .map { it.trim().lowercase() }
            .filter { it.isNotEmpty() }
            .map(::toAction)
            .toSet()
    }

    private fun toAction(s: String): Action =
        when (s) {
            "read" -> Action.read
            "write" -> Action.write
            "exec" -> Action.exec
            else -> throw IllegalArgumentException("Unknown action: $s")
        }
}

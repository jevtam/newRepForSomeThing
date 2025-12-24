package infrastructure.repo
import domain.model.Action
import domain.ports.PermissionService

class InMemoryPermissionService : PermissionService {
    private val table: Map<String, Map<String, Set<Action>>> = mapOf(
        "alice" to mapOf(
            "A"   to setOf(Action.read),
            "A.B" to setOf(Action.read, Action.write)
        ),
        "bob" to mapOf(
            "A.A8B" to setOf(Action.read)
        )
    )

    override fun allowed(login: String, path: String): Set<Action> {
        val userPerms = table[login] ?: return emptySet()
        val acc = mutableSetOf<Action>()
        var cur = path
        while (true) {             // A.B.C -> A.B.C, A.B, A
            userPerms[cur]?.let { acc.addAll(it) }
            val i = cur.lastIndexOf('.')
            if (i < 0) break
            cur = cur.substring(0, i)
        }
        return acc
    }
}
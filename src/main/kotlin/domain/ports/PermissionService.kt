package domain.ports
import domain.model.Action
interface PermissionService {
    fun allowed(login: String, path: String): Set<Action>
}
package domain.services
import domain.model.Action
import domain.ports.PermissionService

class AccessPolicy(private val permissions: PermissionService) {
    fun isAllowed(login: String, resourcePath: String, action: Action): Boolean =
        action in permissions.allowed(login, resourcePath)
}
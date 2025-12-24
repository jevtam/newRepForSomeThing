package domain.services

import domain.model.Action
import domain.ports.PermissionService
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class AccessPolicyTest {

    private val permissionService = mockk<PermissionService>()
    private val policy = AccessPolicy(permissionService)

    @Test
    fun returns_true_when_action_is_in_allowed_set() {
        every { permissionService.allowed("alice", "A.B.C") } returns
                setOf(Action.read, Action.write)

        val result = policy.isAllowed(
            login = "alice",
            resourcePath = "A.B.C",
            action = Action.read
        )

        assertTrue(result)
    }

    @Test
    fun returns_false_when_action_is_not_in_allowed_set() {
        every { permissionService.allowed("alice", "A.B.C") } returns
                setOf(Action.write)

        val result = policy.isAllowed(
            login = "alice",
            resourcePath = "A.B.C",
            action = Action.read
        )

        assertFalse(result)
    }
}
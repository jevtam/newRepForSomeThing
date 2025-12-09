package usecase

import domain.model.Action
import domain.services.AccessPolicy
import domain.services.AuthService
import domain.services.QuotaService
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CheckAccessUseCaseTest {

    private val authSvc: AuthService = mockk()
    private val policy: AccessPolicy = mockk()
    private val quotaSvc: QuotaService = mockk()

    private val usecase = CheckAccess(
        auth = authSvc,
        policy = policy,
        quota = quotaSvc
    )

    @Test
    fun ok_returns_Ok() {
        every { authSvc.isPasswordValid("alice", "pwd") } returns true
        every { quotaSvc.validate("A.B.C", 10) } returns QuotaService.Result.Ok
        every { policy.isAllowed("alice", "A.B.C", Action.read) } returns true

        val input = CheckAccessInput(
            login = "alice",
            password = "pwd",
            action = Action.read,
            resource = "A.B.C",
            volume = 10
        )

        val result = usecase.execute(input)

        assertEquals(CheckAccessResult.Ok, result)
    }

    @Test
    fun wrong_password_returns_BadLoginOrPassword() {
        every { authSvc.isPasswordValid("alice", "pwd") } returns false

        val input = CheckAccessInput(
            login = "alice",
            password = "pwd",
            action = Action.read,
            resource = "A.B.C",
            volume = 1
        )

        val result = usecase.execute(input)

        assertEquals(CheckAccessResult.BadLoginOrPassword, result)
    }

    @Test
    fun resource_not_found_returns_ResourceNotFound() {
        every { authSvc.isPasswordValid("alice", "pwd") } returns true
        every { quotaSvc.validate("X.Y", 1) } returns QuotaService.Result.ResourceNotFound

        val input = CheckAccessInput(
            login = "alice",
            password = "pwd",
            action = Action.read,
            resource = "X.Y",
            volume = 1
        )

        val result = usecase.execute(input)

        assertEquals(CheckAccessResult.ResourceNotFound, result)
    }

    @Test
    fun bad_volume_format_returns_BadFormat() {
        every { authSvc.isPasswordValid("alice", "pwd") } returns true
        every { quotaSvc.validate("A.B.C", -5) } returns QuotaService.Result.BadFormat

        val input = CheckAccessInput(
            login = "alice",
            password = "pwd",
            action = Action.read,
            resource = "A.B.C",
            volume = -5
        )

        val result = usecase.execute(input)

        assertEquals(CheckAccessResult.BadFormat, result)
    }

    @Test
    fun quota_exceeded_returns_VolumeExceeded() {
        every { authSvc.isPasswordValid("alice", "pwd") } returns true
        every { quotaSvc.validate("A.B.C", 40) } returns QuotaService.Result.VolumeExceeded

        val input = CheckAccessInput(
            login = "alice",
            password = "pwd",
            action = Action.read,
            resource = "A.B.C",
            volume = 40
        )

        val result = usecase.execute(input)

        assertEquals(CheckAccessResult.VolumeExceeded, result)
    }
}

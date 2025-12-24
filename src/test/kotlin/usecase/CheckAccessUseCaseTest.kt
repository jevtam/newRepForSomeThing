package usecase

import domain.model.Action
import domain.model.Resource
import domain.model.User
import domain.services.AuthService
import domain.services.AccessPolicy
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
    fun ok_returns_code_0() {
        val user = User("alice", "salt", "hash")
        val res = Resource("A.B.C", 30)

        every { authSvc.isPasswordValid("alice", "pwd") } returns true
        every { quotaSvc.validate("A.B.C", 10) } returns QuotaService.Result.Ok
        every { policy.isAllowed("alice", "A.B.C", Action.read) } returns true

        val input = CheckAccessInput("alice", "pwd", Action.read, "A.B.C", 10)
        val code = usecase.execute(input)

        assertEquals(0, code)
    }

    @Test
    fun wrong_password_returns_code_2() {
        val user = User("alice", "salt", "hash")

        every { authSvc.isPasswordValid("alice", "pwd") } returns false

        val input = CheckAccessInput("alice", "pwd", Action.read, "A.B.C", 1)
        val code = usecase.execute(input)

        assertEquals(2, code)
    }

    @Test
    fun resource_not_found_returns_code_6() {
        val user = User("alice", "s", "h")

        every { authSvc.isPasswordValid("alice", "pwd") } returns true
        every { quotaSvc.validate("X.Y", 1) } returns QuotaService.Result.ResourceNotFound

        val input = CheckAccessInput("alice", "pwd", Action.read, "X.Y", 1)
        val code = usecase.execute(input)

        assertEquals(6, code)
    }

    @Test
    fun bad_volume_format_returns_code_7() {
        val user = User("alice", "salt", "hash")

        every { authSvc.isPasswordValid("alice", "pwd") } returns true
        every { quotaSvc.validate("A.B.C", -5) } returns QuotaService.Result.BadFormat

        val input = CheckAccessInput("alice", "pwd", Action.read, "A.B.C", -5)
        val code = usecase.execute(input)

        assertEquals(7, code)
    }

    @Test
    fun quota_exceeded_returns_code_8() {
        val user = User("alice", "salt", "hash")

        every { authSvc.isPasswordValid("alice", "pwd") } returns true
        every { quotaSvc.validate("A.B.C", 40) } returns QuotaService.Result.VolumeExceeded

        val input = CheckAccessInput("alice", "pwd", Action.read, "A.B.C", 40)
        val code = usecase.execute(input)

        assertEquals(8, code)
    }
}
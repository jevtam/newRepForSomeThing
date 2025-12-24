package usecase

import domain.model.Action
import domain.services.AccessPolicy
import domain.services.AuthService
import domain.services.QuotaService

data class CheckAccessInput(
    val login: String,
    val password: String,
    val action: Action,
    val resource: String,
    val volume: Int
)

sealed interface CheckAccessResult {
    data object Ok : CheckAccessResult
    data object BadLoginOrPassword : CheckAccessResult
    data object NoAccess : CheckAccessResult
    data object ResourceNotFound : CheckAccessResult
    data object BadFormat : CheckAccessResult
    data object VolumeExceeded : CheckAccessResult
}

class CheckAccess(
    private val auth: AuthService,
    private val policy: AccessPolicy,
    private val quota: QuotaService
) {
    fun execute(input: CheckAccessInput): CheckAccessResult {
        if (!auth.isPasswordValid(input.login, input.password))
            return CheckAccessResult.BadLoginOrPassword

        when (val q = quota.validate(input.resource, input.volume)) {
            is QuotaService.Result.ResourceNotFound -> return CheckAccessResult.ResourceNotFound
            is QuotaService.Result.BadFormat       -> return CheckAccessResult.BadFormat
            is QuotaService.Result.VolumeExceeded  -> return CheckAccessResult.VolumeExceeded
            is QuotaService.Result.Ok -> {}
        }

        if (!policy.isAllowed(input.login, input.resource, input.action))
            return CheckAccessResult.NoAccess

        return CheckAccessResult.Ok
    }
}
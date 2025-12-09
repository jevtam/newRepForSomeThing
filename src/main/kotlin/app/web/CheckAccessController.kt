package app.web

import domain.model.Action
import org.springframework.web.bind.annotation.*
import usecase.CheckAccess
import usecase.CheckAccessInput
import usecase.CheckAccessResult

data class CheckAccessRequest(
    val login: String,
    val password: String,
    val action: String,
    val resource: String,
    val volume: Int
)

data class CheckAccessResponse(
    val code: Int
)

@RestController
@RequestMapping("/api")
class CheckAccessController(
    private val checkAccess: CheckAccess
) {

    @PostMapping("/check-access")
    fun check(@RequestBody req: CheckAccessRequest): CheckAccessResponse {
        val input = CheckAccessInput(
            login = req.login,
            password = req.password,
            action = Action.valueOf(req.action.lowercase()), // тут лучше сделать аккуратный маппинг
            resource = req.resource,
            volume = req.volume
        )

        val result = checkAccess.execute(input)

        val code = when (result) {
            CheckAccessResult.Ok -> 0
            CheckAccessResult.BadLoginOrPassword -> 2
            CheckAccessResult.ResourceNotFound -> 6
            CheckAccessResult.BadFormat -> 7
            CheckAccessResult.VolumeExceeded -> 8
            CheckAccessResult.NoAccess -> 5 // если у тебя такое есть
        }

        return CheckAccessResponse(code)
    }
}

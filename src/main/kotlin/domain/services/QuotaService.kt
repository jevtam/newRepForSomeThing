package domain.services
import domain.ports.ResourceRepository

class QuotaService(private val resources: ResourceRepository) {
    fun validate(resourcePath: String, volume: Int): Result {
        val res = resources.find(resourcePath) ?: return Result.ResourceNotFound
        if (volume < 0) return Result.BadFormat
        if (volume > res.maxVolume) return Result.VolumeExceeded
        return Result.Ok
    }
    sealed interface Result {
        data object Ok : Result
        data object BadFormat : Result
        data object ResourceNotFound : Result
        data object VolumeExceeded : Result
    }
}
package domain.services

import domain.model.Resource
import domain.ports.ResourceRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class QuotaServiceTest {

    private val repo: ResourceRepository = mockk()
    private val service = QuotaService(repo)

    @Test
    fun ok_when_volume_in_range() {
        every { repo.find("A.B.C") } returns Resource("A.B.C", 30)

        val result = service.validate("A.B.C", 10)

        assertEquals(QuotaService.Result.Ok, result)
    }

    @Test
    fun bad_format_when_volume_negative() {
        every { repo.find("A.B.C") } returns Resource("A.B.C", 30)

        val result = service.validate("A.B.C", -1)

        assertEquals(QuotaService.Result.BadFormat, result)
    }

    @Test
    fun resource_not_found() {
        every { repo.find("A.B.C") } returns null

        val result = service.validate("A.B.C", 10)

        assertEquals(QuotaService.Result.ResourceNotFound, result)
    }

    @Test
    fun volume_exceeded() {
        every { repo.find("A.B.C") } returns Resource("A.B.C", 30)

        val result = service.validate("A.B.C", 40)

        assertEquals(QuotaService.Result.VolumeExceeded, result)
    }
}
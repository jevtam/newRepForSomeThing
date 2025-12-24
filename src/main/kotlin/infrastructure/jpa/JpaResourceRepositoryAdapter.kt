package infrastructure.jpa

import data.repo.ResourceJpaRepository
import domain.model.Resource
import domain.ports.ResourceRepository
import org.springframework.stereotype.Repository

class JpaResourceRepositoryAdapter(
    private val repo: ResourceJpaRepository
) : ResourceRepository {

    override fun find(path: String): Resource? {
        val e = repo.findByPath(path) ?: return null
        return Resource(
            path = e.path,
            maxVolume = e.maxVolume
        )
    }
}

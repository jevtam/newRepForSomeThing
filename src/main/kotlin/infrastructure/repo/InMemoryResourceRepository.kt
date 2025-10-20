package infrastructure.repo
import domain.model.Resource
import domain.ports.ResourceRepository

class InMemoryResourceRepository : ResourceRepository {
    private val data = listOf(
        Resource("A", 100),
        Resource("A.B", 60),
        Resource("A.B.C", 30),
        Resource("A.B.C.f_d", 10),
        Resource("A.A8B", 80)
    ).associateBy { it.path }
    override fun find(path: String) = data[path]
}
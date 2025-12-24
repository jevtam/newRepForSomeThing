package domain.ports
import domain.model.Resource
interface ResourceRepository { fun find(path: String): Resource? }
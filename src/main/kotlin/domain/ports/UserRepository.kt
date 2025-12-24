package domain.ports
import domain.model.User
interface UserRepository { fun find(login: String): User? }
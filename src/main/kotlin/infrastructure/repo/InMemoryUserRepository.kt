package infrastructure.repo
import domain.model.User
import domain.ports.UserRepository

class InMemoryUserRepository : UserRepository {
    private val data = listOf(
        User("alice", "nan",    "a547590f99d5fd581b3ce00f83757bb2019ec16e54816418e1e932ddbd8afadc"),
        User("bob",   "pepper", "744a9101f7182a6ae0d978121ff74e33cac8d2832579c0637c1c37e9bbb6c065")
    ).associateBy { it.login }
    override fun find(login: String) = data[login]
}
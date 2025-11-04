package domain.model
data class User(val login: String, val salt: String, val passwordHashHex: String)
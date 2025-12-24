package data.entity

import jakarta.persistence.*

@Entity
@Table(name = "users")
class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false, unique = true, length = 64)
    var username: String = "",

    @Column(nullable = false, length = 64)
    var salt: String = "",

    @Column(name = "password_hash_hex", nullable = false, length = 64)
    var passwordHashHex: String = ""
)

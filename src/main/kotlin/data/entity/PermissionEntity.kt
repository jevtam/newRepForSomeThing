package data.entity

import jakarta.persistence.*


@Entity
@Table(
    name = "permissions",
    uniqueConstraints = [UniqueConstraint(name = "uk_perm", columnNames = ["user_id", "resource_path"])]
)
class PermissionEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: UserEntity? = null,

    @Column(name = "resource_path", nullable = false, length = 128)
    var resourcePath: String = "",

    @Column(name = "actions", nullable = false, length = 128)
    var actions: String = ""
)

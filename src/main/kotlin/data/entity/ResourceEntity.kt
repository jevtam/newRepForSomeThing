package data.entity

import jakarta.persistence.*

@Entity
@Table(name = "resources")
class ResourceEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false, unique = true, length = 128)
    var path: String = "",

    @Column(name = "max_volume", nullable = false)
    var maxVolume: Int = 0
)

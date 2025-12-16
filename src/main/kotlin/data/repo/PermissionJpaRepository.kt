package data.repo

import data.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface PermissionJpaRepository : JpaRepository<PermissionEntity, Long> {
    fun findAllByUserUsernameAndResourcePath(username: String, path: String): List<PermissionEntity>
}

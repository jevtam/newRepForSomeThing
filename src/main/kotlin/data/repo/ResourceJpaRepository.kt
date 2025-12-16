package data.repo

import data.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ResourceJpaRepository : JpaRepository<ResourceEntity, Long> {
    fun findByPath(path: String): ResourceEntity?
    fun existsByPath(path: String): Boolean   // если очень надо “existsBy...”
}

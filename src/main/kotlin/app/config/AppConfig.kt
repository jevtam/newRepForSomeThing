package app.config

import domain.ports.PermissionService
import domain.ports.ResourceRepository
import domain.ports.UserRepository
import domain.services.AccessPolicy
import domain.services.AuthService
import domain.services.QuotaService
import infrastructure.crypto.Sha256Hasher
import infrastructure.jpa.JpaPermissionServiceAdapter
import infrastructure.jpa.JpaResourceRepositoryAdapter
import infrastructure.jpa.JpaUserRepositoryAdapter
import data.repo.PermissionJpaRepository
import data.repo.ResourceJpaRepository
import data.repo.UserJpaRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import usecase.CheckAccess

@Configuration
class AppConfig {


    @Bean
    fun userRepositoryAdapter(repo: UserJpaRepository): UserRepository =
        JpaUserRepositoryAdapter(repo)

    @Bean
    fun resourceRepositoryAdapter(repo: ResourceJpaRepository): ResourceRepository =
        JpaResourceRepositoryAdapter(repo)

    @Bean
    fun permissionServiceAdapter(repo: PermissionJpaRepository): PermissionService =
        JpaPermissionServiceAdapter(repo)


    @Bean
    fun hasher() = Sha256Hasher()

    @Bean
    fun authService(users: UserRepository, hasher: Sha256Hasher) =
        AuthService(users, hasher)

    @Bean
    fun quotaService(resources: ResourceRepository) =
        QuotaService(resources)

    @Bean
    fun accessPolicy(perms: PermissionService) =
        AccessPolicy(perms)


    @Bean
    fun checkAccess(auth: AuthService, policy: AccessPolicy, quota: QuotaService) =
        CheckAccess(auth, policy, quota)
}

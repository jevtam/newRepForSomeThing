package app.config

import domain.ports.PermissionService
import domain.ports.ResourceRepository
import domain.ports.UserRepository
import domain.services.AccessPolicy
import domain.services.AuthService
import domain.services.QuotaService
import infrastructure.crypto.Sha256Hasher
import infrastructure.repo.InMemoryPermissionService
import infrastructure.repo.InMemoryResourceRepository
import infrastructure.repo.InMemoryUserRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AppConfig {

    @Bean
    fun userRepository(): UserRepository = InMemoryUserRepository()

    @Bean
    fun resourceRepository(): ResourceRepository = InMemoryResourceRepository()

    @Bean
    fun permissionService(): PermissionService =
        InMemoryPermissionService()

    @Bean
    fun hasher() = Sha256Hasher()

    @Bean
    fun authService(userRepository: UserRepository, hasher: Sha256Hasher) =
        AuthService(userRepository, hasher)

    @Bean
    fun quotaService(resourceRepository: ResourceRepository) =
        QuotaService(resourceRepository)

    @Bean
    fun accessPolicy(permissionService: PermissionService) =
        AccessPolicy(permissionService)
}

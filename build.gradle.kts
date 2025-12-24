plugins {
    kotlin("jvm") version "2.2.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3.6")
    testImplementation("io.mockk:mockk:1.13.12")
    testImplementation("org.junit.jupiter:junit-jupiter:5.11.0")

    // Kotlin stdlib/reflect, чтобы Maven и Gradle были согласованы (на всякий случай)
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.24")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.24")

    // Spring Boot Web — тут все нужные Spring-классы (контекст, веб, аннотации и т.п.)
    implementation("org.springframework.boot:spring-boot-starter-web:3.3.0")

    // SQLite-драйвер
    implementation("org.xerial:sqlite-jdbc:3.46.0.0")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}
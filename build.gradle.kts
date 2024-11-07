plugins {
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.spring") version "1.9.22"

    id("org.springframework.boot") version "2.7.18"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "test.task"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val easyRandomVersion = "4.0.0"

dependencies {
    // Spring Boot dependencies
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation(group = "org.springdoc", name = "springdoc-openapi-ui", version = "1.6.15")

    implementation("javax.annotation:javax.annotation-api")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

    // Kotlin dependencies
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation(group = "io.github.microutils", name = "kotlin-logging", version = "2.1.23")

    // PostgreSQL JDBC Driver
    implementation("org.postgresql:postgresql")

    // Flyway
    implementation("org.flywaydb:flyway-core")

    // TestContainers for PostgreSQL
    testImplementation("org.testcontainers:testcontainers:1.20.0")
    testImplementation("org.testcontainers:junit-jupiter:1.20.0")
    testImplementation("org.testcontainers:postgresql:1.20.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test") // Spring Boot Test
    testImplementation(group = "org.jeasy", name = "easy-random-core", version = easyRandomVersion)
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")

    // JDBC Template
    implementation("org.springframework:spring-jdbc")

    // Kotlin test dependencies
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}
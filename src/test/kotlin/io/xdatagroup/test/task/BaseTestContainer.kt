package io.xdatagroup.test.task

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class BaseTestContainer {

    companion object {
        private var pgContainer: PostgreSQLContainer<*> = PostgreSQLContainer<Nothing>("postgres:12.8")
            .apply {
                withUsername("task_user")
                withPassword("testPassword")
                withDatabaseName("tasks_db")
                withInitScript("sql/schema.sql")
            }

        @DynamicPropertySource
        @JvmStatic
        fun configureProperties(registry: DynamicPropertyRegistry) {
            registry.add("DB_URL") { pgContainer.jdbcUrl }
            registry.add("DB_USERNAME") { pgContainer.username }
            registry.add("DB_PASSWORD") { pgContainer.password }
            println("db_url: ${pgContainer.jdbcUrl}")
            println("username: ${pgContainer.username}")
            println("password: ${pgContainer.password}")
        }

        init {
            pgContainer.start()
        }
    }
}
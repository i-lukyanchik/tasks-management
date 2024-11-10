package io.xdatagroup.test.task.base

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class BaseTestContainer {

    @LocalServerPort
    var port: Int = 0

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    companion object {
        private var pgContainer: PostgreSQLContainer<*> = PostgreSQLContainer<Nothing>("postgres:12.8")
            .apply {
                withUsername("task_user")
                withPassword("testPassword")
                withDatabaseName("tasks_db")
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
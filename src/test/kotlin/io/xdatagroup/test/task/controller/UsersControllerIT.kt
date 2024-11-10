package io.xdatagroup.test.task.controller

import io.xdatagroup.test.task.base.BaseTestContainer
import io.xdatagroup.test.task.dto.UsersResponse
import java.util.Collections
import javax.annotation.PostConstruct
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType

internal class UsersControllerIT : BaseTestContainer() {

    private lateinit var getAllUsersEndpoint: String

    @PostConstruct
    fun init() {
        val commonPrefix = "http://localhost:$port/$USERS_REQUEST_MAPPING"
        getAllUsersEndpoint = commonPrefix
    }

    @Test
    fun `get all users success - hardcoded data`() {
        val users =
            restTemplate.exchange(
                getAllUsersEndpoint,
                HttpMethod.GET,
                HttpEntity(
                    null,
                    HttpHeaders().apply {
                        accept = Collections.singletonList(MediaType.APPLICATION_JSON)
                    }),
                UsersResponse::class.java
            )
        assertEquals(HttpStatus.OK, users.statusCode)
        assertEquals(1, users.body!!.content.size)
        assertEquals("testName", users.body!!.content[0].name)
    }
}
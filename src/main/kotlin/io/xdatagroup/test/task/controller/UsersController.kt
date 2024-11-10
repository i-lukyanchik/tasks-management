package io.xdatagroup.test.task.controller

import io.xdatagroup.test.task.api.UsersApi
import io.xdatagroup.test.task.dto.UsersResponse
import io.xdatagroup.test.task.service.UsersService
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

const val USERS_REQUEST_MAPPING = "/api/v1/users"

@RestController
@RequestMapping(USERS_REQUEST_MAPPING)
@Validated
class UsersController(
    private val usersService: UsersService
) : UsersApi {

    @GetMapping
    override fun getUsers(): ResponseEntity<UsersResponse> {
        return ResponseEntity.ok(usersService.getAllUsers())
    }
}
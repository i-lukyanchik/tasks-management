package io.xdatagroup.test.task.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import io.xdatagroup.test.task.dto.ErrorMessage
import io.xdatagroup.test.task.dto.UsersResponse
import org.springframework.http.ResponseEntity

@Tag(
    name = "Users API", description = "Endpoint for retrieving users"
)
fun interface UsersApi {

    @Operation(
        description = "Get all users",
        summary = "Get all users",
        method = "GET"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "OK",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(
                            implementation = UsersResponse::class,
                            name = "get-users"
                        )
                    )
                ]
            ),

            ApiResponse(
                responseCode = "400", description = "Bad request",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(
                            implementation = ErrorMessage::class,
                            name = "error-response"
                        )
                    )
                ]
            ),

            ApiResponse(
                responseCode = "500", description = "Internal server error",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(
                            implementation = ErrorMessage::class,
                            name = "error-response"
                        )
                    )
                ]
            )
        ]
    )
    fun getUsers() : ResponseEntity<UsersResponse>
}
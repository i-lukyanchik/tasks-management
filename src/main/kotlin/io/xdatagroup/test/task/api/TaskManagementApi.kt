package io.xdatagroup.test.task.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.Parameters
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import io.xdatagroup.test.task.dto.CreateTaskRequest
import io.xdatagroup.test.task.dto.ErrorMessage
import io.xdatagroup.test.task.dto.GenerateReportRequest
import io.xdatagroup.test.task.dto.ReportResponse
import io.xdatagroup.test.task.dto.SearchTasksRequest
import io.xdatagroup.test.task.dto.TaskResponse
import io.xdatagroup.test.task.dto.TasksResponse
import io.xdatagroup.test.task.dto.UpdateTaskRequest
import javax.validation.Valid


@Tag(
    name = "Task Management API", description = "Endpoints for managing tasks"
)
interface TaskManagementApi {

    @Operation(
        description = "Create task",
        summary = "Create task",
        method = "POST"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "OK",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(
                            implementation = TaskResponse::class,
                            name = "create-task"
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
    fun createTask(@Valid request: CreateTaskRequest): ResponseEntity<TaskResponse>

    @Operation(
        description = "Get task by id",
        summary = "Get task by id",
        method = "GET"
    )
    @Parameters(
        value = [
            Parameter(
                name = "taskId",
                required = true,
                description = "Identifier of task",
                `in` = ParameterIn.PATH
            )
        ]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "OK",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(
                            implementation = TaskResponse::class,
                            name = "get-task"
                        )
                    )
                ]
            ),

            ApiResponse(
                responseCode = "410", description = "Gone",
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
    fun getTask(taskId: Long): ResponseEntity<TaskResponse>

    @Operation(
        description = "Update task",
        summary = "Update task",
        method = "PUT"
    )
    @Parameters(
        value = [
            Parameter(
                name = "taskId",
                required = true,
                description = "Identifier of task",
                `in` = ParameterIn.PATH
            )
        ]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "204", description = "No Content", content = [Content()]
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
                responseCode = "410", description = "Gone",
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
    fun updateTask(taskId: Long, @Valid request: UpdateTaskRequest): ResponseEntity<Unit>

    @Operation(
        description = "Search tasks",
        summary = "Search tasks",
        method = "POST"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "OK",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(
                            implementation = TasksResponse::class,
                            name = "search-tasks"
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
    fun searchTasks(@Valid request: SearchTasksRequest): ResponseEntity<TasksResponse>

    @Operation(
        description = "Generate tasks report",
        summary = "Generate tasks report",
        method = "POST"
    )
    @RequestBody(
        description = "Report generation body", content = [Content(
            mediaType = "application/json",
            schema = Schema(
                implementation = GenerateReportRequest::class
            )
        )],
        required = true
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "OK",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(
                            implementation = TasksResponse::class,
                            name = "search-tasks"
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
    fun generateReport(@Valid request: GenerateReportRequest): ResponseEntity<ReportResponse>

}
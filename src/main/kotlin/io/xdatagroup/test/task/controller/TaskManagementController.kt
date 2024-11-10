package io.xdatagroup.test.task.controller

import io.xdatagroup.test.task.dto.CreateTaskRequest
import io.xdatagroup.test.task.dto.GenerateReportRequest
import io.xdatagroup.test.task.dto.ReportResponse
import io.xdatagroup.test.task.dto.SearchTasksRequest
import io.xdatagroup.test.task.dto.TaskResponse
import io.xdatagroup.test.task.dto.TasksResponse
import io.xdatagroup.test.task.dto.UpdateTaskRequest
import io.xdatagroup.test.task.service.TaskService
import javax.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import io.xdatagroup.test.task.api.TaskManagementApi
import org.springframework.validation.annotation.Validated

const val TASK_MANAGEMENT_REQUEST_MAPPING = "/api/v1/tasks"
const val SEARCH_TASKS = "/search"
const val REPORT_TASKS = "/reports"
const val BY_ID = "/{taskId}"

@RestController
@RequestMapping(TASK_MANAGEMENT_REQUEST_MAPPING)
@Validated
class TaskManagementController(
    private val taskService: TaskService
): TaskManagementApi {

    @PostMapping
    override fun createTask(@Valid @RequestBody request: CreateTaskRequest): ResponseEntity<TaskResponse> =
        ResponseEntity.ok(taskService.createTask(request))

    @GetMapping(BY_ID)
    override fun getTask(@PathVariable taskId: Long): ResponseEntity<TaskResponse> =
        ResponseEntity.ok(taskService.getTask(taskId))

    @PutMapping(BY_ID)
    override fun updateTask(@PathVariable taskId: Long, @Valid @RequestBody request: UpdateTaskRequest): ResponseEntity<Unit> {
        taskService.updateTask(taskId, request)
        return ResponseEntity.noContent().build()
    }

    @PostMapping(SEARCH_TASKS)
    override fun searchTasks(@Valid @RequestBody request: SearchTasksRequest): ResponseEntity<TasksResponse> =
        ResponseEntity.ok(taskService.searchTasks(request))

    @PostMapping(REPORT_TASKS)
    override fun generateReport(@Valid @RequestBody request: GenerateReportRequest): ResponseEntity<ReportResponse> =
        ResponseEntity.ok(taskService.generateReport(request))
}
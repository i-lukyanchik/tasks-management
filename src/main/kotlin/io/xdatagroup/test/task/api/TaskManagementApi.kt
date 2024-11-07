package io.xdatagroup.test.task.api

import org.springframework.http.ResponseEntity
import io.xdatagroup.test.task.dto.CreateTaskRequest
import io.xdatagroup.test.task.dto.GenerateReportRequest
import io.xdatagroup.test.task.dto.ReportResponse
import io.xdatagroup.test.task.dto.SearchTasksRequest
import io.xdatagroup.test.task.dto.TaskResponse
import io.xdatagroup.test.task.dto.TasksResponse
import io.xdatagroup.test.task.dto.UpdateTaskRequest

//todo add some api here, validate from localhost optional fields and required are displayed properly
interface TaskManagementApi { // todo in tests cover scenarios: 1) get tasks by member, 2) assignTaskToMember, 3) updateTaskStatus

    fun createTask(request: CreateTaskRequest): ResponseEntity<TaskResponse>

    fun getTask(taskId: Long): ResponseEntity<TaskResponse>

    fun updateTask(taskId: Long, request: UpdateTaskRequest): ResponseEntity<Unit>

    // todo active tasks for all, all tasks for admin with pagination
    fun searchTasks(request: SearchTasksRequest): ResponseEntity<TasksResponse>

    // todo maybe add comment about materialized view or smth like that (pre-aggregation)
    // todo add comment about option to store in s3 and making logic async
    fun generateReport(request: GenerateReportRequest): ResponseEntity<ReportResponse>

}
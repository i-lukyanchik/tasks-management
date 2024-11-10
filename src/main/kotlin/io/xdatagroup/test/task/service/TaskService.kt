package io.xdatagroup.test.task.service

import io.xdatagroup.test.task.dao.TaskDao
import io.xdatagroup.test.task.dto.CreateTaskRequest
import io.xdatagroup.test.task.dto.GenerateReportRequest
import io.xdatagroup.test.task.dto.ReportResponse
import io.xdatagroup.test.task.dto.SearchTasksRequest
import io.xdatagroup.test.task.dto.TaskResponse
import io.xdatagroup.test.task.dto.TasksResponse
import io.xdatagroup.test.task.dto.UpdateTaskRequest
import io.xdatagroup.test.task.enums.TaskStatus
import io.xdatagroup.test.task.exception.TaskAlreadyPresentException
import io.xdatagroup.test.task.exception.TaskNotFoundException
import java.time.Instant
import java.time.temporal.ChronoUnit
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

const val READ_TIMEOUT = 25
private val FINAL_TASK_STATUSES = setOf(TaskStatus.RELEASED, TaskStatus.CANCELLED)
const val MAX_ALLOWED_REPORTING_RANGE_IN_DAYS = 365

@Service
class TaskService(
    private val taskDao: TaskDao,
    private val authService: AuthService
) {

    @Transactional
    fun createTask(request: CreateTaskRequest): TaskResponse {
        validateRequest(request.title, request.dueDate)
        return taskDao.createTask(request, authService.getUserId())
    }

    @Transactional(readOnly = true, timeout = READ_TIMEOUT)
    fun getTask(taskId: Long): TaskResponse = taskDao.findTaskById(taskId) ?: throw TaskNotFoundException(taskId)

    @Transactional
    fun deleteTasks() = taskDao.deleteTasks()

    @Transactional
    fun updateTask(taskId: Long, request: UpdateTaskRequest) {
        validateRequest(request.title, request.dueDate)
        val task = getTask(taskId)
        require(task.status !in FINAL_TASK_STATUSES) { "Task must not be in final status" }
        taskDao.updateTask(taskId, request, authService.getUserId())
    }

    @Transactional(readOnly = true, timeout = READ_TIMEOUT)
    fun searchTasks(request: SearchTasksRequest): TasksResponse {
        require(!(request.taskStatus == null && request.memberId == null)) { "At least one of fields: taskStatus, memberId need to be specified" }
        return TasksResponse(taskDao.searchTasks(request.memberId, request.taskStatus))
    }

    @Transactional(readOnly = true, timeout = READ_TIMEOUT)
    fun generateReport(request: GenerateReportRequest): ReportResponse { // better to use materialized view or smth like pre-aggregation, also option to store in s3 and making logic async
        val startDate = request.startDate
        val endDate = request.endDate
        require(startDate.isBefore(endDate)) { "Start date must be less than end date" }
        require(ChronoUnit.DAYS.between(startDate, endDate) <= MAX_ALLOWED_REPORTING_RANGE_IN_DAYS) { "Max allowed days for generating report is $MAX_ALLOWED_REPORTING_RANGE_IN_DAYS" }
        val tasks = taskDao.getTasksByDateRange(startDate, endDate)
        val completedTasks = tasks.filter { it.status == TaskStatus.RELEASED }
        val completionRate = if (tasks.isEmpty()) 0.0 else (completedTasks.size.toDouble() / tasks.size * 100)
        val avgCompletionTime = if (completedTasks.isEmpty()) 0.0 else completedTasks.map { it.updatedAt!!.toEpochMilli().minus(it.createdAt.toEpochMilli()) }.average() / 1000
        return ReportResponse(tasks.size, completedTasks.size, completionRate, avgCompletionTime)
    }

    private fun validateRequest(title: String, dueDate: Instant?) {
        dueDate?.let {
            require(
                Instant.now().isBefore(dueDate)
            ) { "Due date must be in the future if provided" }
        }
        taskDao.findTaskByTitle(title)?.let { throw TaskAlreadyPresentException(title, it.id) }
    }
}
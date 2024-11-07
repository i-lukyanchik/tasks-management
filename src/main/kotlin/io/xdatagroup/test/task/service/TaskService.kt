package io.xdatagroup.test.task.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import io.xdatagroup.test.task.dao.TaskDao
import io.xdatagroup.test.task.dto.CreateTaskRequest
import io.xdatagroup.test.task.dto.GenerateReportRequest
import io.xdatagroup.test.task.dto.ReportResponse
import io.xdatagroup.test.task.dto.SearchTasksRequest
import io.xdatagroup.test.task.dto.TaskResponse
import io.xdatagroup.test.task.dto.TasksResponse
import io.xdatagroup.test.task.dto.UpdateTaskRequest
import io.xdatagroup.test.task.enums.TaskStatus
import io.xdatagroup.test.task.exception.TaskNotFoundException

const val READ_TIMEOUT = 25

@Service
class TaskService(
    private val taskDao: TaskDao,
    private val authService: AuthService
) {

    @Transactional
    fun createTask(request: CreateTaskRequest): TaskResponse {
        // todo validate already present by title
        return taskDao.createTask(request, authService.getUserId())
    }

    @Transactional(readOnly = true, timeout = READ_TIMEOUT)
    fun getTask(taskId: Long): TaskResponse = taskDao.findTask(taskId) ?: throw TaskNotFoundException(taskId)

    @Transactional
    fun updateTask(taskId: Long, request: UpdateTaskRequest) {
        getTask(taskId)
        taskDao.updateTask(taskId, request, authService.getUserId())
    }

    @Transactional(readOnly = true, timeout = READ_TIMEOUT)
    fun searchTasks(request: SearchTasksRequest): TasksResponse {
        require(!(request.taskStatus == null && request.memberId == null)) { "One of fields: taskStatus, memberId need to be specified" }
        return TasksResponse(taskDao.searchTasks(request.memberId, request.taskStatus))
    }

    @Transactional(readOnly = true, timeout = READ_TIMEOUT)
    fun generateReport(request: GenerateReportRequest): ReportResponse {
        // todo validate startDate less endDate + range not greater than let's say 1 month
        val tasks = taskDao.getTasksByDateRange(request.startDate, request.endDate)
        val completedTasks = tasks.filter { it.status == TaskStatus.RELEASED }
        val completionRate = if (tasks.isEmpty()) 0.0 else (completedTasks.size.toDouble() / tasks.size * 100)
        val avgCompletionTime = if (completedTasks.isEmpty()) 0.0 else completedTasks.map { it.updatedAt!!.toEpochMilli().minus(it.createdAt.toEpochMilli()) }.average() / 1000
        return ReportResponse(tasks.size, completedTasks.size, completionRate, avgCompletionTime)
    }

}
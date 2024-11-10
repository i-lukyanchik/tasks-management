package io.xdatagroup.test.task.service

import io.xdatagroup.test.task.dao.TaskDao
import io.xdatagroup.test.task.dto.CreateTaskRequest
import io.xdatagroup.test.task.dto.Format
import io.xdatagroup.test.task.dto.GenerateReportRequest
import io.xdatagroup.test.task.dto.SearchTasksRequest
import io.xdatagroup.test.task.dto.TaskResponse
import io.xdatagroup.test.task.dto.UpdateTaskRequest
import io.xdatagroup.test.task.enums.TaskStatus
import io.xdatagroup.test.task.exception.TaskAlreadyPresentException
import io.xdatagroup.test.task.tools.easyRandom
import io.xdatagroup.test.task.tools.minusDays
import io.xdatagroup.test.task.tools.plusDays
import java.time.Instant
import kotlin.test.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

internal class TaskServiceTest {

    private val taskDao: TaskDao = mock()
    private val authService = AuthService()
    private val taskService = TaskService(taskDao, authService)

    @Test
    fun `create task - same title already exists`() {
        val createTaskRequest = easyRandom.nextObject(CreateTaskRequest::class.java).copy(
            dueDate = Instant.now().plusDays(1)
        )
        whenever(taskDao.findTaskByTitle(any())).thenReturn(easyRandom.nextObject(TaskResponse::class.java))
        assertThrows<TaskAlreadyPresentException> { taskService.createTask(createTaskRequest) }
    }

    @Test
    fun `create task - due date must be in the future if provided`() {
        val createTaskRequest = easyRandom.nextObject(CreateTaskRequest::class.java).copy(
            dueDate = Instant.now().minusDays(1)
        )
        assertEquals(
            "Due date must be in the future if provided",
            assertThrows<IllegalArgumentException> { taskService.createTask(createTaskRequest) }.message
        )
    }

    @Test
    fun `update task - same title already exists`() {
        whenever(taskDao.findTaskByTitle(any())).thenReturn(easyRandom.nextObject(TaskResponse::class.java))
        assertThrows<TaskAlreadyPresentException> {
            taskService.updateTask(
                easyRandom.nextLong(), easyRandom.nextObject(UpdateTaskRequest::class.java).copy(
                    dueDate = Instant.now().plusDays(1)
                )
            )
        }
    }

    @Test
    fun `update task - due date must be in the future if provided`() {
        val updateTaskRequest = easyRandom.nextObject(UpdateTaskRequest::class.java).copy(
            dueDate = Instant.now().minusDays(1)
        )
        assertEquals(
            "Due date must be in the future if provided",
            assertThrows<IllegalArgumentException> {
                taskService.updateTask(
                    easyRandom.nextLong(),
                    updateTaskRequest
                )
            }.message
        )
    }

    @Test
    fun `update task - task must not be in final status`() {
        val updateTaskRequest = easyRandom.nextObject(UpdateTaskRequest::class.java).copy(
            dueDate = Instant.now().plusDays(1)
        )
        whenever(taskDao.findTaskById(any())).thenReturn(
            easyRandom.nextObject(TaskResponse::class.java).copy(
                status = TaskStatus.RELEASED
            )
        )
        assertEquals(
            "Task must not be in final status",
            assertThrows<IllegalArgumentException> {
                taskService.updateTask(
                    easyRandom.nextLong(),
                    updateTaskRequest
                )
            }.message
        )
    }

    @Test
    fun `generate report - validate max allowed reporting range in days`() {
        assertEquals(
            "Max allowed days for generating report is $MAX_ALLOWED_REPORTING_RANGE_IN_DAYS",
            assertThrows<IllegalArgumentException> {
                taskService.generateReport(
                    GenerateReportRequest(
                        startDate = Instant.now(),
                        endDate = Instant.now().plusDays(500),
                        Format.JSON
                    )
                )
            }.message)
    }

    @Test
    fun `generate report - startDate must be less than endDate`() {
        assertEquals(
            "Start date must be less than end date",
            assertThrows<IllegalArgumentException> {
                taskService.generateReport(
                    GenerateReportRequest(
                        startDate = Instant.now().plusDays(10),
                        endDate = Instant.now(),
                        Format.JSON
                    )
                )
            }.message)
    }

    @Test
    fun `search tasks - at least one of fields - taskStatus, memberId need to be specified for search`() {
        assertEquals(
            "At least one of fields: taskStatus, memberId need to be specified",
            assertThrows<IllegalArgumentException> {
                taskService.searchTasks(SearchTasksRequest(null, null))
            }.message)
    }

    @Test
    fun `search tasks - success if both params provided`() {
        whenever(taskDao.searchTasks(any(), any())).thenReturn(emptyList())

        taskService.searchTasks(SearchTasksRequest(1, TaskStatus.READY))

        verify(taskDao).searchTasks(any(), any())
    }
}
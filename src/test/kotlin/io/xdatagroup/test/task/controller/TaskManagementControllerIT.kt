package io.xdatagroup.test.task.controller

import io.xdatagroup.test.task.base.BaseTestContainer
import io.xdatagroup.test.task.dao.TeamMemberDao
import io.xdatagroup.test.task.dto.CreateTaskRequest
import io.xdatagroup.test.task.dto.ErrorMessage
import io.xdatagroup.test.task.dto.Format
import io.xdatagroup.test.task.dto.GenerateReportRequest
import io.xdatagroup.test.task.dto.ReportResponse
import io.xdatagroup.test.task.dto.SearchTasksRequest
import io.xdatagroup.test.task.dto.TaskResponse
import io.xdatagroup.test.task.dto.TasksResponse
import io.xdatagroup.test.task.dto.UpdateTaskRequest
import io.xdatagroup.test.task.enums.TaskStatus
import io.xdatagroup.test.task.service.AuthService
import io.xdatagroup.test.task.service.HARD_CODED_USER_ID
import io.xdatagroup.test.task.service.TaskService
import io.xdatagroup.test.task.tools.easyRandom
import io.xdatagroup.test.task.tools.minusDays
import io.xdatagroup.test.task.tools.plusDays
import java.time.Instant
import java.util.Collections
import javax.annotation.PostConstruct
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType

internal class TaskManagementControllerIT : BaseTestContainer() {

    @Autowired
    private lateinit var teamMemberDao: TeamMemberDao

    @Autowired
    private lateinit var taskService: TaskService

    @MockBean
    private lateinit var authService: AuthService

    private lateinit var searchTasksEndpoint: String
    private lateinit var getUpdateByIdEndpoint: String
    private lateinit var createTaskEndpoint: String
    private lateinit var generateReportEndpoint: String

    @PostConstruct
    fun init() {
        val commonPrefix = "http://localhost:$port/$TASK_MANAGEMENT_REQUEST_MAPPING"
        createTaskEndpoint = commonPrefix
        searchTasksEndpoint = "$commonPrefix$SEARCH_TASKS"
        getUpdateByIdEndpoint = "$commonPrefix$BY_ID"
        generateReportEndpoint = "$commonPrefix$REPORT_TASKS"
    }

    @BeforeEach
    fun setup() {
        taskService.deleteTasks()
        whenever(authService.getUserId()).thenReturn(teamMemberDao.findAnyMember()!!.id)
    }

    @Test
    fun `create task success`() {
        val createTaskRequest = randomCreateTaskRequest()
        val createResponse = sendCreateTaskRequest(createTaskRequest, TaskResponse::class.java)

        val body = createResponse.body!!
        assertEquals(HttpStatus.OK, createResponse.statusCode)
        assertNotNull(body.id)
        assertEquals(createTaskRequest.title, body.title)
        assertEquals(HARD_CODED_USER_ID, body.createdByMemberId)
        assertNotNull(body.createdAt)
    }

    @ParameterizedTest
    @ValueSource(ints = [-1, 101])
    fun `create task negative - priority must be between 1 and 100 inclusive`(value: Int) {
        val createTaskRequest = randomCreateTaskRequest().copy(
            priority = value
        )
        val createResponse = sendCreateTaskRequest(createTaskRequest, ErrorMessage::class.java)

        assertEquals(HttpStatus.BAD_REQUEST, createResponse.statusCode)
    }

    @ParameterizedTest
    @ValueSource(ints = [-1, 101])
    fun `update task negative - priority must be between 1 and 100 inclusive`(value: Int) {
        val createTaskRequest = randomCreateTaskRequest().copy(
            priority = 1
        )
        val createTaskResponse = sendCreateTaskRequest(createTaskRequest, TaskResponse::class.java)
        val updateTaskRequest = easyRandom.nextObject(UpdateTaskRequest::class.java).copy(
            assignedMemberId = null,
            dueDate = Instant.now().plusDays(1),
            priority = value
        )

        val updateResponse = sendUpdateRequest(updateTaskRequest, createTaskResponse.body!!.id, ErrorMessage::class.java)

        assertEquals(HttpStatus.BAD_REQUEST, updateResponse.statusCode)
    }

    @Test
    fun `update task not found`() {
        sendCreateTaskRequest(randomCreateTaskRequest(), TaskResponse::class.java)
        val updateTaskRequest = easyRandom.nextObject(UpdateTaskRequest::class.java).copy(
            assignedMemberId = null,
            dueDate = Instant.now().plusDays(1),
            priority = 1
        )
        val updateResponse = sendUpdateRequest(updateTaskRequest, easyRandom.nextLong(), ErrorMessage::class.java)
        assertEquals(HttpStatus.GONE, updateResponse.statusCode)
    }

    @Test
    fun `update task success`() {
        val createTaskRequest = randomCreateTaskRequest()
        val createTaskResponse = sendCreateTaskRequest(createTaskRequest, TaskResponse::class.java)
        val updateTaskRequest = easyRandom.nextObject(UpdateTaskRequest::class.java).copy(
            assignedMemberId = null,
            dueDate = Instant.now().plusDays(1),
            priority = 1
        )
        val updateResponse = sendUpdateRequest(updateTaskRequest, createTaskResponse.body!!.id, Unit::class.java)
        assertEquals(HttpStatus.NO_CONTENT, updateResponse.statusCode)
    }

    @Test
    fun `get task not found`() {
        val taskId = easyRandom.nextLong()
        val response = restTemplate.getForEntity(getUpdateByIdEndpoint, ErrorMessage::class.java, taskId)
        assertEquals(response.body!!.message, "Task with id = $taskId is not found")
        assertEquals(HttpStatus.GONE, response.statusCode)
    }

    @Test
    fun `get task success`() {
        val createTaskRequest = randomCreateTaskRequest()
        val createResponse = sendCreateTaskRequest(createTaskRequest, TaskResponse::class.java)

        val getResponse =
            restTemplate.getForEntity(getUpdateByIdEndpoint, TaskResponse::class.java, createResponse.body!!.id)
        assertEquals(getResponse.body!!.title, createResponse.body!!.title)
        assertEquals(HttpStatus.OK, getResponse.statusCode)
    }

    @Test
    fun `search tasks success found`() {
        val createTaskResponse = sendCreateTaskRequest(randomCreateTaskRequest(), TaskResponse::class.java)
        val searchTasksRequest = SearchTasksRequest(null, TaskStatus.NEW)
        val searchResponse = sendSearchRequest(searchTasksRequest)
        assertEquals(HttpStatus.OK, searchResponse.statusCode)
        assertEquals(1, searchResponse.body!!.content.size)
        assertEquals(createTaskResponse.body!!.title, searchResponse.body!!.content[0].title)
    }

    @ParameterizedTest
    @EnumSource(value = TaskStatus::class, names = ["NEW"], mode = EnumSource.Mode.EXCLUDE)
    fun `search tasks success empty`(taskStatus: TaskStatus) {
        val searchTasksRequest = SearchTasksRequest(null, taskStatus)
        val searchResponse =
            restTemplate.exchange(
                searchTasksEndpoint,
                HttpMethod.POST,
                HttpEntity(
                    searchTasksRequest,
                    HttpHeaders().apply {
                        accept = Collections.singletonList(MediaType.APPLICATION_JSON)
                        contentType = MediaType.APPLICATION_JSON
                    }),
                TasksResponse::class.java
            )
        assertEquals(HttpStatus.OK, searchResponse.statusCode)
        assertEquals(0, searchResponse.body!!.content.size)
    }

    @Test
    fun `generate report success not zero`() {
        val createTaskResponse = sendCreateTaskRequest(randomCreateTaskRequest(), TaskResponse::class.java)
        val updateTaskRequest = easyRandom.nextObject(UpdateTaskRequest::class.java).copy(
            assignedMemberId = null,
            dueDate = Instant.now().plusDays(1),
            priority = 1,
            status = TaskStatus.RELEASED
        )
        sendUpdateRequest(updateTaskRequest, createTaskResponse.body!!.id, Unit::class.java)

        val generateReportRequest =
            GenerateReportRequest(Instant.now().minusDays(3), Instant.now().plusDays(1), Format.JSON)
        val reportingResponse = sendReportingRequest(generateReportRequest)

        assertEquals(HttpStatus.OK, reportingResponse.statusCode)
        val reportResponse = reportingResponse.body!!
        assertEquals(1, reportResponse.totalTasks)
        assertEquals(1, reportResponse.completedTasks)
        assertEquals(100.0, reportResponse.completionRate)
        assertTrue(reportResponse.avgCompletionTimeInSeconds > 0)
    }

    @Test
    fun `generate report success zero`() {
        val generateReportRequest = GenerateReportRequest(Instant.now().minusDays(3), Instant.now(), Format.JSON)
        val reportingResponse = sendReportingRequest(generateReportRequest)
        assertEquals(HttpStatus.OK, reportingResponse!!.statusCode)
        val reportResponse = reportingResponse.body!!
        assertEquals(0, reportResponse.totalTasks)
        assertEquals(0, reportResponse.completedTasks)
        assertEquals(0.0, reportResponse.completionRate)
        assertEquals(0.0, reportResponse.avgCompletionTimeInSeconds)
    }

    private fun sendSearchRequest(searchTasksRequest: SearchTasksRequest) =
        restTemplate.exchange(
            searchTasksEndpoint,
            HttpMethod.POST,
            HttpEntity(
                searchTasksRequest,
                HttpHeaders().apply {
                    accept = Collections.singletonList(MediaType.APPLICATION_JSON)
                    contentType = MediaType.APPLICATION_JSON
                }),
            TasksResponse::class.java
        )

    private fun <T> sendUpdateRequest(
        updateTaskRequest: UpdateTaskRequest,
        taskId: Long,
        clazz: Class<T>
    ) =
        restTemplate.exchange(
            getUpdateByIdEndpoint,
            HttpMethod.PUT,
            HttpEntity(
                updateTaskRequest,
                HttpHeaders().apply {
                    contentType = MediaType.APPLICATION_JSON
                }),
            clazz, taskId
        )

    private fun <T> sendCreateTaskRequest(createTaskRequest: CreateTaskRequest, clazz: Class<T>) =
        restTemplate.exchange(
            createTaskEndpoint,
            HttpMethod.POST,
            HttpEntity(
                createTaskRequest,
                HttpHeaders().apply {
                    accept = Collections.singletonList(MediaType.APPLICATION_JSON)
                    contentType = MediaType.APPLICATION_JSON
                }),
            clazz
        )

    private fun randomCreateTaskRequest() = easyRandom.nextObject(CreateTaskRequest::class.java).copy(
        priority = 1,
        assignedMemberId = null,
        dueDate = Instant.now().plusDays(1)
    )

    private fun sendReportingRequest(generateReportRequest: GenerateReportRequest) =
        restTemplate.exchange(
            generateReportEndpoint,
            HttpMethod.POST,
            HttpEntity(
                generateReportRequest,
                HttpHeaders().apply {
                    accept = Collections.singletonList(MediaType.APPLICATION_JSON)
                    contentType = MediaType.APPLICATION_JSON
                }),
            ReportResponse::class.java
        )
}
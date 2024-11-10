package io.xdatagroup.test.task.dao

import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.stereotype.Component
import io.xdatagroup.test.task.dao.rowmapper.TaskRowMapper
import io.xdatagroup.test.task.dto.CreateTaskRequest
import io.xdatagroup.test.task.dto.TaskResponse
import io.xdatagroup.test.task.dto.UpdateTaskRequest
import io.xdatagroup.test.task.enums.TaskStatus
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

private const val SQL_INSERT = """
    insert into tasks (title, description, due_date, priority, assigned_member_id, status, created_at, created_by_member_id)
    values (:title, :description, :dueDate, :priority, :assignedMemberId, 'NEW', now(), :createdByMemberId)
    returning id, title, description, due_date, priority, assigned_member_id, status, created_at, created_by_member_id, updated_at, updated_by_member_id
"""

private const val SQL_GET_BY_ID = "select * from tasks where id = :taskId"

private const val SQL_GET_BY_TITLE = "select * from tasks where title = :title"

private const val SQL_DELETE_TASKS = "delete from tasks"

private const val SQL_UPDATE = """
    update tasks set title = :title, description = :description, due_date = :dueDate, priority = :priority, 
                 assigned_member_id = :assignedMemberId, status = :status, updated_at = now(), updated_by_member_id = :updatedByMemberId
"""

private const val SQL_REPORTING = "select * from tasks where created_at between :startDate and :endDate"

@Component
class TaskDao(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val taskRowMapper: TaskRowMapper
) {

    fun createTask(request: CreateTaskRequest, createdByUserId: Long): TaskResponse =
        jdbcTemplate.query(
            SQL_INSERT, mapOf(
            "title" to request.title,
            "description" to request.description,
            "dueDate" to request.dueDate?.toTimestamp(),
            "priority" to request.priority,
            "assignedMemberId" to request.assignedMemberId,
            "createdByMemberId" to createdByUserId
        ), taskRowMapper).single()

    fun findTaskByTitle(title: String) : TaskResponse? = try {
        jdbcTemplate.queryForObject(SQL_GET_BY_TITLE, mapOf("title" to title), taskRowMapper)
    }  catch (e: EmptyResultDataAccessException) {
        null
    }

    fun findTaskById(taskId: Long) : TaskResponse? = try {
        jdbcTemplate.queryForObject(SQL_GET_BY_ID, mapOf("taskId" to taskId), taskRowMapper)
    }  catch (e: EmptyResultDataAccessException) {
        null
    }

    fun deleteTasks() = jdbcTemplate.update(SQL_DELETE_TASKS, mapOf<String, Any>())

    fun updateTask(taskId: Long, request: UpdateTaskRequest, updatedByUserId: Long) {
        jdbcTemplate.update(
            SQL_UPDATE, mapOf(
            "title" to request.title,
            "description" to request.description,
            "dueDate" to request.dueDate?.toTimestamp(),
            "priority" to request.priority,
            "assignedMemberId" to request.assignedMemberId,
            "status" to request.status.name,
            "updatedByMemberId" to updatedByUserId))
    }

    fun searchTasks(assignedMemberId: Long?, taskStatus: TaskStatus?): List<TaskResponse> {
        val stringBuilder = StringBuilder("select * from tasks where true")
        if (assignedMemberId != null) {
            stringBuilder.append(" and assigned_member_id = :assignedMemberId")
        }
        if (taskStatus != null) {
            stringBuilder.append(" and status = :status")
        }
        return jdbcTemplate.query(stringBuilder.toString(), mapOf("assignedMemberId" to assignedMemberId, "status" to taskStatus?.name), taskRowMapper)
    }

    fun getTasksByDateRange(startDate: Instant, endDate: Instant): List<TaskResponse> =
        jdbcTemplate.query(SQL_REPORTING, mapOf("startDate" to startDate.toTimestamp(), "endDate" to endDate.toTimestamp()), taskRowMapper)


    fun Instant?.toTimestamp(): Timestamp {
        val localDateTime = LocalDateTime.ofInstant(this, ZoneOffset.UTC)
        return Timestamp.valueOf(localDateTime)
    }
}
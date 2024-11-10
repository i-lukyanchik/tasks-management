package io.xdatagroup.test.task.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.time.Instant
import io.xdatagroup.test.task.enums.TaskStatus
import javax.validation.constraints.Max
import javax.validation.constraints.Min

@Schema(description = "Body to update task, title must be unique")
data class UpdateTaskRequest(
    @field:Schema(required = true, description = "Task status", example = "IN_PROGRESS")
    val status: TaskStatus,
    @field:Schema(required = true, description = "Title, must be unique", example = "testTitle")
    val title: String,
    @field:Schema(required = true, description = "Description", example = "Test description")
    val description: String,
    @field:Schema(required = false, description = "Due date, if provided must be in the future")
    val dueDate: Instant?,
    @field:Schema(required = false, description = "Priority", example = "15", minimum = "1", maximum = "100")
    @field:Min(1)
    @field:Max(100)
    val priority: Int?,
    @field:Schema(required = false, description = "Assigned member id", example = "1")
    val assignedMemberId: Long?
)
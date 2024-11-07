package io.xdatagroup.test.task.dto

import java.time.Instant
import io.xdatagroup.test.task.enums.TaskStatus

data class UpdateTaskRequest(
    val status: TaskStatus, // todo validate can't be returned to prev status
    val title: String,
    val description: String,
    val dueDate: Instant?,
    val priority: Int?,
    val assignedMemberId: Long?
)
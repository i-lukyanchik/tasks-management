package io.xdatagroup.test.task.dto

import java.time.Instant
import io.xdatagroup.test.task.enums.TaskStatus

data class TaskResponse(
    val id: Long = 0,
    val title: String,
    val description: String,
    val dueDate: Instant?,
    val priority: Int?,
    val assignedMemberId: Long?,
    val status: TaskStatus = TaskStatus.NEW,
    val createdAt: Instant,
    val createdByMemberId: Long,
    val updatedAt: Instant?,
    val updatedByMemberId: Long?,
)
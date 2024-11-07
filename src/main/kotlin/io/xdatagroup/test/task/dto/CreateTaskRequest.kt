package io.xdatagroup.test.task.dto

import java.time.Instant

data class CreateTaskRequest(
    val title: String,
    val description: String,
    val dueDate: Instant?,
    val priority: Int?,
    val assignedMemberId: Long?
)

package io.xdatagroup.test.task.dto

import io.swagger.v3.oas.annotations.media.Schema
import io.xdatagroup.test.task.enums.TaskStatus

@Schema(description = "Body to search tasks. At least one of fields: taskStatus, memberId need to be specified")
data class SearchTasksRequest(
    @field:Schema(required = false, description = "Member Id", example = "1")
    val memberId: Long?,
    @field:Schema(required = false, description = "Task status", example = "NEW")
    val taskStatus: TaskStatus?
)
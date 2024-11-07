package io.xdatagroup.test.task.dto

import io.xdatagroup.test.task.enums.TaskStatus

data class SearchTasksRequest(
    val memberId: Long?,
    val taskStatus: TaskStatus?
)
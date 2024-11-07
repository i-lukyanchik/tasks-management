package io.xdatagroup.test.task.dto

data class ReportResponse(
    val totalTasks: Int,
    val completedTasks: Int,
    val completionRate: Double,
    val avgCompletionTimeInSeconds: Double
)
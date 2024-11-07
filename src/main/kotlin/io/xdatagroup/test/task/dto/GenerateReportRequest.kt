package io.xdatagroup.test.task.dto

import java.time.Instant

data class GenerateReportRequest(
    val startDate: Instant,
    val endDate: Instant,
    val format: Format
)

enum class Format {
    JSON // can be extended later
}
package io.xdatagroup.test.task.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.time.Instant

@Schema(description = "Body to generate report, 1 year (365 days) is max allowed range between start date and end date")
data class GenerateReportRequest(
    @field:Schema(required = true, description = "Start date")
    val startDate: Instant,
    @field:Schema(required = true, description = "End date")
    val endDate: Instant,
    @field:Schema(required = true, description = "Report format")
    val format: Format
)

enum class Format {
    JSON // can be extended later
}
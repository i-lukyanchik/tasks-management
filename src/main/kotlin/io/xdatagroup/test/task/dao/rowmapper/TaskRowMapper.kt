package io.xdatagroup.test.task.dao.rowmapper

import java.sql.ResultSet
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import io.xdatagroup.test.task.dto.TaskResponse
import io.xdatagroup.test.task.enums.TaskStatus

@Component
class TaskRowMapper: RowMapper<TaskResponse> {

    override fun mapRow(rs: ResultSet, rowNum: Int): TaskResponse =
        TaskResponse(
            id = rs.getLong("id"),
            title = rs.getString("title"),
            description = rs.getString("description"),
            dueDate = rs.getTimestamp("due_date")?.toInstant(),
            priority = rs.getInt("priority"),
            assignedMemberId = rs.getLong("assigned_member_id"),
            status = TaskStatus.valueOf(rs.getString("status")),
            createdAt = rs.getTimestamp("created_at").toInstant(),
            createdByMemberId = rs.getLong("created_by_member_id"),
            updatedAt = rs.getTimestamp("updated_at")?.toInstant(),
            updatedByMemberId = rs.getLong("updated_by_member_id")
        )
}
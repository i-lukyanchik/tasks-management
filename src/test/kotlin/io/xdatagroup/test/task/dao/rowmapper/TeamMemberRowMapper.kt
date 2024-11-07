package io.xdatagroup.test.task.dao.rowmapper

import io.xdatagroup.test.task.dto.TeamMember
import java.sql.ResultSet
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component

@Component
class TeamMemberRowMapper: RowMapper<TeamMember> {

    override fun mapRow(rs: ResultSet, rowNum: Int): TeamMember =
        TeamMember(
            id = rs.getLong("id"),
            name = rs.getString("name")
        )
}
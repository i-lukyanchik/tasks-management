package io.xdatagroup.test.task.dao

import io.xdatagroup.test.task.dao.rowmapper.TeamMemberRowMapper
import io.xdatagroup.test.task.dto.TeamMember
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component

const val SQL_FIND_ANY = "select * from team_members limit :limit"

@Component
class TeamMemberDao(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val teamMemberRowMapper: TeamMemberRowMapper
) {

    fun findAnyMember(): TeamMember? = try {
        jdbcTemplate.queryForObject(SQL_FIND_ANY, mapOf("limit" to 1), teamMemberRowMapper)
    } catch (e: EmptyResultDataAccessException) {
        null
    }
}
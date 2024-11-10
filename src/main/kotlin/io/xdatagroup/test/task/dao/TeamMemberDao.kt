package io.xdatagroup.test.task.dao

import io.xdatagroup.test.task.dao.rowmapper.TeamMemberRowMapper
import io.xdatagroup.test.task.dto.TeamMember
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component

private const val SQL_FIND_ALL = "select * from team_members"
private const val SQL_FIND_ANY = "$SQL_FIND_ALL limit 1"

@Component
class TeamMemberDao(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val teamMemberRowMapper: TeamMemberRowMapper
) {

    fun findMembers(): List<TeamMember> = jdbcTemplate.query(SQL_FIND_ALL, mapOf<String, Any>(), teamMemberRowMapper)

    fun findAnyMember(): TeamMember? = try {
        jdbcTemplate.queryForObject(SQL_FIND_ANY, mapOf<String, Any>(), teamMemberRowMapper)
    } catch (e: EmptyResultDataAccessException) {
        null
    }
}
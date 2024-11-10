package io.xdatagroup.test.task.service

import io.xdatagroup.test.task.dao.TeamMemberDao
import io.xdatagroup.test.task.dto.UsersResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UsersService(
    private val teamMemberDao: TeamMemberDao
) {

    @Transactional(readOnly = true, timeout = READ_TIMEOUT)
    fun getAllUsers(): UsersResponse = UsersResponse(teamMemberDao.findMembers())
}
package io.xdatagroup.test.task.service

import org.springframework.stereotype.Service

const val HARD_CODED_USER_ID = 1L

@Service
class AuthService {

    fun getUserId() = HARD_CODED_USER_ID // in real impl we would take it from JWT token like SecurityContextHolder.getContext().authentication.principal
}
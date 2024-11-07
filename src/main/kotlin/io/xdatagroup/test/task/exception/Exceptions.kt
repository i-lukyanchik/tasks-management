package io.xdatagroup.test.task.exception

class TaskNotFoundException(val taskId: Long) : RuntimeException() {
    override val message: String
        get() = "Task with id = $taskId is not found"
}
package io.xdatagroup.test.task.exception

class TaskNotFoundException(private val taskId: Long) : RuntimeException() {
    override val message: String
        get() = "Task with id = $taskId is not found"
}

class TaskAlreadyPresentException(private val title: String, private val taskId: Long) : RuntimeException() {
    override val message: String
        get() = "Task with title = $title already present for task with id = $taskId"
}
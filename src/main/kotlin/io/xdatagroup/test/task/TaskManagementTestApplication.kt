package io.xdatagroup.test.task

import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder

@SpringBootApplication
class TaskManagementTestApplication

@Suppress("SpreadOperator")
fun main(args: Array<String>) {
    SpringApplicationBuilder(TaskManagementTestApplication::class.java)
        .bannerMode(Banner.Mode.OFF)
        .run(*args)
}

package io.xdatagroup.test.task.tools

import java.time.Instant
import java.time.temporal.ChronoUnit

fun Instant.minusDays(days: Int): Instant = this.minus(days.toLong(), ChronoUnit.DAYS)

fun Instant.plusDays(days: Int): Instant = this.plus(days.toLong(), ChronoUnit.DAYS)
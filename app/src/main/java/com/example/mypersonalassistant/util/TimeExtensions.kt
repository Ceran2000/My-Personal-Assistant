package com.example.mypersonalassistant.util

import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

fun ZonedDateTime.toStartOfDay(): ZonedDateTime = this.withHour(0).truncatedTo(ChronoUnit.HOURS)

fun ZonedDateTime.toMillis() = this.toInstant().toEpochMilli()

fun ZonedDateTime.toFormattedString(): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    return this.format(formatter)
}

fun Long.toZonedDateTime(zoneId: ZoneId = ZoneId.systemDefault()): ZonedDateTime {
    val instant = Instant.ofEpochMilli(this)
    val utcZonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.of("UTC"))
    return utcZonedDateTime.withZoneSameLocal(zoneId)
}
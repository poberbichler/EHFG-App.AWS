package org.ehfg.app.lambda.session

import com.fasterxml.jackson.annotation.JsonIgnore

data class Day(
    val description: String,
    val sessions: List<Session>
)

data class Session(
    val id: String,
    val name: String,
    val description: String,
    val location: String,
    val code: String,
    val speakers: List<String>,
    val startTime: Long,
    val endTime: Long,
    val startTimeUtc: String,
    val endTimeUtc: String,
    @JsonIgnore
    val day: String
)
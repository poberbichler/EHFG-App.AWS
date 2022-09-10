package org.ehfg.app.lambda.common

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

@JsonIgnoreProperties(value = ["lat", "lng"])
data class Event(
    val eventname: String,
    val eventid: String,
    val subtitle: String?,
    val headline: String?,
    val date: String,
    val start: String,
    val end: String,
    val location: String,
    val description: String,
    val eventcategory: String,
    val eventshortid: String,
    val speakers: List<Speaker>
) {
    val startTimestamp: ZonedDateTime
        get() = LocalDate.parse(date)
            .atTime(LocalTime.parse(start))
            .atZone(ZoneId.of("CET"))

    val endTimestamp: ZonedDateTime
        get() = LocalDate.parse(date)
            .atTime(LocalTime.parse(end))
            .atZone(ZoneId.of("CET"))
}

data class Speaker(
    val speaker: String,
    val speakerid: String,
    val organisation: String,
    val image: String?
) {
    val firstName: String
        get() = speaker.split(",")[1].trim()

    val lastName: String
        get() = speaker.split(",")[0].trim()
}
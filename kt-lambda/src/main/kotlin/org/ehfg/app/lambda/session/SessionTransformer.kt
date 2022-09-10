package org.ehfg.app.lambda.session

import org.ehfg.app.lambda.common.Event
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.format.DateTimeFormatter

@Component
class SessionTransformer {
    val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    companion object {
        val DAYS: Map<String, String> = linkedMapOf(
            Pair("2022-09-26", "Day 1"),
            Pair("2022-09-27", "Day 2"),
            Pair("2022-09-28", "Day 3"),
            Pair("2022-09-29", "Day 4"),
        )
    }

    fun transform(input: List<Event>): Map<String, Day> {
        logger.info("transforming input...")

        val sessionsByDay = input
            .filter { DAYS.containsKey(it.date) }
            .map { toSession(it) }
            .groupBy { it.day }

        val result = LinkedHashMap<String, Day>()
        DAYS.forEach { (date, description) ->
            result[date] = Day(description = description, sessions = sessionsByDay.getOrDefault(date, emptyList()))
        }

        return result
    }

    private fun toSession(input: Event) = Session(
        id = input.eventid,
        name = input.eventname,
        description = input.description,
        location = input.location,
        code = input.eventshortid,
        day = input.date,
        speakers = input.speakers.map { it.speakerid },
        startTime = input.startTimestamp.toInstant().toEpochMilli(),
        endTime = input.endTimestamp.toInstant().toEpochMilli(),
        startTimeUtc = input.startTimestamp.format(DateTimeFormatter.ISO_INSTANT),
        endTimeUtc = input.endTimestamp.format(DateTimeFormatter.ISO_INSTANT),
    )
}
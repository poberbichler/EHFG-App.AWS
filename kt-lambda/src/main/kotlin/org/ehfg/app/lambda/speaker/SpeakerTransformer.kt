package org.ehfg.app.lambda.speaker

import org.ehfg.app.lambda.common.Event
import org.ehfg.app.lambda.common.Speaker
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class SpeakerTransformer {
    val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    fun transform(events: List<Event>): List<EventSpeaker> {
        logger.info("transforming input...")

        return events
            .flatMap { it.speakers }
            .map { toSpeaker(it) }
            .distinctBy { it.id }
    }

    private fun toSpeaker(it: Speaker) = EventSpeaker(
        id = it.speakerid,
        firstName = it.firstName,
        lastName = it.lastName,
        description = it.organisation,
        imageUrl = it.image ?: "default"
    )
}
package org.ehfg.app.lambda.speaker

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.function.Consumer

@Component
class SpeakerUpdate : Consumer<Any> {
    private val log = LoggerFactory.getLogger(this.javaClass)

    override fun accept(bla: Any) {
        log.info("updating speakers... {} - {}", bla, bla.javaClass);
    }
}
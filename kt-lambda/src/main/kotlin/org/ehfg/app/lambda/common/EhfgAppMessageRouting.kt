package org.ehfg.app.lambda.common

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.cloud.function.context.MessageRoutingCallback
import org.springframework.cloud.function.context.MessageRoutingCallback.FunctionRoutingResult
import org.springframework.messaging.Message
import org.springframework.stereotype.Component

@Component
class EhfgAppMessageRouting(val objectMapper: ObjectMapper) : MessageRoutingCallback {
    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    override fun routingResult(message: Message<*>): FunctionRoutingResult {
        logger.info("received {} - {}", message, message.javaClass)

        return try {
            val payload = this.objectMapper.readValue(message.payload as ByteArray, Payload::class.java)
            FunctionRoutingResult(payload.definition ?: "twitter")
        } catch (ex: Exception) {
            logger.warn("an error happened...", ex)
            FunctionRoutingResult("twitter")
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class Payload(@JsonProperty("spring.cloud.function.definition") val definition: String?)
}
package org.ehfg.app.lambda.common

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers

@Component
class ProgramDownloader(val objectMapper: ObjectMapper) {
    val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    fun download(): List<Event> {

        val request = HttpRequest.newBuilder().GET()
            .uri(URI.create("https://www.ehfg.org/programme.json?year=2022"))
            .build()

        logger.info("fetching data from {}", "https://www.ehfg.org/programme.json?year=2022")

        return HttpClient.newHttpClient()
            .send(request, BodyHandlers.ofString())
            .body()
            // the backend doesn't return proper json, so we have to fix it...
            .replace(
                "Organised by Gesundheit Österreich GmbH (Austrian National Public Health Institute) in the framework of the Austrian federal \"Agenda Health Promotion\"",
                """Organised by Gesundheit Österreich GmbH (Austrian National Public Health Institute) in the framework of the Austrian federal \"Agenda Health Promotion\""""
            )
            .run { objectMapper.readValue(this, object : TypeReference<List<Event>>() {}) }
    }
}
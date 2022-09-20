package org.ehfg.app.lambda.common

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class ProgramDownloaderTest {
    @Autowired
    lateinit var programDownloader: ProgramDownloader

    @Test
    fun shouldDownload() {
        val eventList = this.programDownloader.downloadAndConvert()
        assertThat(eventList).isNotEmpty

        eventList.single { it.eventshortid == "S4" }
            .also { session ->
                assertThat(session).isNotNull
                assertThat(session.eventname).isEqualTo("The health impacts of the climate crisis ")
                assertThat(session.subtitle).isEqualTo("New tools to support European action ")
                assertThat(session.eventid).isEqualTo("2183")
                assertThat(session.eventcategory).isEqualTo("Session")
                assertThat(session.eventshortid).isEqualTo("S4")
                assertThat(session.date).isEqualTo("2022-09-27")
                assertThat(session.location).isEqualTo("Kursaal")
                assertThat(session.headline).isNotBlank
                assertThat(session.description).isNotBlank
                assertThat(session.speakers).hasSize(10)
            }
    }

    @Test
    fun handleEscapedCharacters() {
        assertThat(this.programDownloader.download())
            .doesNotContain("&#")
    }
}
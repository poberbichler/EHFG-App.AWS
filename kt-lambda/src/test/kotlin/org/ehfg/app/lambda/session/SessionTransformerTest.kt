package org.ehfg.app.lambda.session

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.ehfg.app.lambda.common.Event
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.ClassPathResource

@SpringBootTest
internal class SessionTransformerTest {
    @Autowired
    lateinit var sessionTransformer: SessionTransformer

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Test
    fun alwaysCreateDays() {
        assertThat(this.sessionTransformer.transform(emptyList()))
            .containsOnlyKeys(SessionTransformer.DAYS.keys)
    }

    @Test
    fun fromJson() {
        this.objectMapper.readValue(
            ClassPathResource("sample-events.json").file,
            object : TypeReference<List<Event>>() {})
            .run { sessionTransformer.transform(this) }
            .also { result ->
                assertThat(result).isNotEmpty;
                assertThat(result.keys).containsAll(SessionTransformer.DAYS.keys)
                assertThat(result["2022-09-26"]!!.sessions).hasSize(4)
                assertThat(result["2022-09-27"]!!.sessions).hasSize(13)
                assertThat(result["2022-09-28"]!!.sessions).hasSize(14)
                assertThat(result["2022-09-29"]!!.sessions).hasSize(5)

                result["2022-09-27"]!!.sessions
                    .single { it.code == "S5" }
                    .also { session ->
                        assertThat(session).isNotNull
                        assertThat(session.code).isEqualTo("S5")
                        assertThat(session.description).startsWith("Can the European Health Union be real and tangible")
                        assertThat(session.location).isEqualTo("Conference Centre")
                        assertThat(session.speakers).containsExactly("2241", "2242", "2243", "1529")
                        assertThat(session.id).isEqualTo("2139")
                        assertThat(session.name).isEqualTo("Reducing health inequalities: cancer care as a blueprint")
                        assertThat(session.startTime).isEqualTo(1664262000000L)
                        assertThat(session.endTime).isEqualTo(1664267400000L)
                        assertThat(session.startTimeUtc).isEqualTo("2022-09-27T07:00:00Z")
                        assertThat(session.endTimeUtc).isEqualTo("2022-09-27T08:30:00Z")
                    }
            }
    }
}
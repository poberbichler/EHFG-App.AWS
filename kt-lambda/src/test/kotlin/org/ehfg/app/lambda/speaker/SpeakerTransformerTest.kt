package org.ehfg.app.lambda.speaker

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.ehfg.app.lambda.common.Event
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.ClassPathResource

@SpringBootTest
internal class SpeakerTransformerTest {
    @Autowired
    lateinit var speakerTransformer: SpeakerTransformer

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Test
    fun convertEmptyList() {
        assertThat(this.speakerTransformer.transform(emptyList())).isEmpty()
    }

    @Test
    fun fromJson() {
        this.objectMapper.readValue(
            ClassPathResource("sample-events.json").file,
            object : TypeReference<List<Event>>() {})
            .run { speakerTransformer.transform(this) }
            .also { result ->
                assertThat(result).hasSize(125)
                result
                    .single { it.id == "2270" }
                    .also { speaker ->
                        assertThat(speaker.id).isEqualTo("2270")
                        assertThat(speaker.firstName).isEqualTo("Carla")
                        assertThat(speaker.lastName).isEqualTo("Maia")
                        assertThat(speaker.description).isEqualTo("Institute of Hygiene and Tropical Medicine")
                        assertThat(speaker.fullName).isEqualTo("Carla Maia")
                        assertThat(speaker.imageUrl).isEqualTo("https://www.ehfg.org/fileadmin/_processed_/9/4/csm_Maia_Carla_7a8bfef62a.jpeg")
                    }

                result
                    .single { it.id == "2261" }
                    .also { speaker ->
                        assertThat(speaker.id).isEqualTo("2261")
                        assertThat(speaker.firstName).isEqualTo("Silvana")
                        assertThat(speaker.lastName).isEqualTo("Di Sabatino")
                        assertThat(speaker.description).isEqualTo("Department of Physics and Astronomy")
                        assertThat(speaker.fullName).isEqualTo("Silvana Di Sabatino")
                        assertThat(speaker.imageUrl).isEqualTo("default")
                    }
            }
    }
}
package org.ehfg.app.search

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest
import org.springframework.core.io.ClassPathResource
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.client.MockRestServiceServer

import static org.assertj.core.api.Assertions.assertThat
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess

/**
 * @author patrick
 * @since 07.2017
 */
@RunWith(SpringRunner.class)
@RestClientTest(SearchServiceImpl.class)
class SearchServiceImplTest {
    @Autowired
    SearchServiceImpl searchService

    @Autowired
    MockRestServiceServer server

    @Value('${endpoint.url.session}')
    String sessionEndpointUrl
    @Value('${endpoint.url.speaker}')
    String speakerEndpointUrl

    @Before
    void setup() {
        this.server.expect(requestTo(this.sessionEndpointUrl))
                .andRespond(withSuccess(new ClassPathResource("sessions.json"), MediaType.APPLICATION_JSON))

        this.server.expect(requestTo(this.speakerEndpointUrl))
                .andRespond(withSuccess(new ClassPathResource("speakers.json"), MediaType.APPLICATION_JSON))
    }

    @Test
    void shouldReturnEmptyResultForNonsense() {
        def result = searchService.search("umpalumpa")
        assertThat(result.hasAnyResult()).isFalse()
    }

    @Test
    void shouldBuildIndexByUsingBothEndpoints() {
        def result = searchService.search("helmut")
        assertThat(result.tweets).isEmpty()
        assertThat(result.hasAnyResult()).isTrue()
        assertThat(result[SearchResultType.SPEAKER]).hasSize(1)
        assertThat(result[SearchResultType.SESSION]).hasSize(5)
    }
}

package org.ehfg.app.twitter

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpMethod
import org.springframework.social.twitter.api.Twitter
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.client.MockRestServiceServer

import java.time.LocalDateTime
import java.time.Month

import static org.springframework.test.web.client.match.MockRestRequestMatchers.*
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess
/**
 * @author patrick
 * @since 07.2017
 */
@RestClientTest(RedirectingStreamListener.class)
@RunWith(SpringRunner.class)
class RedirectingStreamListenerTest {
    @Autowired
    RedirectingStreamListener streamListener

    @Autowired
    MockRestServiceServer server

    @MockBean
    Twitter twitterTemplate

    @Value('${endpoint.url.twitter}')
    String endpointUrl

    @Test
    void receiveTweet() {
        def tweet = TweetMappingTest.createTweet(LocalDateTime.of(2017, Month.MAY, 5, 14, 00))

        this.server.expect(requestTo(this.endpointUrl))
                .andExpect(method(HttpMethod.POST))
                .andExpect(jsonPath("tweet").exists())
                .andExpect(jsonPath("tweet.id").value(123))
                .andExpect(jsonPath("tweet.creationDate").value(1493992800000))
                .andRespond(withSuccess())

        streamListener.onTweet(tweet)
        server.verify()
    }
}

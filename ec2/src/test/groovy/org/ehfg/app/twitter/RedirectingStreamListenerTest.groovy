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
        def tweet = TweetMappingTest.createTweet(LocalDateTime.parse("2017-05-05T14:00:00"))

        this.server.expect(requestTo(this.endpointUrl))
                .andExpect(method(HttpMethod.POST))
                .andExpect(jsonPath("tweet").exists())
                .andExpect(jsonPath("tweet.id").value(123))
                .andExpect(jsonPath("tweet.profileImage").value("https://pbs.twimg.com/profile_images/855124102021074944/FsJ1Cum5_normal.jpg"))
                .andExpect(jsonPath("tweet.nickName").value("p_oberbichler"))
                .andExpect(jsonPath("tweet.fullName").value("Patrick Oberbichler"))
                .andExpect(jsonPath("tweet.timestamp").value(1493992800000))
                .andExpect(jsonPath("tweet.retweetId").doesNotExist())
                .andExpect(jsonPath("tweet.retweet").value(false))
                .andRespond(withSuccess())

        streamListener.onTweet(tweet)
        server.verify()
    }

    @Test
    void receiveRetweet() {
        def tweet = TweetMappingTest.createTweet(LocalDateTime.parse("2017-05-05T14:00:00"))
        tweet.retweetedStatus = TweetMappingTest.createTweet(111)

        this.server.expect(requestTo(this.endpointUrl))
                .andExpect(method(HttpMethod.POST))
                .andExpect(jsonPath("tweet").exists())
                .andExpect(jsonPath("tweet.id").value(123))
                .andExpect(jsonPath("tweet.timestamp").value(1493992800000))
                .andExpect(jsonPath("tweet.retweetId").value(111))
                .andExpect(jsonPath("tweet.retweet").value(true))
                .andRespond(withSuccess())

        streamListener.onTweet(tweet)
        server.verify()
    }
}

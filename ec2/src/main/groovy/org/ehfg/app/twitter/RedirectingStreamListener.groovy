package org.ehfg.app.twitter

import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.social.twitter.api.StreamDeleteEvent
import org.springframework.social.twitter.api.StreamListener
import org.springframework.social.twitter.api.StreamWarningEvent
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
/**
 * @author patrick
 * @since 07.2017
 */
@Slf4j
@Service
class RedirectingStreamListener implements StreamListener {
    private final RestTemplate restTemplate
    private final String endpointUrl

    @Autowired
    RedirectingStreamListener(RestTemplateBuilder restTemplateBuilder,
                              @Value('${endpoint.url.twitter}') String endpointUrl) {
        this.restTemplate = restTemplateBuilder.build()
        this.endpointUrl = endpointUrl
    }

    @Override
    void onTweet(org.springframework.social.twitter.api.Tweet tweet) {
        log.info("received tweet [{}]", tweet)

        restTemplate.postForObject(this.endpointUrl, [tweet: new Tweet(tweet)], Void.class)
    }

    @Override
    void onDelete(StreamDeleteEvent deleteEvent) {
        log.info("received deleteEvent [{}]", deleteEvent)
    }

    @Override
    void onLimit(int numberOfLimitedTweets) {
        log.info("received limit [{}]", numberOfLimitedTweets)

    }

    @Override
    void onWarning(StreamWarningEvent warningEvent) {
        log.info("received warningEvent [{}]", warningEvent)
    }
}
package org.ehfg.app.twitter

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.social.TwitterProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.social.twitter.api.Twitter
import org.springframework.social.twitter.api.impl.TwitterTemplate

/**
 * @author patrick
 * @since 07.2017
 */
@Configuration
class TwitterConfig {
    @Bean
    Twitter twitter(TwitterProperties twitterProperties,
                    @Value('${twitter.access.token}') String accessToken,
                    @Value('${twitter.access.secret}') String accessTokenSecret) {
        return new TwitterTemplate(twitterProperties.appId,
                twitterProperties.appSecret,
                accessToken, accessTokenSecret);
    }
}

package org.ehfg.app.twitter

import org.junit.Test
import org.springframework.social.twitter.api.Entities
import org.springframework.social.twitter.api.HashTagEntity
import org.springframework.social.twitter.api.TwitterProfile
import org.springframework.social.twitter.api.UrlEntity

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime

import static org.assertj.core.api.Assertions.assertThat

/**
 * @author patrick
 * @since 07.2017
 */
class TweetMappingTest {

    @Test
    void transformSpringSocialTweet() {
        org.springframework.social.twitter.api.Tweet sourceTweet = createTweet()

        def targetTweet = new Tweet(sourceTweet)
        targetTweet.with {
            assertThat(id).isEqualTo(sourceTweet.id)
            assertThat(message).isEqualTo(sourceTweet.unmodifiedText)
            assertThat(creationDate).isEqualTo(ZonedDateTime.ofInstant(sourceTweet.createdAt.toInstant(), ZoneId.systemDefault()))
            assertThat(hashtag).isEqualTo("EHFG2017")
            assertThat(formattedMesssage).isEqualTo('Hallo Welt <span class="hashtag">#EHFG201</span> <span class="hashtag">#EHFG2017</span> ' +
                    '<a href="#" onclick="window.open("https://github.com/poberbichler/playground-spring-session/blob/master/src/test/java/", "_blank")>github.com/poberbichler/p...</a>')

            assertThat(retweet).isFalse()
            assertThat(retweetedBy).isEmpty()

            author.with {
                assertThat(id).isEqualTo(sourceTweet.fromUserId)
                assertThat(fullName).isEqualTo(sourceTweet.user.name)
                assertThat(nickName).isEqualTo(sourceTweet.user.screenName)
                assertThat(profileImage).isEqualTo("https://pbs.twimg.com/profile_images/855124102021074944/FsJ1Cum5_normal.jpg")
            }
        }
    }

    static org.springframework.social.twitter.api.Tweet createTweet() {
        return createTweet(LocalDateTime.now())
    }

    static org.springframework.social.twitter.api.Tweet createTweet(LocalDateTime createdAt) {
        def sourceTweet = new org.springframework.social.twitter.api.Tweet(
                123L, "Hallo Welt #EHFG201 #EHFG2017 https://t.co/SHrGE6dZE0", Date.from(createdAt.toInstant(ZoneOffset.UTC)),
                "p_oberbichler", "https://pbs.twimg.com/profile_images/855124102021074944/FsJ1Cum5_normal.jpg",
                0L, 215640018, "DE",
                '<a href="http://twitter.com" rel="nofllow">Twitter Web Client<a/>')
        sourceTweet.setEntities(new Entities([new UrlEntity("github.com/poberbichler/p...",
                "https://github.com/poberbichler/playground-spring-session/blob/master/src/test/java/",
                "https://t.co/SHrGE6dZE0", null)],
                [new HashTagEntity("EHFG201", null), new HashTagEntity("EHFG2017", null)],
                [], []))
        sourceTweet.user = new TwitterProfile(215640018L, "p_oberbichler",
                "Patrick Oberbichler", null, "http://pbs.twimg.com/profile_images/855124102021074944/FsJ1Cum5_normal.jpg",
                "description", "location", new Date())
        return sourceTweet
    }
}

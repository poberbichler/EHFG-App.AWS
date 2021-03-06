package org.ehfg.app.twitter

import org.junit.Test
import org.springframework.social.twitter.api.Entities
import org.springframework.social.twitter.api.HashTagEntity
import org.springframework.social.twitter.api.TwitterProfile
import org.springframework.social.twitter.api.UrlEntity

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

import static org.assertj.core.api.Assertions.assertThat
/**
 * @author patrick
 * @since 07.2017
 */
class TweetMappingTest {

    @Test
    void transformSpringSocialTweet() {
        org.springframework.social.twitter.api.Tweet sourceTweet = createTweet(123)

        def targetTweet = new Tweet(sourceTweet)
        targetTweet.with {
            assertThat(id).isEqualTo(sourceTweet.idStr)
            assertThat(message).isEqualTo(sourceTweet.unmodifiedText)
            assertThat(creationDate).isEqualTo(LocalDateTime.ofInstant(sourceTweet.createdAt.toInstant(), ZoneId.of("GMT+2")).atZone(ZoneId.of("UTC")))
            assertThat(hashtag).isEqualTo("EHFG2017")
            assertThat(formattedMesssage).isEqualTo('Hallo Welt <span class="hashtag">#EHFG201</span> <span class="hashtag">#EHFG2017</span> ' +
                    '<a href="#" onclick="window.open("https://github.com/poberbichler/playground-spring-session/blob/master/src/test/java/", "_blank")>github.com/poberbichler/p...</a>')

            assertThat(retweet).isFalse()
            assertThat(retweetedBy).isEmpty()
            assertThat(retweetId).isNull()

            author.with {
                assertThat(id).isEqualTo(sourceTweet.fromUserId.toString())
                assertThat(fullName).isEqualTo(sourceTweet.user.name)
                assertThat(nickName).isEqualTo(sourceTweet.user.screenName)
                assertThat(profileImage).isEqualTo("https://pbs.twimg.com/profile_images/855124102021074944/FsJ1Cum5_normal.jpg")
            }
        }
    }

    @Test
    void transformRetweet() {
        def tweet = createTweet(111)
        tweet.retweetedStatus = createTweet(123)

        def targetTweet = new Tweet(tweet)
        targetTweet.with {
            assertThat(id).is(111)
            assertThat(retweet).isTrue()
            assertThat(retweetId).is("123")
        }
    }

    static org.springframework.social.twitter.api.Tweet createTweet(LocalDateTime dateTime) {
        return createTweet(dateTime, 123)
    }

    static org.springframework.social.twitter.api.Tweet createTweet(long id) {
        return createTweet(LocalDateTime.now(), id)
    }

    static org.springframework.social.twitter.api.Tweet createTweet(LocalDateTime createdAt, long id) {
        def sourceTweet = new org.springframework.social.twitter.api.Tweet(
                id, "Hallo Welt #EHFG201 #EHFG2017 https://t.co/SHrGE6dZE0", Date.from(createdAt.toInstant(ZoneOffset.UTC)),
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

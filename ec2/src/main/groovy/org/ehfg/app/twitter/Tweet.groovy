package org.ehfg.app.twitter

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonUnwrapped
import groovy.transform.ToString

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

import static org.springframework.util.StringUtils.hasText
/**
 * @author patrick
 * @since 06.2017
 */
@ToString
class Tweet {
    String id

    String message
    @JsonProperty("timestamp")
    ZonedDateTime creationDate
    String hashtag
    String formattedMesssage

    boolean retweet = false
    String retweetId
    List<String> retweetedBy = new LinkedList<>()

    @JsonUnwrapped
    TwitterUser author

    Tweet(org.springframework.social.twitter.api.Tweet source) {
        this.id = source.idStr
        this.message = source.unmodifiedText
        this.creationDate = LocalDateTime.ofInstant(source.createdAt.toInstant(), ZoneId.of("GMT+2"))
                .atZone(ZoneId.of("UTC"))
        this.retweet = source.retweet
        this.hashtag = findHashtag(source)
        this.formattedMesssage = createFormattedMessage(source)

        this.retweetId = source.retweetedStatus?.id

        source.user.with {
            this.author = new TwitterUser(id: id,
                    fullName: name,
                    nickName: screenName,
                    profileImage: profileImageUrl.replaceFirst("http:", "https:"))
        }
    }

    private static String findHashtag(org.springframework.social.twitter.api.Tweet tweet) {
        return tweet.entities.hashTags.find { it.text ==~ /EHFG\d{4}/ }?.text
    }

    private static String createFormattedMessage(org.springframework.social.twitter.api.Tweet source) {
        def message = source.unmodifiedText.replaceAll(/#\w+/,) { "<span class=\"hashtag\">$it</span>" }
        source.entities.urls.each {
            if (hasText(it.url)) {
                message = message.replace(it.url, "<a href=\"#\" onclick=\"window.open('${it.expandedUrl}', '_blank')\">${it.displayUrl}</a>")
            }
        }

        return message
    }
}

class TwitterUser {
    @JsonIgnore
    String id

    String fullName
    String nickName
    String profileImage
}
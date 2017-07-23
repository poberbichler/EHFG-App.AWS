package org.ehfg.app.twitter

import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.ToString

import java.time.ZoneId
import java.time.ZonedDateTime

/**
 * @author patrick
 * @since 06.2017
 */
@ToString
class Tweet {
    long id

    String message
    @JsonProperty("timestamp")
    ZonedDateTime creationDate
    String hashtag
    String formattedMesssage

    boolean retweet = false
    List<String> retweetedBy = new LinkedList<>()

    TwitterUser author

    Tweet(org.springframework.social.twitter.api.Tweet source) {
        this.id = source.id
        this.message = source.unmodifiedText
        this.creationDate = ZonedDateTime.ofInstant(source.createdAt.toInstant(), ZoneId.systemDefault())
        this.retweet = source.retweet
        this.hashtag = findHashtag(source)
        this.formattedMesssage = createFormattedMessage(source)

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
            message = message.replace(it.url, "<a href=\"#\" onclick=\"window.open(\"${it.expandedUrl}\", \"_blank\")>${it.displayUrl}</a>")
        }

        return message
    }
}

class TwitterUser {
    long id

    String fullName
    String nickName
    String profileImage
}
package org.ehfg.app.lambda.twitter

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.*
import kotlin.math.floor

class TweetInput(
    val pageId: Int?,
    val tweetId: String?,
    val tweet: Tweet?
)

class TweetPage(
    val data: List<Tweet>,
    val currentPage: Int,
    val morePages: Boolean,
    val maxPages: Int,
    val currentHashtag: String
) {
    companion object {
        fun of(tweets: Deque<Tweet>, pageId: Int, pageSize: Int = 20): TweetPage {
            val pageCount = floor(tweets.size / pageSize.toFloat()).toInt()
            return TweetPage(
                data = tweets.chunked(pageSize)[pageId],
                currentPage = pageId,
                maxPages = pageCount,
                morePages = pageId != pageCount,
                currentHashtag = "#EHFG2022"
            )
        }
    }
}

class RemainingTweets {
    companion object {
        fun of(tweets: Deque<Tweet>, tweetId: String) = tweets.takeWhile { it.id != tweetId }
    }
}

@JsonIgnoreProperties("hashtag")
data class Tweet(
    val id: String,
    val message: String,
    val formattedMesssage: String,
    val timestamp: Long,
    val retweet: Boolean,
    val retweetedBy: MutableList<String>,
    val fullName: String,
    val nickName: String,
    val profileImage: String,
    val retweetId: String?
)

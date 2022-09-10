package org.ehfg.app.lambda.twitter

import java.util.*
import kotlin.math.floor

class TweetInput(
    val pageId: Int?,
    val tweetId: String?,
    val tweet: InputTweet?
)

class TweetPage(
    val data: List<StoredTweet>,
    val currentPage: Int,
    val morePages: Boolean,
    val maxPages: Int,
    val currentHashtag: String
) {
    companion object {
        fun of(tweets: Deque<StoredTweet>, pageId: Int, pageSize: Int = 20): TweetPage {
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
        fun of(tweets: Deque<StoredTweet>, tweetId: String) = tweets.takeWhile { it.id != tweetId }
    }
}

data class InputTweet(
    val id: String,
    val message: String,
    val timestamp: Long,
    val retweet: Boolean,
    val retweetedBy: MutableList<String>,
    val author: Author,
    val retweetId: String?
)

data class Author(
    val id: String,
    val fullName: String,
    val nickName: String,
    val profileImage: String,
)

data class StoredTweet(
    val id: String,
    val fullName: String,
    val nickName: String,
    val message: String,
    val profileImage: String,
    val timestamp: Long,
    val retweet: Boolean,
    val retweetedBy: MutableList<String>,
) {

}
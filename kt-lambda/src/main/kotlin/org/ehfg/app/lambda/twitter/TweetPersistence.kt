package org.ehfg.app.lambda.twitter

import org.ehfg.app.lambda.common.S3Uploader
import org.springframework.stereotype.Service
import java.util.*

@Service
class TweetPersistence(val s3Uploader: S3Uploader) {
    fun persist(tweets: Deque<StoredTweet>, tweet: InputTweet): String {
        tweets.addFirst(toStoredTweet(tweet))

        if (tweet.retweetId != null) {
            val retweet = tweets
                .singleOrNull { it.id == tweet.retweetId }
            retweet?.retweetedBy?.add(tweet.author.nickName)
        }

        this.s3Uploader.upload("tweets.json", tweets)
        return "ok"
    }

    private fun toStoredTweet(input: InputTweet): StoredTweet {
        return StoredTweet(
            id = input.id,
            message = input.message,
            timestamp = input.timestamp,
            retweet = input.retweet,
            retweetedBy = input.retweetedBy,
            fullName = input.author.fullName,
            nickName = input.author.nickName,
            profileImage = input.author.profileImage,
        )
    }
}
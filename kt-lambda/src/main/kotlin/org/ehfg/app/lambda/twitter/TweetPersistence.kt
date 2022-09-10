package org.ehfg.app.lambda.twitter

import org.ehfg.app.lambda.common.S3Uploader
import org.springframework.stereotype.Service
import java.util.*

@Service
class TweetPersistence(val s3Uploader: S3Uploader) {
    fun persist(tweets: Deque<Tweet>, tweet: Tweet): String {
        tweets.addFirst(tweet)

        if (tweet.retweetId != null) {
            tweets.single { it.id == tweet.retweetId }?.retweetedBy?.add(tweet.nickName)
        }

        this.s3Uploader.upload("tweets.json", tweets)
        return "ok"
    }
}
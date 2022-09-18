package org.ehfg.app.lambda.twitter

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.ObjectProvider
import org.springframework.stereotype.Service
import software.amazon.awssdk.core.sync.ResponseTransformer
import software.amazon.awssdk.services.s3.S3Client
import java.util.*

@Service
class TweetFetcher(
    val s3Client: ObjectProvider<S3Client>,
    val objectMapper: ObjectMapper
) {
    fun fetchTweets(): Deque<Tweet> {
        return this.s3Client.getObject()
            .getObject({ it.bucket("ehfg-app").key("tweets.json") }, ResponseTransformer.toBytes())
            .asUtf8String()
            .run { objectMapper.readValue(this, object : TypeReference<LinkedList<Tweet>>() {}) }
    }
}
package org.ehfg.app.lambda.twitter

import org.springframework.stereotype.Component
import java.util.*
import java.util.function.Function

@Component
class TwitterFunctionality(
    val tweetFetcher: TweetFetcher,
    val tweetPersistence: TweetPersistence
) : Function<TweetInput, Any> {

    private val PAGE_SIZE = 20

    // TODO: input type
    override fun apply(input: TweetInput): Any {
        val tweets = tweetFetcher.fetchTweets()
        return when {
            input.tweet != null -> tweetPersistence.persist(tweets, input.tweet)
            input.pageId != null -> findByPage(tweets, input.pageId)
            input.tweetId != null -> findNewerTweets(tweets, input.tweetId)
            else -> "unknown execution"
        }
    }

    private fun findByPage(tweets: Deque<StoredTweet>, pageId: Int) = TweetPage.of(tweets, pageId, PAGE_SIZE)

    private fun findNewerTweets(tweets: Deque<StoredTweet>, tweetId: String) = RemainingTweets.of(tweets, tweetId)
}
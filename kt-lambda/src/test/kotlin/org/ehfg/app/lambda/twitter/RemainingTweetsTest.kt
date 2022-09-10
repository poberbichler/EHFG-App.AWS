package org.ehfg.app.lambda.twitter

import com.fasterxml.jackson.core.type.TypeReference
import org.assertj.core.api.Assertions.assertThat
import org.ehfg.app.lambda.common.CommonConfig
import org.junit.jupiter.api.Test
import org.springframework.core.io.ClassPathResource
import java.util.*

internal class RemainingTweetsTest {
    @Test
    fun fetchLatest() {
        assertThat(RemainingTweets.of(fetchTweets(), "1182608763007713280"))
            .singleElement()
    }

    @Test
    fun fetchNone() {
        assertThat(RemainingTweets.of(fetchTweets(), "1182615793198080001"))
            .isEmpty()
    }

    @Test
    fun fetchFew() {
        assertThat(RemainingTweets.of(fetchTweets(), "1182562333383708672"))
            .hasSize(10)
    }

    private fun fetchTweets(): Deque<Tweet> {
        return CommonConfig().objectMapper()
            .readValue(
                ClassPathResource("sample-tweets.json").file,
                object : TypeReference<LinkedList<Tweet>>() {})
    }
}
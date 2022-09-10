package org.ehfg.app.lambda.twitter

import com.fasterxml.jackson.core.type.TypeReference
import org.assertj.core.api.Assertions.assertThat
import org.ehfg.app.lambda.common.CommonConfig
import org.junit.jupiter.api.Test
import org.springframework.core.io.ClassPathResource
import java.util.*

internal class TweetPageTest {
    @Test
    fun firstPage() {
        val page = TweetPage.of(fetchTweets(), 0, pageSize = 11)
        assertThat(page).isNotNull
        assertThat(page.data).hasSize(11)
        assertThat(page.currentPage).isEqualTo(0)
        assertThat(page.maxPages).isEqualTo(2)
        assertThat(page.morePages).isTrue
    }

    @Test
    fun secondPage() {
        val page = TweetPage.of(fetchTweets(), 1, pageSize = 11)
        assertThat(page).isNotNull
        assertThat(page.data).hasSize(11)
        assertThat(page.currentPage).isEqualTo(1)
        assertThat(page.maxPages).isEqualTo(2)
        assertThat(page.morePages).isTrue
    }

    @Test
    fun thirdPage() {
        val page = TweetPage.of(fetchTweets(), 2, pageSize = 11)
        assertThat(page).isNotNull
        assertThat(page.data).hasSize(8)
        assertThat(page.currentPage).isEqualTo(2)
        assertThat(page.maxPages).isEqualTo(2)
        assertThat(page.morePages).isFalse
    }

    private fun fetchTweets(): Deque<Tweet> {
        return CommonConfig().objectMapper()
            .readValue(
                ClassPathResource("sample-tweets.json").file,
                object : TypeReference<LinkedList<Tweet>>() {})
    }
}
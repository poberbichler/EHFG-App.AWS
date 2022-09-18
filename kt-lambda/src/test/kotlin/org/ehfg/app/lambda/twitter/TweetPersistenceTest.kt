package org.ehfg.app.lambda.twitter

import com.fasterxml.jackson.core.type.TypeReference
import org.assertj.core.api.Assertions.assertThat
import org.ehfg.app.lambda.common.CommonConfig
import org.ehfg.app.lambda.common.S3Uploader
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.ObjectProvider
import org.springframework.core.io.ClassPathResource
import org.testcontainers.containers.localstack.LocalStackContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import java.util.*

@Testcontainers
internal class TweetPersistenceTest {
    @Container
    val localStackContainer = LocalStackContainer(DockerImageName.parse("localstack/localstack"))
        .withServices(LocalStackContainer.Service.S3)!!

    @Test
    fun normalTweet() {
        val s3Client = S3Client.builder()
            .credentialsProvider(getStaticCredentials())
            .region(Region.of(localStackContainer.region))
            .endpointOverride(localStackContainer.getEndpointOverride(LocalStackContainer.Service.S3))
            .build()

        s3Client.createBucket { it.bucket("ehfg-app") }

        val objectMapper = CommonConfig().objectMapper()
        val tweet = objectMapper.readValue(ClassPathResource("sample-tweet.json").file, Tweet::class.java)

        TweetPersistence(S3Uploader(asObjectProvider(s3Client), objectMapper))
            .persist(fetchTweets(), tweet)

        TweetFetcher(asObjectProvider(s3Client), objectMapper).fetchTweets()
            .also { allTweets ->
                assertThat(allTweets).hasSize(31)
                allTweets.first().also { tweet ->
                    assertThat(tweet).isNotNull
                    assertThat(tweet.id).isEqualTo("12345")
                    assertThat(tweet.fullName).isEqualTo("Dorli Kahr-Gottlieb")
                    assertThat(tweet.nickName).isEqualTo("DorliKahr")
                    assertThat(tweet.message).isEqualTo("Congratulations again, @BathTR!@GasteinForum is excited to support your important work! #tobaccocontrol #monitoring… https://t.co/UIlMQfUMoI")
                    assertThat(tweet.formattedMesssage).isEqualTo("Congratulations again, @BathTR!@GasteinForum is excited to support your important work! <span class=\"hashtag\">#tobaccocontrol</span> <span class=\"hashtag\">#monitoring</span>… <a href=\"#\" onclick=\"window.open('https://twitter.com/i/web/status/1182602598597177344', '_blank')\">twitter.com/i/web/status/1…</a>")
                    assertThat(tweet.profileImage).isEqualTo("https://pbs.twimg.com/profile_images/1109071236511031296/MBdJkZtL_normal.jpg")
                    assertThat(tweet.timestamp).isEqualTo(1570796605000)
                    assertThat(tweet.retweet).isEqualTo(false)
                    assertThat(tweet.retweetedBy).isEmpty()
                }
            }
    }

    @Test
    fun retweet() {
        val s3Client = S3Client.builder()
            .credentialsProvider(getStaticCredentials())
            .region(Region.of(localStackContainer.region))
            .endpointOverride(localStackContainer.getEndpointOverride(LocalStackContainer.Service.S3))
            .build()

        s3Client.createBucket { it.bucket("ehfg-app") }

        val objectMapper = CommonConfig().objectMapper()
        val tweet = objectMapper.readValue(ClassPathResource("sample-retweet.json").file, Tweet::class.java)

        TweetPersistence(S3Uploader(asObjectProvider(s3Client), objectMapper))
            .persist(fetchTweets(), tweet)

        TweetFetcher(asObjectProvider(s3Client), objectMapper).fetchTweets()
            .also { allTweets ->
                assertThat(allTweets).hasSize(31)

                assertThat(allTweets.first.retweet).isTrue

                allTweets
                    .single { it.id == "1182412016222457858" }
                    .also { retweetedTweet ->
                        assertThat(retweetedTweet).isNotNull
                        assertThat(retweetedTweet.retweetedBy).contains("the-bilb")
                    }
            }
    }

    private fun getStaticCredentials(): StaticCredentialsProvider =
        StaticCredentialsProvider.create(
            AwsBasicCredentials.create(
                localStackContainer.accessKey,
                localStackContainer.secretKey
            )
        )

    private fun asObjectProvider(s3Client: S3Client) = object : ObjectProvider<S3Client> {
        override fun getObject(vararg args: Any?): S3Client {
            return s3Client;
        }

        override fun getObject(): S3Client {
            return s3Client;
        }

        override fun getIfAvailable(): S3Client {
            return s3Client;
        }

        override fun getIfUnique(): S3Client {
            return s3Client;
        }
    }

    private fun fetchTweets(): Deque<Tweet> {
        return CommonConfig().objectMapper()
            .readValue(
                ClassPathResource("sample-tweets.json").file,
                object : TypeReference<LinkedList<Tweet>>() {})
    }
}
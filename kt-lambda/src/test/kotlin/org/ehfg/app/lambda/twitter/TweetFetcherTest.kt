package org.ehfg.app.lambda.twitter

import org.assertj.core.api.Assertions.assertThat
import org.ehfg.app.lambda.common.CommonConfig
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

@Testcontainers
internal class TweetFetcherTest {
    @Container
    val localStackContainer = LocalStackContainer(DockerImageName.parse("localstack/localstack"))
        .withServices(LocalStackContainer.Service.S3)!!

    @Test
    fun fetchTweets() {
        val s3Client = S3Client.builder()
            .credentialsProvider(getStaticCredentials())
            .region(Region.of(localStackContainer.region))
            .endpointOverride(localStackContainer.getEndpointOverride(LocalStackContainer.Service.S3))
            .build()

        s3Client.createBucket { it.bucket("ehfg-app") }
        s3Client.putObject(
            { it.bucket("ehfg-app").key("tweets.json") },
            ClassPathResource("sample-tweets.json").file.toPath()
        )

        val tweets = TweetFetcher(asObjectProvider(s3Client), CommonConfig().objectMapper()).fetchTweets()
        assertThat(tweets).hasSize(30)
        tweets
            .single { it.id == "1182363167533535237" }
            .also { tweet ->
                assertThat(tweet).isNotNull
                assertThat(tweet.id).isEqualTo("1182363167533535237")
                assertThat(tweet.fullName).isEqualTo("Future of AI")
                assertThat(tweet.nickName).isEqualTo("future_of_AI")
                assertThat(tweet.message).isEqualTo("How To Ensure Human Touch In Digital Healthcare Driven By Ai Solutions? https://t.co/RNM7BXZT5i #AI #healthcare… https://t.co/ZoWz7rihNM")
                assertThat(tweet.formattedMesssage).isEqualTo("How To Ensure Human Touch In Digital Healthcare Driven By Ai Solutions? <a href=\"#\" onclick=\"window.open('https://buff.ly/2oteyOZ', '_blank')\">buff.ly/2oteyOZ</a> <span class=\"hashtag\">#AI</span> <span class=\"hashtag\">#healthcare</span>… <a href=\"#\" onclick=\"window.open('https://twitter.com/i/web/status/1182363167533535237', '_blank')\">twitter.com/i/web/status/1…</a>")
                assertThat(tweet.profileImage).isEqualTo("https://pbs.twimg.com/profile_images/1047866034970140673/ZBhSks16_normal.jpg")
                assertThat(tweet.timestamp).isEqualTo(1570739521000)
                assertThat(tweet.retweet).isFalse
                assertThat(tweet.retweetedBy).contains("DME_Jun")
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
}
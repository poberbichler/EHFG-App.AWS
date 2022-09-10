package org.ehfg.app.lambda.twitter

import org.assertj.core.api.Assertions.assertThat
import org.ehfg.app.lambda.common.CommonConfig
import org.junit.jupiter.api.Test
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

        val tweets = TweetFetcher(s3Client, CommonConfig().objectMapper()).fetchTweets()
        assertThat(tweets).hasSize(30)
        tweets
            .single { it.id == "828691081424351233" }
            .also { tweet ->
                assertThat(tweet).isNotNull
                assertThat(tweet.id).isEqualTo("828691081424351233")
                assertThat(tweet.fullName).isEqualTo("Lloyd Larcher")
                assertThat(tweet.nickName).isEqualTo("Lloyd_16707")
                assertThat(tweet.message).isEqualTo("RT @EU_eHealth: <span class=\"hashtag\">#eHealth</span> in Europe: 'Reality meets Reality' at <span class=\"hashtag\">#EHFG2016:</span> <a href=\"#\" onclick=\"window.open('http://bit.ly/2bLNWQQ', '_blank')\">bit.ly/2bLNWQQ</a> @GasteinForum   @peetso_terje https://t.c... ")
                assertThat(tweet.profileImage).isEqualTo("http://pbs.twimg.com/profile_images/644521777234755585/uBK9YY_p_normal.jpg")
                assertThat(tweet.timestamp).isEqualTo(1486413924000)
                assertThat(tweet.retweet).isTrue
                assertThat(tweet.retweetedBy).isEmpty()
            }
    }

    private fun getStaticCredentials(): StaticCredentialsProvider =
        StaticCredentialsProvider.create(
            AwsBasicCredentials.create(
                localStackContainer.accessKey,
                localStackContainer.secretKey
            )
        )
}
package org.ehfg.app.lambda.common

import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.ehfg.app.lambda.session.Day
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.ObjectProvider
import org.testcontainers.containers.localstack.LocalStackContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.core.sync.ResponseTransformer
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import java.util.*

@Testcontainers
internal class S3UploaderTest {
    @Container
    val localStackContainer = LocalStackContainer(DockerImageName.parse("localstack/localstack"))
        .withServices(LocalStackContainer.Service.S3)!!

    @Test
    fun shouldUploadToS3() {
        val s3Client = S3Client.builder()
            .credentialsProvider(getStaticCredentials())
            .region(Region.of(localStackContainer.region))
            .endpointOverride(localStackContainer.getEndpointOverride(LocalStackContainer.Service.S3))
            .build()

        s3Client.createBucket { it.bucket("ehfg-app") }

        val key = UUID.randomUUID().toString()
        S3Uploader(asObjectProvider(s3Client), ObjectMapper())
            .upload(key, mapOf(Pair("Day 1", Day(description = "some desc", sessions = emptyList()))))

        val payloadInBucket = s3Client
            .getObject({ it.bucket("ehfg-app").key(key) }, ResponseTransformer.toBytes())
            .asUtf8String()

        assertThat(payloadInBucket)
            .isEqualTo("""{"Day 1":{"description":"some desc","sessions":[]}}""")
    }

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

    private fun getStaticCredentials(): StaticCredentialsProvider =
        StaticCredentialsProvider.create(
            AwsBasicCredentials.create(
                localStackContainer.accessKey,
                localStackContainer.secretKey
            )
        )
}
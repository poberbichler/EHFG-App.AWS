package org.ehfg.app.lambda.common

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.ObjectProvider
import org.springframework.stereotype.Component
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client

@Component
class S3Uploader(
    val s3Client: ObjectProvider<S3Client>,
    val objectMapper: ObjectMapper
) {
    val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    fun upload(key: String, payload: Any) {
        logger.info("uploading to {}", key)

        this.s3Client.getObject().putObject(
            { request -> request.bucket("ehfg-app").key(key) },
            RequestBody.fromString(this.objectMapper.writeValueAsString(payload))
        )

        logger.info("done uploading")
    }
}
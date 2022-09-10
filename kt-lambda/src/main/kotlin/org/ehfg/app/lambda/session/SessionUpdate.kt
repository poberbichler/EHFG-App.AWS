package org.ehfg.app.lambda.session

import org.ehfg.app.lambda.common.ProgramDownloader
import org.ehfg.app.lambda.common.S3Uploader
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.function.Consumer

@Service
class SessionUpdate(
    val downloader: ProgramDownloader,
    val sessionTransformer: SessionTransformer,
    val uploader: S3Uploader
) : Consumer<Any> {
    private val log = LoggerFactory.getLogger(this.javaClass)

    override fun accept(input: Any) {
        log.info("updating session... {} - {}", input, input.javaClass);

        this.downloader.download()
            .run { sessionTransformer.transform(this) }
            .run { uploader.upload("sessions.json", this) }
    }
}
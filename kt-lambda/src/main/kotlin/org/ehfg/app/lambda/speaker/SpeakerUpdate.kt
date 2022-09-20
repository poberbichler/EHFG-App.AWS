package org.ehfg.app.lambda.speaker

import org.ehfg.app.lambda.common.ProgramDownloader
import org.ehfg.app.lambda.common.S3Uploader
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.function.Consumer

@Component
class SpeakerUpdate(
    val downloader: ProgramDownloader,
    val speakerTransformer: SpeakerTransformer,
    val uploader: S3Uploader
) : Consumer<Any> {
    private val log = LoggerFactory.getLogger(this.javaClass)

    override fun accept(input: Any) {
        log.info("updating speakers... {} - {}", input, input.javaClass);

        this.downloader.downloadAndConvert()
            .run { speakerTransformer.transform(this) }
            .run { uploader.upload("speakers.json", this) }
    }
}
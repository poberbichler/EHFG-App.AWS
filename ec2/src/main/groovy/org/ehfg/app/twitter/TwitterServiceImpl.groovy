package org.ehfg.app.twitter

import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.social.twitter.api.Stream
import org.springframework.social.twitter.api.StreamListener
import org.springframework.social.twitter.api.Twitter
import org.springframework.stereotype.Service
/**
 * @author patrick
 * @since 06.2017
 */
@Slf4j
@Service
class TwitterServiceImpl implements TwitterService {
    private final Map<String, Stream> activeListener = [:]

    private final Twitter twitterTemplate
    private final StreamListener streamListener

    @Autowired
    TwitterServiceImpl(Twitter twitterTemplate, StreamListener streamListener) {
        this.twitterTemplate = twitterTemplate
        this.streamListener = streamListener
    }

    @Override
    void addListener(String hashtag) {
        log.info("adding listener for hashtag [{}]", hashtag)
        def stream = this.twitterTemplate.streamingOperations()
                .filter(hashtag, [streamListener])

        this.activeListener.put(hashtag, stream)
    }


    @Override
    boolean removeListener(String hashtag) {
        log.info("removing listener for hashtag [{}]", hashtag)

        def stream = this.activeListener.remove(hashtag)
        stream?.close()
        return stream != null
    }

    @Override
    Collection<String> getListener() {
        return activeListener.keySet()
    }
}

package org.ehfg.app.twitter

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.social.twitter.api.StreamingOperations
import org.springframework.social.twitter.api.Twitter
import org.springframework.test.context.junit4.SpringRunner

import static org.assertj.core.api.Assertions.assertThat
import static org.mockito.BDDMockito.given
/**
 * @author patrick
 * @since 07.2017
 */
@SpringBootTest()
@RunWith(SpringRunner.class)
class TwitterServiceImplTest {
    @Autowired
    TwitterServiceImpl twitterService

    @MockBean
    Twitter twitterTemplate

    @MockBean
    StreamingOperations streamingOperations

    @Before
    void setup() {
        given(twitterTemplate.streamingOperations()).willReturn(streamingOperations)
    }

    @Test
    void addAndRemoveListener() {
        assertThat(this.twitterService.getListener()).isEmpty()

        this.twitterService.addListener("EHFG2017")

        assertThat(this.twitterService.getListener())
                .hasSize(1)
                .contains("EHFG2017")

        this.twitterService.removeListener("EHFG2017")
        assertThat(this.twitterService.getListener()).isEmpty()
    }
}

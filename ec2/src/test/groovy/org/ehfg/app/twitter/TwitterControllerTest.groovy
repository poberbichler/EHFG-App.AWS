package org.ehfg.app.twitter

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc

import static org.mockito.BDDMockito.given
import static org.mockito.Matchers.anyString
import static org.mockito.Mockito.verify
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
 * @author patrick
 * @since 07.2017
 */
@RunWith(SpringRunner.class)
@WebMvcTest(TwitterController.class)
class TwitterControllerTest {
    @Autowired
    MockMvc mockMvc

    @MockBean
    TwitterService twitterService

    @Test
    void listenerShouldReturnStreams() {
        given(twitterService.getListener()).willReturn(["#EHFG2017"])

        mockMvc.perform(get("/twitter"))
                .andExpect(status().isOk())
                .andExpect(content().json('["#EHFG2017"]'))
    }

    @Test
    void postShouldAddStream() {
        given(twitterService.addListener("EHFG2017")).willReturn(true)
        mockMvc.perform(post("/twitter/EHFG2017"))
                .andExpect(status().isCreated())
                .andExpect(content().string("EHFG2017"))

        verify(twitterService).addListener("EHFG2017")
    }

    @Test
    void existingPostShouldReturnOk() {
        given(twitterService.addListener("EHFG2017")).willReturn(false)
        mockMvc.perform(post("/twitter/EHFG2017"))
                .andExpect(status().isOk())
                .andExpect(content().string("EHFG2017"))

        verify(twitterService).addListener("EHFG2017")
    }

    @Test
    void deleteShouldRemoveStreamAndSendOk() {
        given(twitterService.removeListener(anyString())).willReturn(true)

        mockMvc.perform(delete("/twitter/EHFG2017"))
                .andExpect(status().isOk())

        verify(twitterService).removeListener("EHFG2017")
    }

    @Test
    void deleteMissingStreamShouldResultToNotFound() {
        given(twitterService.removeListener(anyString())).willReturn(false)

        mockMvc.perform(delete("/twitter/EHFG2017"))
                .andExpect(status().isNotFound())

        verify(twitterService).removeListener("EHFG2017")
    }
}

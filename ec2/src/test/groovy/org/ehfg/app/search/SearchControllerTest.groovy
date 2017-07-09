package org.ehfg.app.search

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc

import static org.hamcrest.Matchers.isEmptyString
import static org.hamcrest.Matchers.not
import static org.mockito.BDDMockito.given
import static org.mockito.Mockito.verify
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
 * @author patrick
 * @since 07.2017
 */
@RunWith(SpringRunner.class)
@WebMvcTest(SearchController.class)
class SearchControllerTest {
    @Autowired
    MockMvc mockMvc

    @MockBean
    SearchService searchService

    @Test
    void getShouldReturnSearchResult() {
        def searchTerm = "helmut"

        given(searchService.search(searchTerm)).willReturn(new SearchResult())

        this.mockMvc.perform(get("/search/$searchTerm"))
                .andExpect(status().isOk())
                .andExpect(content().string(not(isEmptyString())))

        verify(searchService).search(searchTerm)
    }

    @Test
    void updateIndexOnPost() {
        this.mockMvc.perform(post("/search"))
                .andExpect(status().isOk())

        verify(searchService).updateIndex()
    }
}

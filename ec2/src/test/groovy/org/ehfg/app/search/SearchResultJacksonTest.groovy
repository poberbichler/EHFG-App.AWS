package org.ehfg.app.search

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester
import org.springframework.test.context.junit4.SpringRunner

import static org.assertj.core.api.Assertions.assertThat

/**
 * @author patrick
 * @since 07.2017
 */
@JsonTest
@RunWith(SpringRunner.class)
class SearchResultJacksonTest {
    @Autowired
    JacksonTester<SearchResult> json

    @Test
    void serializeEmptySearchResult() {
        def emptySearchResult = new SearchResult()
        assertThat(this.json.write(emptySearchResult)).isEqualToJson("empty.json")
    }

    @Test
    void deserializeWithoutTweets() {
        def searchResult = new SearchResult()
        searchResult << new SearchResultPosition(id: 175, type: SearchResultType.SESSION, description: "CLOSING PLENARY")
        searchResult << new SearchResultPosition(id: 178, type: SearchResultType.SESSION, description: "Late breaking topic: Beyond BREXIT")
        searchResult << new SearchResultPosition(id: 149, type: SearchResultType.SESSION, description: "Urban environments and NCDs")
        searchResult << new SearchResultPosition(id: 179, type: SearchResultType.SESSION, description: "OPENING PLENARY")
        searchResult << new SearchResultPosition(id: 159, type: SearchResultType.SESSION, description: "Health literacy")
        searchResult << new SearchResultPosition(id: 124, type: SearchResultType.SPEAKER, description: "Brand Helmut")

        assertThat(this.json.write(searchResult)).isEqualToJson("helmut.json")
    }
}

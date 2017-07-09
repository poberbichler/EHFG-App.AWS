package org.ehfg.app.search

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.ToString

/**
 * @author patrick
 * @since 07.2017
 */
class SearchResult {
    List<String> tweets = []

    @JsonIgnore
    Map<SearchResultType, SearchResultWrapper> results = [
            (SearchResultType.SPEAKER) : new SearchResultWrapper(SearchResultType.SPEAKER),
            (SearchResultType.SESSION) : new SearchResultWrapper(SearchResultType.SESSION),
            (SearchResultType.LOCATION): new SearchResultWrapper(SearchResultType.LOCATION)]

    List<SearchResultPosition> getAt(SearchResultType type) {
        return this.results[type].data
    }

    SearchResult leftShift(SearchResultPosition position) {
        this[position.type].add(position)
        return this
    }

    @JsonProperty
    Collection<SearchResultWrapper> getResults() {
        return this.results.values()
    }

    @JsonProperty
    boolean hasAnyResult() {
        return !tweets.empty || results.find { !it.value.data.empty }
    }
}

@ToString
class SearchResultWrapper {
    final SearchResultType type
    List<SearchResultPosition> data = []

    SearchResultWrapper(SearchResultType type) {
        this.type = type
    }
}

class SearchResultPosition {
    String id
    SearchResultType type
    String description
}

enum SearchResultType {
    SPEAKER,
    SESSION,
    LOCATION
}
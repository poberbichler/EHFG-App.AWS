package org.ehfg.app.search

/**
 * @author patrick
 * @since 07.2017
 */
interface SearchService {
    SearchResult search(String searchTerm)

    void updateIndex()
}
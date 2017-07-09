package org.ehfg.app.search

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author patrick
 * @since 07.2017
 */
@RestController
@RequestMapping("search")
class SearchController {
    private final SearchService searchService

    @Autowired
    SearchController(SearchService searchService) {
        this.searchService = searchService
    }

    @GetMapping("{searchTerm}")
    SearchResult search(@PathVariable("searchTerm") String searchTerm) {
        return this.searchService.search(searchTerm)
    }

    @PostMapping
    void updateIndex() {
        this.searchService.updateIndex()
    }
}

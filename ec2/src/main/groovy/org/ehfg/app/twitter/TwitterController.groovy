package org.ehfg.app.twitter

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
/**
 * @author patrick
 * @since 06.2017
 */
@RestController
@RequestMapping("twitter")
class TwitterController {
    private final TwitterService twitterService

    @Autowired
    TwitterController(TwitterService twitterService) {
        this.twitterService = twitterService
    }

    @GetMapping
    Collection<String> getListener() {
        return this.twitterService.getListener()
    }

    @PostMapping("{hashtag}")
    ResponseEntity<?> addListener(@PathVariable("hashtag") String hashtag) {
        if (this.twitterService.addListener(hashtag)) {
            return ResponseEntity.status(HttpStatus.CREATED).body(hashtag)
        }

        return ResponseEntity.ok(hashtag)
    }

    @DeleteMapping("{hashtag}")
    ResponseEntity<?> removeListener(@PathVariable("hashtag") String hashtag) {
        if (!this.twitterService.removeListener(hashtag)) {
            return ResponseEntity.notFound().build()
        }

        return ResponseEntity.ok().build()
    }
}

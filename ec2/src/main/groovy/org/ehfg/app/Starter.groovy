package org.ehfg.app

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.EnableScheduling

/**
 * @author patrick
 * @since 06.2017
 */
@EnableScheduling
@SpringBootApplication
class Starter {
    static void main(String[] args) {
        SpringApplication.run(Starter.class, args)
    }
}

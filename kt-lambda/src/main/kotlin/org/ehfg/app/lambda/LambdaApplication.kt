package org.ehfg.app.lambda

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LambdaApplication

fun main(args: Array<String>) {
	runApplication<LambdaApplication>(*args)
}

package app.bluelips.lib

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LibApplication

fun main(args: Array<String>) {
	runApplication<LibApplication>(*args)
}

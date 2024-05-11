package app.boboc

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WebSocketTempApplication

fun main(args: Array<String>) {
    runApplication<WebSocketTempApplication>(*args)
}
package app.boboc.handler

import app.boboc.annotation.WebSocketController
import app.boboc.annotation.WebSocketHandlerMapping
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession

@WebSocketController
class TempController {

    data class TestMessage(
        val id : String,
        val name: String?
    )

    @WebSocketHandlerMapping("test")
    suspend fun test(session: WebSocketSession, message: WebSocketMessage, customMessage: TestMessage): String {
        println("In Test Function : ${session.id}, ${message.payload}")
        println("Custom Message: $customMessage")
        return "ASDASD"
    }

}

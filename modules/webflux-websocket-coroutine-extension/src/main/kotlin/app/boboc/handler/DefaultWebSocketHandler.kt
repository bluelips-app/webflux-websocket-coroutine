package app.boboc.handler

import app.boboc.handler.CoroutineWebSocketHandler
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession

class DefaultWebSocketHandler : CoroutineWebSocketHandler() {
    override suspend fun coroutineHandle(session: WebSocketSession, message: WebSocketMessage) {
        println("${session.id} - ${message.payloadAsText}")
        println("${session.handshakeInfo.uri}")
    }
}
package app.bluelips.lib.handler

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.mono
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono

abstract class AbstractWebSocketHandler : WebSocketHandler {

    fun receiveMessage(socketSession: WebSocketSession): Flow<WebSocketMessage> {
        return socketSession.receive().asFlow()
            .onEach {
                println(it.payloadAsText)
            }
    }

    override fun handle(session: WebSocketSession): Mono<Void> {
        return mono {
            receiveMessage(session)
        }.then()
    }
}

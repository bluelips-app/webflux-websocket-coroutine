package app.bluelips.lib.handler

import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.mono
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono

abstract class AbstractWebSocketHandler : WebSocketHandler {
    private fun receiveMessage(session: WebSocketSession): Mono<Void> {
        return mono {
            session.receive().map {
                it.retain()
            }.asFlow()
                .collect {
                    receiveMessageHandle(session, it)
                }
        }.then()
    }

    abstract fun receiveMessageHandle(session: WebSocketSession, message: WebSocketMessage)

    override fun handle(session: WebSocketSession): Mono<Void> {
        return receiveMessage(session)
    }
}

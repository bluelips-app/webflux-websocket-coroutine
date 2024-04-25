package app.bluelips.lib.handler

import app.bluelips.lib.annotation.WebSocketHandleMapping
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.reactor.mono
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono

@WebSocketHandleMapping("test")
class TestHandler : WebSocketHandler {

    val objectMapper = ObjectMapper().registerKotlinModule()

    suspend fun receiveMessageHandle(session: WebSocketSession, message: String) {
        println(message)
        session.sendMessage("SEND $message")
    }

    private fun receiveMessage(session: WebSocketSession): Mono<Void> {
        return mono {
            session.receive().map {
                it.retain()
            }.asFlow()
                .collect {
                    receiveMessageHandle(session, it.payloadAsText)
                }
        }.then()
    }


    override fun handle(session: WebSocketSession): Mono<Void> {
        return receiveMessage(session).then()
            .doOnCancel {
                println("Cancel: ${session.id}")
            }
            .doOnError {
                println("doOnError: ${session.id}")
            }
            .doOnTerminate {
                println("doOnTerminate: ${session.id}")
            }
    }

    suspend fun WebSocketSession.sendMessage(message: Any?) {
        val session = this
        session.send(mono {
            session.textMessage(objectMapper.writeValueAsString(message ?: ""))
        }).awaitSingleOrNull()
    }
}
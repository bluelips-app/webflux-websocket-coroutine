package app.boboc.util

import app.boboc.util.WebSocketSessionUtil.sendMessage
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.reactor.mono
import org.springframework.web.reactive.socket.WebSocketSession

object WebSocketSessionUtil {
    val objectMapper = ObjectMapper().registerKotlinModule()

    suspend fun WebSocketSession.sendMessage(message: Any?) {
        val session = this
        session.send(mono {
            session.textMessage(objectMapper.writeValueAsString(message ?: ""))
        }).awaitSingleOrNull()
    }

    suspend fun WebSocketSession.sendStringMessage(message: String?){
        val session = this
        session.send(mono {
            session.textMessage(message ?: "null")
        }).awaitSingleOrNull()
    }
}

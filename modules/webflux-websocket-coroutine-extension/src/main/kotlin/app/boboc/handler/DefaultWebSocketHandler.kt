package app.boboc.handler

import app.boboc.util.WebSocketSessionUtil
import app.boboc.util.WebSocketSessionUtil.sendMessage
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.starProjectedType

class DefaultWebSocketHandler(
    private val controllerBean: Any,
    private val beanMethod: KFunction<*>
) : CoroutineWebSocketHandler() {
    override suspend fun coroutineHandle(session: WebSocketSession, message: WebSocketMessage) {
        parseRequest(session, message)
            .run {
                beanMethod.callBy(this)
            }.also {
                if (!beanMethod.isReturnUnit())
                    session.sendMessage(it)
            }
    }

    private suspend fun parseRequest(session: WebSocketSession, message: WebSocketMessage): Map<KParameter, Any> {
        return beanMethod.parameters.map {
            it.parseParameter(session, message)
        }.associate { it.first to it.second }
    }

    private fun KParameter.parseParameter(session: WebSocketSession, message: WebSocketMessage): Pair<KParameter, Any> {
        return if (this.name == null) {
            this to controllerBean
        } else if (this.isSession()) {
            this to session
        } else if (this.isMessage()) {
            this to message
        } else {
            (this.type.classifier!! as KClass<*>).let { clazz ->
                this to (WebSocketSessionUtil.objectMapper.readValue(message.payloadAsText, clazz.java))
            }
        }
    }

    private fun KParameter.isSession(): Boolean {
        return this.type == WebSocketSession::class.starProjectedType
    }

    private fun KParameter.isMessage(): Boolean {
        return this.type == WebSocketMessage::class.starProjectedType
    }

    private fun KFunction<*>.isReturnUnit(): Boolean {
        return this.returnType == Unit::class.starProjectedType
    }

}

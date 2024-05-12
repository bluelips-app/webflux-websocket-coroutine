package app.boboc.handler

import app.boboc.util.WebSocketSessionUtil
import app.boboc.util.WebSocketSessionUtil.sendMessage
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KType
import kotlin.reflect.full.starProjectedType

class DefaultWebSocketHandler(
    private val controllerBean: Any,
    private val beanMethod: KFunction<*>
) : CoroutineWebSocketHandler() {
    override suspend fun coroutineHandle(session: WebSocketSession, message: WebSocketMessage) {
        beanMethod.parameters.map {
            if (it.name == null) {
                it to controllerBean
            } else if (it.type == session.kotlinType) {
                it to session
            } else if (it.type == message.kotlinType) {
                it to message
            } else {
                val clazz = it.type.classifier!! as KClass<*>
                it to (WebSocketSessionUtil.objectMapper.readValue(message.payloadAsText, clazz.java))

            }
        }.run {
            beanMethod.callBy(this.associate { it.first to it.second })
        }.also {
            session.sendMessage(it)
        }
    }


    private val WebSocketSession.kotlinType: KType
        get() = WebSocketSession::class.starProjectedType

    private val WebSocketMessage.kotlinType: KType
        get() = WebSocketMessage::class.starProjectedType
}

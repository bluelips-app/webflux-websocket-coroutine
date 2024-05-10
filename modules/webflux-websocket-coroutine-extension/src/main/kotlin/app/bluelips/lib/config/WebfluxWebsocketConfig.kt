package app.bluelips.lib.config

import app.bluelips.lib.annotation.WebSocketController
import app.bluelips.lib.annotation.WebSocketHandlerMapping
import app.bluelips.lib.handler.DefaultWebSocketHandler
import org.springframework.beans.factory.getBean
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.HandlerMapping
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping
import org.springframework.web.reactive.socket.WebSocketHandler
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberFunctions

@Configuration
class WebfluxWebsocketConfig(
    private val applicationContext: ApplicationContext,
) {
    @Bean
    fun handlerMapping(): HandlerMapping {
        return SimpleUrlHandlerMapping(pathHandlerMap(), -1)
    }

    fun pathHandlerMap(): Map<String, Any> {
        val handlerMap = mutableMapOf<String, Any>()
        applicationContext.getBeanNamesForAnnotation(WebSocketController::class.java)
            .forEach {
                applicationContext.getBean(it)
                    .run {
                        this::class.memberFunctions.mapNotNull {  beanMethod ->
                            beanMethod.findAnnotation<WebSocketHandlerMapping>()?.path
                        }
                    }.forEach { path ->
                        handlerMap[path] = DefaultWebSocketHandler()
                    }
            }
        return handlerMap
    }
}

package app.boboc.config

import app.boboc.annotation.WebSocketController
import app.boboc.annotation.WebSocketHandlerMapping
import app.boboc.handler.DefaultWebSocketHandler
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.HandlerMapping
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberFunctions

@Configuration
class WebfluxWebsocketConfig(
    private val applicationContext: ApplicationContext,
) {
    @Bean
    fun websocketHandlerMapping(): HandlerMapping {
        return SimpleUrlHandlerMapping(pathHandlerMap(), -1)
    }

    fun pathHandlerMap(): Map<String, Any> {
        val handlerMap = mutableMapOf<String, Any>()
        applicationContext.getBeanNamesForAnnotation(WebSocketController::class.java)
            .forEach {
                applicationContext.getBean(it)
                    .run {
                        this::class.memberFunctions.forEach { beanMethod ->
                            beanMethod.findAnnotation<WebSocketHandlerMapping>()?.let { ann ->
                                handlerMap[ann.path] = DefaultWebSocketHandler(this, beanMethod)
                            }
                        }
                    }
            }
        return handlerMap
    }
}

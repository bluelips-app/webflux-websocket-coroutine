package app.bluelips.lib.config

import app.bluelips.lib.annotation.WebSocketHandlerMapping
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.HandlerMapping
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping

@Configuration
class WebfluxWebsocketConfig(private val applicationContext: ApplicationContext,
) {
    @Bean
    fun handlerMapping(): HandlerMapping {
        return SimpleUrlHandlerMapping(pathHandlerMap(), -1)
    }

    fun pathHandlerMap(): Map<String, Any> {

        return applicationContext.getBeanNamesForAnnotation(WebSocketHandlerMapping::class.java).associate {
            applicationContext.getBean(it).run {
                javaClass.getAnnotation(WebSocketHandlerMapping::class.java).path to this
            }
        }
    }
}


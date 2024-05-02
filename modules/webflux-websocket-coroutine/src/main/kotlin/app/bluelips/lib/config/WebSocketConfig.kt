package app.bluelips.lib.config

import app.bluelips.lib.annotation.WebSocketHandleMapping
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.HandlerMapping
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping

@Configuration
class WebSocketConfig(
    private val applicationContext: ApplicationContext,
) {
    @Bean
    fun handlerMapping(): HandlerMapping {
        return SimpleUrlHandlerMapping(pathHandlerMap(), -1)
    }

    fun pathHandlerMap(): Map<String, Any> {

        return applicationContext.getBeanNamesForAnnotation(WebSocketHandleMapping::class.java).associate {
            applicationContext.getBean(it).run {
                javaClass.getAnnotation(WebSocketHandleMapping::class.java).path to this
            }
        }
    }
}

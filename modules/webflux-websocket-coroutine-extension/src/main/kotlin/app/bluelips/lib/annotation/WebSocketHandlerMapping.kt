package app.bluelips.lib.annotation

import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.Mapping

@Component
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Mapping
annotation class WebSocketHandlerMapping(val path : String)

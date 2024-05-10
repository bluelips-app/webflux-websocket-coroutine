package app.bluelips.lib.handler

import app.bluelips.lib.annotation.WebSocketController
import app.bluelips.lib.annotation.WebSocketHandlerMapping
import org.springframework.stereotype.Component

@WebSocketController
class TempController {

    @WebSocketHandlerMapping("test")
    suspend fun test(){

    }

}

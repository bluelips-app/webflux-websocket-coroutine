package app.boboc.handler

import app.boboc.annotation.WebSocketController
import app.boboc.annotation.WebSocketHandlerMapping

@WebSocketController
class TempController {

    @WebSocketHandlerMapping("test")
    suspend fun test(){

    }

}

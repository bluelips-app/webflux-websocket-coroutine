plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "lib"
include("modules:webflux-websocket-coroutine")
findProject(":modules:webflux-websocket-coroutine")?.name = "webflux-websocket-coroutine"

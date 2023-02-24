package helper

import io.ktor.network.tls.certificates.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File

fun startWebAppServer() {
//    embeddedServer(Netty, port = 8080, host = "192.168.0.104", module = Application::module)
//        .start(wait = true)
    
    embeddedServer(Netty, environment = applicationEngineEnvironment{
        val keyStoreFile = File("webapp".asResources(), "keystore.jks")
        println(keyStoreFile.absolutePath)
        val keyStore = buildKeyStore {
            certificate("sampleAlias") {
                password = "foobar"
//                domains = listOf("127.0.0.1", "0.0.0.0", "localhost", "192.168.0.104")
            }
        }
        keyStore.saveToFile(keyStoreFile, "123456")
    
        connector {
            port = 8080
        }
    
        sslConnector(
            keyStore = keyStore,
            keyAlias = "sampleAlias",
            keyStorePassword = { "123456".toCharArray() },
            privateKeyPassword = { "foobar".toCharArray() }) {
            port = 8890
            host = "192.168.0.104"
            keyStorePath = keyStoreFile
        }
    
        module(Application::module)
    }).start(true)
}

private fun Application.module() {
    routing {
        static("/") {
            staticBasePackage = "webapp"
            resource("index.html")
        }
        
        route("/") {
            get {
                call.respondFile("webapp/index.html".asResources())
            }
        }
    }
}
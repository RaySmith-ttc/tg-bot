package ru.raysmith.tgbot.webappapp

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.browser.window
import kotlinx.coroutines.MainScope

val apiScope = MainScope()

object API {
    val client = HttpClient {
        defaultRequest {
            url(window.location.protocol + "//" + window.location.host)
        }
    }

    suspend fun verifyInitData(initData: String) = client.post("verify") {
        setBody(initData)
    }.status == HttpStatusCode.Accepted
}
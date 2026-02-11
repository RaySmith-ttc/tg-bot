package ru.raysmith.tgbot.webappapp

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.browser.window
import kotlinx.coroutines.MainScope

val mainScope = MainScope()

object API {
    val client = HttpClient {
        expectSuccess = true
        defaultRequest {
            url(window.location.protocol + "//" + window.location.host)
        }

        HttpResponseValidator {
            handleResponseException {
                if (it is ResponseException) {
                    throw ApiException(it.response.bodyAsText())
                } else {
                    throw it
                }
            }
        }
    }

    suspend fun verifyInitData(initData: String) = client.post("verify") {
        setBody(initData)
    }.status == HttpStatusCode.Accepted

    suspend fun createInvoiceLink() = client.get("createInvoiceLink").bodyAsText()
    suspend fun preparedMessageId(userId: Long) = client.get("preparedMessageId?userId=$userId").bodyAsText()
}

class ApiException(override val message: String) : Exception(message)

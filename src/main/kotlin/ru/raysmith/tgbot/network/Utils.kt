package ru.raysmith.tgbot.network

import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.utils.io.streams.*
import kotlinx.serialization.encodeToString
import ru.raysmith.tgbot.model.network.media.input.InputFile
import kotlin.time.Duration

internal inline fun <reified T> HttpRequestBuilder.parameter(key: String, value: T?) {
    when {
        T::class == String::class -> value?.also { url.parameters.append(key, it as String) }
        T::class == Int::class -> value?.also { url.parameters.append(key, it.toString()) }
        T::class == Long::class -> value?.also { url.parameters.append(key, it.toString()) }
        T::class == Boolean::class -> value?.also { url.parameters.append(key, it.toString()) }
        T::class == Duration::class -> value?.also { url.parameters.append(key, (it as Duration).inWholeSeconds.toString()) }
        T::class.java.isEnum -> value?.let {
            url.parameters.append(key, TelegramApi.json.encodeToString<T>(it).drop(1).dropLast(1))
        }
        else -> {
            value?.let { url.parameters.append(key, TelegramApi.json.encodeToString<T>(it)) }
        }
    }
}

internal fun HttpRequestBuilder.setMultiPartFormDataBody(vararg files: Pair<String, InputFile?>) {
    @Suppress("UNCHECKED_CAST")
    val filtered = files.filter { it.second != null } as List<Pair<String, InputFile>>
    if (filtered.isEmpty()) return

    setBody(MultiPartFormDataContent(
        formData {
            filtered.forEach { data ->
                val (name, inputFile) = data

                when (inputFile) {
                    is InputFile.File -> append(
                        name,
                        InputProvider { inputFile.file.inputStream().asInput() },
                        Headers.build {
//                            append(HttpHeaders.ContentType, Files.probeContentType(inputFile.file.toPath()))
                            append(HttpHeaders.ContentDisposition, "filename=\"${inputFile.file.name}\"")
                        }
                    )

                    is InputFile.ByteArray -> append(name, inputFile.byteArray, Headers.build {
                        if (inputFile.mimeType != null) {
                            append(HttpHeaders.ContentType, inputFile.mimeType)
                        }
                        append(HttpHeaders.ContentDisposition, "filename=\"${inputFile.filename}\"")
                    })

                    is InputFile.FileIdOrUrl -> append(name, inputFile.value)
                }
            }
        }
    ))
}
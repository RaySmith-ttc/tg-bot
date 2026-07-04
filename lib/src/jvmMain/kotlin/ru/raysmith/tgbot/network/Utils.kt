package ru.raysmith.tgbot.network

import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.request.setBody
import io.ktor.http.*
import io.ktor.utils.io.streams.*
import kotlinx.io.asSource
import kotlinx.io.buffered
import okio.source
import ru.raysmith.tgbot.model.api.media.input.InputFile
import ru.raysmith.tgbot.model.api.media.input.NotReusableInputFile
import ru.raysmith.tgbot.model.bot.AttachmentsHolder
import java.time.ZonedDateTime
import kotlin.time.Duration

context(_: HttpRequestBuilder)
private inline fun <reified T> getParamValue(value: T?): String? {
    if (value == null) return null

    if (T::class.java.isEnum) {
        return TelegramApi.json.encodeToString<T>(value).drop(1).dropLast(1)
    }

    return when(T::class) {
        String::class -> value
        Int::class -> value.toString()
        Long::class -> value.toString()
        Boolean::class -> value.toString()
        Duration::class -> (value as Duration).inWholeSeconds.toString()
        ZonedDateTime::class -> (value as ZonedDateTime).toEpochSecond()
        else -> TelegramApi.json.encodeToString<T>(value)
    } as String?
}

// TODO add InputFilesHolder or remove function and replace with appendSerializable
internal inline fun <reified T> HttpRequestBuilder.parameter(key: String, value: T?) {
    check(value !is InputFile) {
        "Values that provides additional attachments are not allowed in query parameters. " +
                "Use setFormDataBody + appendSerializable instead."
    }

    getParamValue(value)?.let { encoded ->
        url.parameters.append(key, encoded)
    }
}

context(_: HttpRequestBuilder)
internal inline fun <reified T : Any> FormBuilder.appendSerializable(
    key: String, value: T?, headers: Headers = Headers.Empty,
) {
    when (value) {
        is InputFile -> {
            appendInputFile(key, value, headers)
        }

        is AttachmentsHolder -> {
            getParamValue<T>(value)?.let {
                append(key, it, headers)
            }

            value.attachments.forEach { (_, attachment) ->
                if (attachment?.file is NotReusableInputFile) {
                    appendInputFile(attachment.fieldName, attachment.file, attachment.headers)
                }
            }
        }

        else -> {
            getParamValue<T>(value)?.let {
                append(key, it, headers)
            }

            if (value is Iterable<*>) {
                value.forEach {
                    if (it is AttachmentsHolder) {
                        it.attachments.forEach { (_, attachment) ->
                            if (attachment?.file is NotReusableInputFile) {
                                appendInputFile(attachment.fieldName, attachment.file, attachment.headers)
                            }
                        }
                    }
                }
            }
        }
    }
}

context(request: HttpRequestBuilder)
fun setFormDataBody(block: FormBuilder.() -> Unit) {
    request.setBody(MultiPartFormDataContent(formData(block)))
}

internal fun FormBuilder.appendInputFile(key: String, value: InputFile?, headers: Headers = Headers.Empty) {
    appendInputFile(key to value, headers = headers)
}

internal fun FormBuilder.appendInputFile(vararg files: Pair<String, InputFile?>, headers: Headers = Headers.Empty) {
    @Suppress("UNCHECKED_CAST")
    val filtered = files.filter { it.second != null } as List<Pair<String, InputFile>>
    if (filtered.isEmpty()) return

    filtered.forEach { data ->
        val (name, inputFile) = data

        when (inputFile) {
            is InputFile.File -> {
                append(
                    name,
                    InputProvider { inputFile.file.inputStream().asInput() },
                    Headers.build {
                        if (inputFile.mimeType != null && !headers.contains(HttpHeaders.ContentType)) {
                            append(HttpHeaders.ContentType, inputFile.mimeType)
                        }
                        if (!headers.contains(HttpHeaders.ContentDisposition)) {
                            append(HttpHeaders.ContentDisposition, "filename=\"${inputFile.file.name}\"")
                        }
                        appendAll(headers)
                    }
                )
            }

            is InputFile.ByteArray -> {
                append(
                    name,
                    inputFile.byteArray,
                    Headers.build {
                        if (inputFile.mimeType != null && !headers.contains(HttpHeaders.ContentType)) {
                            append(HttpHeaders.ContentType, inputFile.mimeType)
                        }
                        if (!headers.contains(HttpHeaders.ContentDisposition)) {
                            append(HttpHeaders.ContentDisposition, "filename=\"${inputFile.filename}\"")
                        }
                        appendAll(headers)
                    }
                )
            }

            is InputFile.InputStream -> {
                append(
                    name,
                    InputProvider { inputFile.stream.asSource().buffered() },
                    Headers.build {
                        if (inputFile.mimeType != null && !headers.contains(HttpHeaders.ContentType)) {
                            append(HttpHeaders.ContentType, inputFile.mimeType)
                        }
                        if (!headers.contains(HttpHeaders.ContentDisposition)) {
                            append(HttpHeaders.ContentDisposition, "filename=\"${inputFile.filename}\"")
                        }
                        appendAll(headers)
                    }
                )
            }

            is InputFile.FileIdOrUrl -> append(name, inputFile.value, headers)
        }
    }
}

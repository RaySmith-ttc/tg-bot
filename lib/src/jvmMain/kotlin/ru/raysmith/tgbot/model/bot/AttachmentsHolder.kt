package ru.raysmith.tgbot.model.bot

import io.ktor.http.Headers
import ru.raysmith.tgbot.model.api.media.input.InputFile
import java.util.UUID

internal fun attachmentOf(file: InputFile?): AttachmentData? {
    if (file == null) return null

    return AttachmentData(file)
}

data class AttachmentData(
    val file: InputFile,
    val fieldName: String = UUID.randomUUID().toString(),
    val headers: Headers = Headers.Empty,
)

internal interface AttachmentsHolder {
    // use custom string as key instead of InputFile to improve performance
    val attachments: Map<String, AttachmentData?>

    fun fieldValue(key: String) = attachments[key]!!.let { attachment ->
        when (attachment.file) {
            is InputFile.ByteArray -> "attach://${attachment.fieldName}"
            is InputFile.InputStream -> "attach://${attachment.fieldName}"
            is InputFile.File -> "attach://${attachment.fieldName}"
            is InputFile.FileIdOrUrl -> attachment.file.value
        }
    }
}

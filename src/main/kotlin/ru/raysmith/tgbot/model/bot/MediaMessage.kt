package ru.raysmith.tgbot.model.bot

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.model.network.media.input.InputFile
import ru.raysmith.tgbot.model.network.message.MessageResponse
import ru.raysmith.tgbot.model.network.message.ParseMode
import ru.raysmith.tgbot.utils.withSafeLength
import java.nio.file.Files

abstract class CaptionableMessage {
    protected var _caption: MessageText? = null

    /** Simple caption text to send the message */
    var caption: String? = null

    fun hasCaption() = caption != null || _caption != null

    /** Whether test should be truncated if caption length is greater than 1024 */
    var safeTextLength: Boolean = Bot.Config.safeTextLength

    /**
     * Sets a caption as [MessageText] object
     * */
    fun captionWithEntities(setText: MessageText.() -> Unit) {
        _caption = MessageText(MessageTextType.CAPTION).apply(setText)
    }

    fun getCaptionText(): String? =
        _caption?.getTextString()
            ?: caption?.let { if (safeTextLength) it.withSafeLength(MessageTextType.CAPTION) else it }
}


// TODO add auto ChatAction
abstract class MediaMessage : CaptionableMessage(), IMessage<MessageResponse>, KeyboardCreator {

    protected var media: InputFile? = null
    abstract val mediaName: String

    /** [Parse mode][ParseMode] for a simple caption text */
    var parseMode: ParseMode? = null

    override var disableNotification: Boolean? = null
    override var replyToMessageId: Int? = null
    override var allowSendingWithoutReply: Boolean? = null
    override var keyboardMarkup: MessageKeyboard? = null
    override var protectContent: Boolean? = null

    protected fun getMediaMultipartBody() = when(media) {
        is InputFile.ByteArray -> {
            val media = media as InputFile.ByteArray
            val body = media.byteArray.toRequestBody(media.mimeType.toMediaType())
            MultipartBody.Part.createFormData(mediaName, media.filename, body)
        }
        is InputFile.File -> {
            val media = media as InputFile.File
            val mimeType = Files.probeContentType(media.file.toPath()).toMediaType()
            val body = media.file.readBytes().toRequestBody(mimeType)
            MultipartBody.Part.createFormData(mediaName, media.file.nameWithoutExtension, body)
        }
        else -> error("Only ByteArray and File can be multipart")
    }
}
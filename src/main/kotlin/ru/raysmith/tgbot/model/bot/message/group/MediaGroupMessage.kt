package ru.raysmith.tgbot.model.bot.message.group

import io.ktor.client.*
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.bot.message.IMessage
import ru.raysmith.tgbot.model.bot.message.MessageText
import ru.raysmith.tgbot.model.bot.message.MessageTextType
import ru.raysmith.tgbot.model.network.chat.ChatAction
import ru.raysmith.tgbot.model.network.media.input.*
import ru.raysmith.tgbot.model.network.message.Message
import ru.raysmith.tgbot.model.network.message.MessageEntity
import ru.raysmith.tgbot.model.network.message.ParseMode
import ru.raysmith.tgbot.utils.withSafeLength

class MediaGroupMessage(override val client: HttpClient) :
    MediaRequest(), IMessage<List<Message>> {

    override var messageThreadId: Int? = null
    override var disableNotification: Boolean? = null
    override var protectContent: Boolean? = null
    override var replyToMessageId: Int? = null
    override var allowSendingWithoutReply: Boolean? = null

    /** send [ChatAction.UPLOAD_PHOTO] action while upload files to telegram server */
    var sendAction = true

    private val inputMedia = mutableListOf<InputMediaGroup>()

    private fun getCaption(caption: String?, safeTextLength: Boolean, parseMode: ParseMode?) = when {
        safeTextLength && parseMode == null -> caption?.withSafeLength(MessageTextType.CAPTION)
        safeTextLength -> caption
        else -> caption
    }

    private fun getCaption(printNulls: Boolean, caption: MessageText.() -> Unit) =
        MessageText(MessageTextType.CAPTION)
            .apply { this.printNulls = printNulls }
            .apply(caption).getTextString()

    private fun getCaptionEntities(printNulls: Boolean, safeTextLength: Boolean, caption: MessageText.() -> Unit) =
        MessageText(MessageTextType.CAPTION)
            .apply {
                this.printNulls = printNulls
                this.safeTextLength = safeTextLength
            }
            .apply(caption).getEntities()

    override fun send(chatId: ChatId): List<Message> {
        return if (inputFiles.isEmpty()) {
            sendMediaGroup(
                chatId = chatId,
                messageThreadId = messageThreadId,
                media = inputMedia,
                disableNotification = disableNotification,
                replyToMessageId = replyToMessageId,
                allowSendingWithoutReply = allowSendingWithoutReply,
            )
        } else {
            if (sendAction) {
                val action = when {
                    inputMedia.all { it is InputMediaVideo } -> ChatAction.UPLOAD_VIDEO
                    inputMedia.all { it is InputMediaPhoto } -> ChatAction.UPLOAD_PHOTO
                    inputMedia.all { it is InputMediaDocument } -> ChatAction.UPLOAD_DOCUMENT
                    inputMedia.isNotEmpty() -> ChatAction.UPLOAD_DOCUMENT
                    else -> null
                }

                if (action != null) {
                    sendChatAction(chatId, action, messageThreadId)
                }
            }

            sendMediaGroup(
                chatId = chatId,
                messageThreadId = messageThreadId,
                media = inputMedia,
                disableNotification = disableNotification,
                replyToMessageId = replyToMessageId,
                allowSendingWithoutReply = allowSendingWithoutReply,
                inputFiles = inputFiles
            )
        }
    }
    
    // TODO docs: not correctly work with the safeLength property when parseMode is not null. Provide hand-made safe caption
    fun photo(
        photo: InputFile, caption: String? = null, parseMode: ParseMode? = null,
        safeTextLength: Boolean = Bot.config.safeTextLength, captionEntities: List<MessageEntity>? = null,
        hasSpoiler: Boolean? = null
    ) {
        inputMedia.add(
            InputMediaPhoto(
                getMedia(photo), getCaption(caption, safeTextLength, parseMode), parseMode, captionEntities, hasSpoiler
            )
        )
    }

    fun photo(
        photo: InputFile, hasSpoiler: Boolean? = null, printNulls: Boolean = Bot.config.printNulls,
        safeTextLength: Boolean = Bot.config.safeTextLength, caption: MessageText.() -> Unit
    ) {
        inputMedia.add(
            InputMediaPhoto(
                getMedia(photo),
                getCaption(printNulls, caption),
                null,
                getCaptionEntities(printNulls, safeTextLength, caption),
                hasSpoiler
            )
        )
    }

    fun video(
        video: InputFile, thumbnail: NotReusableInputFile? = null, caption: String? = null, parseMode: ParseMode? = null,
        captionEntities: List<MessageEntity>? = null, width: Int? = null, height: Int? = null, duration: Int? = null,
        supportsStreaming: Boolean? = null, hasSpoiler: Boolean? = null
    ) {
        inputMedia.add(
            InputMediaVideo(
                getMedia(video), thumbnail?.let { getMedia(it) }, getCaption(caption, false, parseMode),
                parseMode, captionEntities, width, height, duration, supportsStreaming, hasSpoiler
            )
        )
    }

    fun video(
        video: InputFile, thumbnail: NotReusableInputFile? = null, width: Int? = null, height: Int? = null, duration: Int? = null,
        supportsStreaming: Boolean? = null, hasSpoiler: Boolean? = null, printNulls: Boolean = Bot.config.printNulls,
        safeTextLength: Boolean = Bot.config.safeTextLength, caption: MessageText.() -> Unit
    ) {
        inputMedia.add(
            InputMediaVideo(
                getMedia(video), thumbnail?.let { getMedia(it) }, getCaption(printNulls, caption), null,
                getCaptionEntities(printNulls, safeTextLength, caption), width, height, duration, supportsStreaming,
                hasSpoiler
            )
        )
    }

    fun document(
        document: InputFile, thumbnail: NotReusableInputFile? = null, disableContentTypeDetection: Boolean? = null,
        printNulls: Boolean = Bot.config.printNulls, safeTextLength: Boolean = Bot.config.safeTextLength,
        caption: MessageText.() -> Unit
    ) {
        checkMediaTypes<InputMediaDocument>("document")
        inputMedia.add(
            InputMediaDocument(
                getMedia(document), thumbnail?.let { getMedia(it) }, getCaption(printNulls, caption), null,
                getCaptionEntities(printNulls, safeTextLength, caption), disableContentTypeDetection
            )
        )
    }

    fun document(
        document: InputFile, thumbnail: NotReusableInputFile? = null, caption: String? = null, parseMode: ParseMode? = null,
        captionEntities: List<MessageEntity>? = null, disableContentTypeDetection: Boolean? = null
    ) {
        checkMediaTypes<InputMediaDocument>("document")
        inputMedia.add(
            InputMediaDocument(
                getMedia(document), thumbnail?.let { getMedia(it) }, getCaption(caption, false, parseMode), parseMode,
                captionEntities, disableContentTypeDetection
            )
        )
    }

    fun audio(
        audio: InputFile, thumbnail: NotReusableInputFile? = null, caption: String? = null, parseMode: ParseMode? = null,
        captionEntities: List<MessageEntity>? = null, duration: Int? = null, performer: String? = null,
        title: String? = null
    ) {
        checkMediaTypes<InputMediaAudio>("audio")
        inputMedia.add(
            InputMediaAudio(
                getMedia(audio), thumbnail?.let { getMedia(it) }, getCaption(caption, false, parseMode),
                parseMode, captionEntities, duration, performer, title
            )
        )
    }

    fun audio(
        audio: InputFile, thumbnail: NotReusableInputFile? = null, duration: Int? = null, performer: String? = null,
        title: String? = null, printNulls: Boolean = Bot.config.printNulls,
        safeTextLength: Boolean = Bot.config.safeTextLength, caption: MessageText.() -> Unit
    ) {
        checkMediaTypes<InputMediaAudio>("audio")
        inputMedia.add(
            InputMediaAudio(
                getMedia(audio), thumbnail?.let { getMedia(it) }, getCaption(printNulls, caption), null,
                getCaptionEntities(printNulls, safeTextLength, caption), duration, performer, title
            )
        )
    }

    private inline fun <reified T : InputMedia> checkMediaTypes(name: String) = check(inputMedia.all { it is T }) { "$name can't be mixed with other media types" }
}
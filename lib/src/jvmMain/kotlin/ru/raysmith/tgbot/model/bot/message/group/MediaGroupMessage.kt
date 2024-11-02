package ru.raysmith.tgbot.model.bot.message.group

import io.ktor.client.*
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotConfig
import ru.raysmith.tgbot.core.BotHolder
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.bot.message.MessageText
import ru.raysmith.tgbot.model.bot.message.MessageTextType
import ru.raysmith.tgbot.model.bot.message.ExtendedMessage
import ru.raysmith.tgbot.model.network.chat.ChatAction
import ru.raysmith.tgbot.model.network.media.input.*
import ru.raysmith.tgbot.model.network.message.Message
import ru.raysmith.tgbot.model.network.message.MessageEntity
import ru.raysmith.tgbot.model.network.message.ParseMode
import ru.raysmith.tgbot.model.network.message.ReplyParameters
import ru.raysmith.tgbot.utils.withSafeLength

class MediaGroupMessage(override val bot: Bot) : MediaRequest(), ExtendedMessage<List<Message>>, BotHolder {
    override val client: HttpClient = bot.client
    override val botConfig: BotConfig = bot.botConfig

    override var messageThreadId: Int? = null
    override var messageEffectId: String? = null
    override var businessConnectionId: String? = null

    override var disableNotification: Boolean? = null
    override var replyParameters: ReplyParameters? = null
    override var protectContent: Boolean? = null
    override var allowPaidBroadcast: Boolean? = null

    // TODO see MediaMessage.sendChatAction and overrides of it
    /**
     * Send [ChatAction.UPLOAD_PHOTO], [ChatAction.UPLOAD_VIDEO] or [ChatAction.UPLOAD_DOCUMENT] action
     * while upload files to telegram server
     * */
    var sendAction = false

    private val inputMedia = mutableListOf<InputMediaGroup>()

    // TODO [docs] safeTextLength not work when parseMode is not null
    private fun getCaption(caption: String?, safeTextLength: Boolean, parseMode: ParseMode?) = when {
        safeTextLength && parseMode == null -> caption?.withSafeLength(MessageTextType.CAPTION)
        safeTextLength -> caption // should not be cut because MessageText builders are doing their job
        else -> caption
    }

    private fun getCaption(printNulls: Boolean, caption: MessageText.() -> Unit) =
        MessageText(MessageTextType.CAPTION, bot.botConfig)
            .apply { this.printNulls = printNulls }
            .apply(caption).getTextString()

    private fun getCaptionEntities(printNulls: Boolean, safeTextLength: Boolean, caption: MessageText.() -> Unit) =
        MessageText(MessageTextType.CAPTION, bot.botConfig)
            .apply {
                this.printNulls = printNulls
                this.safeTextLength = safeTextLength
            }
            .apply(caption).getEntities()

    override suspend fun send(chatId: ChatId): List<Message> {
        return if (inputFiles.isEmpty()) {
            sendMediaGroup(
                businessConnectionId = businessConnectionId,
                chatId = chatId,
                messageThreadId = messageThreadId,
                media = inputMedia,
                disableNotification = disableNotification,
                protectContent = protectContent,
                allowPaidBroadcast = allowPaidBroadcast,
                messageEffectId = messageEffectId,
                replyParameters = replyParameters
            )
        } else {
            if (sendAction) {
                val action = when {
                    inputMedia.isNotEmpty() && inputMedia.all { it is InputMediaVideo } -> ChatAction.UPLOAD_VIDEO
                    inputMedia.isNotEmpty() && inputMedia.all { it is InputMediaPhoto } -> ChatAction.UPLOAD_PHOTO
                    inputMedia.isNotEmpty() -> ChatAction.UPLOAD_DOCUMENT
                    else -> null
                }

                if (action != null) {
                    sendChatAction(businessConnectionId, chatId, messageThreadId, action)
                }
            }

            sendMediaGroup(
                businessConnectionId = businessConnectionId,
                chatId = chatId,
                messageThreadId = messageThreadId,
                media = inputMedia,
                disableNotification = disableNotification,
                replyParameters = replyParameters,
                protectContent = protectContent,
                allowPaidBroadcast = allowPaidBroadcast,
                messageEffectId = messageEffectId,
                inputFiles = inputFiles
            )
        }
    }
    
    fun photo(
        photo: InputFile, caption: String? = null, parseMode: ParseMode? = null,
        captionEntities: List<MessageEntity>? = null, hasSpoiler: Boolean? = null
    ) {
        checkMediaTypes("photo")
        inputMedia.add(
            InputMediaPhoto(
                applyMedia(photo), getCaption(caption, safeTextLength = false, parseMode), parseMode, captionEntities, 
                hasSpoiler
            )
        )
    }

    fun photo(
        photo: InputFile, hasSpoiler: Boolean? = null, printNulls: Boolean = bot.botConfig.printNulls,
        safeTextLength: Boolean = bot.botConfig.safeTextLength, caption: MessageText.() -> Unit
    ) {
        checkMediaTypes("photo")
        inputMedia.add(
            InputMediaPhoto(
                applyMedia(photo),
                getCaption(printNulls, caption),
                null,
                getCaptionEntities(printNulls, safeTextLength, caption),
                hasSpoiler
            )
        )
    }

    fun video(
        video: InputFile, thumbnail: NotReusableInputFile? = null, caption: String? = null,
        parseMode: ParseMode? = null, captionEntities: List<MessageEntity>? = null,
        showCaptionAboveMedia: Boolean? = null, width: Int? = null, height: Int? = null, duration: Int? = null,
        supportsStreaming: Boolean? = null, hasSpoiler: Boolean? = null
    ) {
        checkMediaTypes("video")
        inputMedia.add(
            InputMediaVideo(
                applyMedia(video), thumbnail?.let { applyMedia(it) },
                getCaption(caption, safeTextLength = false, parseMode), parseMode, captionEntities,
                showCaptionAboveMedia, width, height, duration, supportsStreaming, hasSpoiler
            )
        )
    }

    fun video(
        video: InputFile, thumbnail: NotReusableInputFile? = null, showCaptionAboveMedia: Boolean? = null,
        width: Int? = null, height: Int? = null, duration: Int? = null, supportsStreaming: Boolean? = null,
        hasSpoiler: Boolean? = null, printNulls: Boolean = bot.botConfig.printNulls,
        safeTextLength: Boolean = bot.botConfig.safeTextLength,
        caption: MessageText.() -> Unit
    ) {
        checkMediaTypes("video")
        inputMedia.add(
            InputMediaVideo(
                applyMedia(video), thumbnail?.let { applyMedia(it) }, getCaption(printNulls, caption), null,
                getCaptionEntities(printNulls, safeTextLength, caption), showCaptionAboveMedia, width, height, duration,
                supportsStreaming, hasSpoiler
            )
        )
    }

    fun document(
        document: InputFile, thumbnail: NotReusableInputFile? = null, disableContentTypeDetection: Boolean? = null,
        printNulls: Boolean = bot.botConfig.printNulls, safeTextLength: Boolean = bot.botConfig.safeTextLength,
        caption: MessageText.() -> Unit
    ) {
        checkMediaTypes("document")
        inputMedia.add(
            InputMediaDocument(
                applyMedia(document), thumbnail?.let { applyMedia(it) }, getCaption(printNulls, caption), null,
                getCaptionEntities(printNulls, safeTextLength, caption), disableContentTypeDetection
            )
        )
    }

    fun document(
        document: InputFile, thumbnail: NotReusableInputFile? = null, caption: String? = null,
        parseMode: ParseMode? = null, captionEntities: List<MessageEntity>? = null,
        disableContentTypeDetection: Boolean? = null
    ) {
        checkMediaTypes("document")
        inputMedia.add(
            InputMediaDocument(
                applyMedia(document), thumbnail?.let { applyMedia(it) },
                getCaption(caption, safeTextLength = false, parseMode), parseMode, captionEntities,
                disableContentTypeDetection
            )
        )
    }

    fun audio(
        audio: InputFile, thumbnail: NotReusableInputFile? = null, caption: String? = null,
        parseMode: ParseMode? = null, captionEntities: List<MessageEntity>? = null, duration: Int? = null,
        performer: String? = null, title: String? = null
    ) {
        checkMediaTypes("audio")
        inputMedia.add(
            InputMediaAudio(
                applyMedia(audio), thumbnail?.let { applyMedia(it) },
                getCaption(caption, safeTextLength = false, parseMode), parseMode, captionEntities, duration, performer,
                title
            )
        )
    }

    fun audio(
        audio: InputFile, thumbnail: NotReusableInputFile? = null, duration: Int? = null, performer: String? = null,
        title: String? = null, printNulls: Boolean = bot.botConfig.printNulls,
        safeTextLength: Boolean = bot.botConfig.safeTextLength, caption: MessageText.() -> Unit
    ) {
        checkMediaTypes("audio")
        inputMedia.add(
            InputMediaAudio(
                applyMedia(audio), thumbnail?.let { applyMedia(it) }, getCaption(printNulls, caption), null,
                getCaptionEntities(printNulls, safeTextLength, caption), duration, performer, title
            )
        )
    }

    private fun checkMediaTypes(type: String) = check(inputMedia.all { it.type == type }) {
        "$type can't be mixed with other media types"
    }
}
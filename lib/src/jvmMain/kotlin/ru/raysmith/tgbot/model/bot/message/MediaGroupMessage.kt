package ru.raysmith.tgbot.model.bot.message

import io.ktor.client.HttpClient
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotConfig
import ru.raysmith.tgbot.core.BotHolder
import ru.raysmith.tgbot.model.api.chat.ChatAction
import ru.raysmith.tgbot.model.api.media.input.InputFile
import ru.raysmith.tgbot.model.api.media.input.InputMediaAudio
import ru.raysmith.tgbot.model.api.media.input.InputMediaDocument
import ru.raysmith.tgbot.model.api.media.input.InputMediaGroup
import ru.raysmith.tgbot.model.api.media.input.InputMediaPhoto
import ru.raysmith.tgbot.model.api.media.input.InputMediaVideo
import ru.raysmith.tgbot.model.api.media.input.NotReusableInputFile
import ru.raysmith.tgbot.model.api.message.Message
import ru.raysmith.tgbot.model.api.message.MessageEntity
import ru.raysmith.tgbot.model.api.message.ParseMode
import ru.raysmith.tgbot.model.api.message.ReplyParameters
import ru.raysmith.tgbot.model.api.suggestedpost.SuggestedPostParameters
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.utils.withSafeLength

class MediaGroupMessage(override val bot: Bot) : IMessage<List<Message>>, ThreadMessage, EffectableMessage,
    BusinessMessage, SuggestablePostMessage, BotHolder {

    override val client: HttpClient = bot.client
    override val botConfig: BotConfig = bot.botConfig

    override var messageThreadId: Int? = null
    override var messageEffectId: String? = null
    override var businessConnectionId: String? = null

    override var disableNotification: Boolean? = null
    override var replyParameters: ReplyParameters? = null
    override var protectContent: Boolean? = null
    override var allowPaidBroadcast: Boolean? = null
    override var directMessagesTopicId: Int? = null
    override var suggestedPostParameters: SuggestedPostParameters? = null

    // TODO see MediaMessage.sendChatAction and overrides of it
    /**
     * Send [ru.raysmith.tgbot.model.api.chat.ChatAction.UPLOAD_PHOTO], [ru.raysmith.tgbot.model.api.chat.ChatAction.UPLOAD_VIDEO] or [ru.raysmith.tgbot.model.api.chat.ChatAction.UPLOAD_DOCUMENT] action
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
        if (sendAction && inputMedia.isNotEmpty()) {
            val action = when {
                inputMedia.all { it is InputMediaVideo } -> ChatAction.UPLOAD_VIDEO
                inputMedia.all { it is InputMediaPhoto } -> ChatAction.UPLOAD_PHOTO
                else -> ChatAction.UPLOAD_DOCUMENT
            }
            sendChatAction(businessConnectionId, chatId, messageThreadId, action)
        }

        return sendMediaGroup(
            businessConnectionId = businessConnectionId,
            chatId = chatId,
            messageThreadId = messageThreadId,
            directMessagesTopicId = directMessagesTopicId,
            media = inputMedia,
            disableNotification = disableNotification,
            protectContent = protectContent,
            allowPaidBroadcast = allowPaidBroadcast,
            messageEffectId = messageEffectId,
            replyParameters = replyParameters
        )
    }

    fun photo(
        photo: InputFile,
        caption: String? = null,
        parseMode: ParseMode? = null,
        captionEntities: List<MessageEntity>? = null,
        hasSpoiler: Boolean? = null,
    ) {
        checkMediaTypes("photo")
        inputMedia.add(
            InputMediaPhoto(
                photo,
                getCaption(caption, safeTextLength = false, parseMode),
                parseMode,
                captionEntities,
                hasSpoiler,
            )
        )
    }

    fun photo(
        photo: InputFile,
        hasSpoiler: Boolean? = null,
        printNulls: Boolean = bot.botConfig.printNulls,
        safeTextLength: Boolean = bot.botConfig.safeTextLength,
        caption: MessageText.() -> Unit,
    ) {
        checkMediaTypes("photo")
        inputMedia.add(
            InputMediaPhoto(
                photo,
                getCaption(printNulls, caption),
                null,
                getCaptionEntities(printNulls, safeTextLength, caption),
                hasSpoiler,
            )
        )
    }

    fun video(
        video: InputFile,
        thumbnail: InputFile? = null,
        cover: InputFile? = null,
        caption: String? = null,
        parseMode: ParseMode? = null,
        captionEntities: List<MessageEntity>? = null,
        showCaptionAboveMedia: Boolean? = null,
        width: Int? = null,
        height: Int? = null,
        duration: Int? = null,
        supportsStreaming: Boolean? = null,
        hasSpoiler: Boolean? = null,
        safeTextLength: Boolean = bot.botConfig.safeTextLength,
    ) {
        checkMediaTypes("video")
        inputMedia.add(
            InputMediaVideo(
                video,
                thumbnail,
                cover,
                getCaption(caption, safeTextLength, parseMode),
                parseMode,
                captionEntities,
                showCaptionAboveMedia,
                width,
                height,
                duration,
                supportsStreaming,
                hasSpoiler,
            )
        )
    }

    fun video(
        video: InputFile,
        thumbnail: InputFile? = null,
        cover: InputFile? = null,
        showCaptionAboveMedia: Boolean? = null,
        width: Int? = null,
        height: Int? = null,
        duration: Int? = null,
        supportsStreaming: Boolean? = null,
        hasSpoiler: Boolean? = null,
        printNulls: Boolean = bot.botConfig.printNulls,
        safeTextLength: Boolean = bot.botConfig.safeTextLength,
        caption: MessageText.() -> Unit,
    ) {
        checkMediaTypes("video")
        inputMedia.add(
            InputMediaVideo(
                video,
                thumbnail,
                cover,
                getCaption(printNulls, caption),
                null,
                getCaptionEntities(printNulls, safeTextLength, caption),
                showCaptionAboveMedia,
                width,
                height,
                duration,
                supportsStreaming,
                hasSpoiler,
            )
        )
    }

    fun document(
        document: InputFile,
        thumbnail: NotReusableInputFile? = null,
        disableContentTypeDetection: Boolean? = null,
        printNulls: Boolean = bot.botConfig.printNulls,
        safeTextLength: Boolean = bot.botConfig.safeTextLength,
        caption: MessageText.() -> Unit,
    ) {
        checkMediaTypes("document")
        inputMedia.add(
            InputMediaDocument(
                document,
                thumbnail,
                getCaption(printNulls, caption),
                null,
                getCaptionEntities(printNulls, safeTextLength, caption),
                disableContentTypeDetection,
            )
        )
    }

    fun document(
        document: InputFile,
        thumbnail: NotReusableInputFile? = null,
        caption: String? = null,
        parseMode: ParseMode? = null,
        captionEntities: List<MessageEntity>? = null,
        disableContentTypeDetection: Boolean? = null,
        safeTextLength: Boolean = bot.botConfig.safeTextLength,
    ) {
        checkMediaTypes("document")
        inputMedia.add(
            InputMediaDocument(
                document,
                thumbnail,
                getCaption(caption, safeTextLength, parseMode),
                parseMode,
                captionEntities,
                disableContentTypeDetection,
            )
        )
    }

    fun audio(
        audio: InputFile,
        thumbnail: NotReusableInputFile? = null,
        caption: String? = null,
        parseMode: ParseMode? = null,
        captionEntities: List<MessageEntity>? = null,
        duration: Int? = null,
        performer: String? = null,
        title: String? = null,
        safeTextLength: Boolean = bot.botConfig.safeTextLength,
    ) {
        checkMediaTypes("audio")
        inputMedia.add(
            InputMediaAudio(
                audio,
                thumbnail,
                getCaption(caption, safeTextLength, parseMode),
                parseMode,
                captionEntities,
                duration,
                performer,
                title,
            )
        )
    }

    fun audio(
        audio: InputFile,
        thumbnail: NotReusableInputFile? = null,
        duration: Int? = null,
        performer: String? = null,
        title: String? = null,
        printNulls: Boolean = bot.botConfig.printNulls,
        safeTextLength: Boolean = bot.botConfig.safeTextLength,
        caption: MessageText.() -> Unit,
    ) {
        checkMediaTypes("audio")
        inputMedia.add(
            InputMediaAudio(
                audio,
                thumbnail,
                getCaption(printNulls, caption),
                null,
                getCaptionEntities(printNulls, safeTextLength, caption),
                duration,
                performer,
                title,
            )
        )
    }

    private fun checkMediaTypes(type: String) = check(inputMedia.all { it.type == type }) {
        "$type can't be mixed with other media types"
    }
}

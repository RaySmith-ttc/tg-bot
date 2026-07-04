package ru.raysmith.tgbot.model.bot.message.media

import io.ktor.client.*
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotConfig
import ru.raysmith.tgbot.core.BotHolder
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.bot.message.BusinessMessage
import ru.raysmith.tgbot.model.api.chat.ChatAction
import ru.raysmith.tgbot.model.api.media.input.*
import ru.raysmith.tgbot.model.api.message.Message
import ru.raysmith.tgbot.model.api.suggestedpost.SuggestedPostParameters
import ru.raysmith.tgbot.model.bot.message.SuggestablePostMessage
import ru.raysmith.tgbot.model.bot.message.ThreadMessage
import ru.raysmith.tgbot.network.API

class PaidMediaMessage(override val bot: Bot) : CaptionableMediaMessage(), API, BotHolder, BusinessMessage,
    ThreadMessage, SuggestablePostMessage {

    override val client: HttpClient = bot.client
    override val botConfig: BotConfig = bot.botConfig

    override val mediaName: String = "paid"
    override var sendChatAction: Boolean = bot.botConfig.sendChatActionWithMedaMessage
    override var safeTextLength: Boolean = bot.botConfig.safeTextLength
    override var businessConnectionId: String? = null
    override var messageThreadId: Int? = null
    override var allowPaidBroadcast: Boolean? = null
    override var disableNotification: Boolean? = null
    override var directMessagesTopicId: Int? = null
    override var suggestedPostParameters: SuggestedPostParameters? = null

    private val inputMedia = mutableListOf<InputPaidMedia>()

    /** The number of Telegram Stars that must be paid to buy access to the media */
    var starCount: Int = 0

    /**
     * Bot-defined paid media payload, 0-128 bytes.
     * This will not be displayed to the user, use it for your internal processes.
     * */
    var payload: String? = null

    private suspend fun sendPaidMeda(chatId: ChatId) = sendPaidMedia(
        businessConnectionId = businessConnectionId,
        chatId = chatId,
        messageThreadId = messageThreadId,
        directMessagesTopicId = directMessagesTopicId,
        starCount = starCount,
        media = inputMedia,
        payload = payload,
        caption = getCaptionText(),
        parseMode = getParseModeIfNeed(),
        captionEntities = getEntities(),
        showCaptionAboveMedia = showCaptionAboveMedia,
        disableNotification = disableNotification,
        protectContent = protectContent,
        allowPaidBroadcast = allowPaidBroadcast,
        suggestedPostParameters = suggestedPostParameters,
        replyParameters = replyParameters,
        keyboardMarkup = keyboardMarkup?.toMarkup(),
    )

    override suspend fun send(chatId: ChatId): Message {
        if (sendChatAction && inputMedia.isNotEmpty()) {
            val action = when {
                inputMedia.all { it is InputPaidMediaPhoto } -> ChatAction.UPLOAD_PHOTO
                inputMedia.all { it is InputPaidMediaVideo } -> ChatAction.UPLOAD_VIDEO
                else -> ChatAction.UPLOAD_DOCUMENT
            }
            sendChatAction(businessConnectionId, chatId, messageThreadId, action)
        }

        return sendPaidMeda(chatId)
    }

    fun photo(photo: InputFile) {
        inputMedia.add(InputPaidMediaPhoto(photo))
    }

    fun video(
        video: InputFile,
        thumbnail: InputFile? = null,
        cover: InputFile,
        startTimestamp: Int? = null,
        width: Int? = null,
        height: Int? = null,
        duration: Int? = null,
        supportsStreaming: Boolean? = null,
    ) {
        inputMedia.add(
            InputPaidMediaVideo(
                media = video,
                thumbnail = thumbnail,
                cover = cover,
                startTimestamp = startTimestamp,
                width = width,
                height = height,
                duration = duration,
                supportsStreaming = supportsStreaming,
            )
        )
    }
}

package ru.raysmith.tgbot.model.bot.message.media

import io.ktor.client.*
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotConfig
import ru.raysmith.tgbot.core.BotHolder
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.bot.message.MessageText
import ru.raysmith.tgbot.model.bot.message.MessageTextType
import ru.raysmith.tgbot.model.bot.message.group.MediaRequestWithCaption
import ru.raysmith.tgbot.model.bot.message.group.PrivateChatsAndGroupsMediaRequestWithCaption
import ru.raysmith.tgbot.model.network.chat.ChatAction
import ru.raysmith.tgbot.model.network.media.input.*
import ru.raysmith.tgbot.model.network.message.Message
import ru.raysmith.tgbot.model.network.message.ParseMode
import ru.raysmith.tgbot.model.network.message.ReplyParameters
import ru.raysmith.tgbot.network.API
import ru.raysmith.tgbot.utils.withSafeLength

class PaidMediaMessage(override val bot: Bot) : MediaRequestWithCaption(), API, BotHolder {
    override val client: HttpClient = bot.client
    override val botConfig: BotConfig = bot.botConfig
    override val mediaName: String = "paid"
    override var sendChatAction: Boolean = bot.botConfig.sendChatActionWithMedaMessage
    override var safeTextLength: Boolean = bot.botConfig.safeTextLength

    override var disableNotification: Boolean? = null
    override var protectContent: Boolean? = null
    override var replyParameters: ReplyParameters? = null

    private val inputMedia = mutableListOf<InputPaidMedia>()

    /** The number of Telegram Stars that must be paid to buy access to the media */
    var starCount: Int = 0

    private suspend fun sendPaidMeda(chatId: ChatId) = sendPaidMedia(
        starCount = starCount,
        chatId = chatId,
        media = inputMedia,
        caption = getCaptionText(),
        parseMode = getParseModeIfNeed(),
        captionEntities = getEntities(),
        showCaptionAboveMedia = showCaptionAboveMedia,
        disableNotification = disableNotification,
        protectContent = protectContent,
        replyParameters = replyParameters,
        keyboardMarkup = keyboardMarkup?.toMarkup(),
        inputFiles = inputFiles
    )
    
    override suspend fun send(chatId: ChatId): Message {
        return if (inputFiles.isEmpty()) {
            sendPaidMeda(chatId)
        } else {
            if (sendChatAction) {
                val action = when {
                    inputMedia.all { it is InputPaidMediaPhoto } -> ChatAction.UPLOAD_PHOTO
                    inputMedia.all { it is InputPaidMediaVideo } -> ChatAction.UPLOAD_VIDEO
                    inputMedia.isNotEmpty() -> ChatAction.UPLOAD_DOCUMENT
                    else -> null
                }

                if (action != null) {
                    sendChatAction(null, chatId, null, action)
                }
            }

            sendPaidMeda(chatId)
        }
    }

    fun photo(
        photo: InputFile
    ) {
        inputMedia.add(
            InputPaidMediaPhoto(
                applyMedia(photo)
            )
        )
    }

    fun video(
        video: InputFile, thumbnail: NotReusableInputFile? = null, width: Int? = null, height: Int? = null,
        duration: Int? = null, supportsStreaming: Boolean? = null,
    ) {
        inputMedia.add(
            InputPaidMediaVideo(
                applyMedia(video), thumbnail?.let { applyMedia(it) }, width, height, duration,
                supportsStreaming
            )
        )
    }
}
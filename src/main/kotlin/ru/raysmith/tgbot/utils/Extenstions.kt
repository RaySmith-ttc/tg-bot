package ru.raysmith.tgbot.utils

import kotlinx.serialization.builtins.ListSerializer
import ru.raysmith.tgbot.core.handler.CallbackQueryHandler
import ru.raysmith.tgbot.core.handler.CallbackQueryHandlerData
import ru.raysmith.tgbot.core.handler.MessageHandler
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.bot.message.MessageText
import ru.raysmith.tgbot.model.bot.message.MessageTextType
import ru.raysmith.tgbot.model.network.Location
import ru.raysmith.tgbot.model.network.Poll
import ru.raysmith.tgbot.model.network.Venue
import ru.raysmith.tgbot.model.network.media.*
import ru.raysmith.tgbot.model.network.message.Dice
import ru.raysmith.tgbot.model.network.updates.UpdateType
import ru.raysmith.tgbot.network.TelegramApi
import java.util.*

internal fun List<UpdateType>.asParameter(): String {
    return TelegramApi.json.encodeToJsonElement(ListSerializer(UpdateType.serializer()), this).toString()
}

internal fun Map<*, CallbackQueryHandlerData>.handleAll(handler: CallbackQueryHandler) {
    for (it in this) {
        if (handler.isAnswered) break

        it.value.datePicker?.handle(handler) ?: it.value.handler?.invoke(handler)
    }
}

internal fun Properties?.getOrDefault(key: String, default: String) = this?.getOrDefault(key, default)?.toString() ?: default

/** Append line break */
fun MessageText.n() = text("\n")

fun String.withSafeLength(type: MessageTextType) = if (this.length > type.maxLength) this.take(type.maxLength) else this

fun Long.toChatId(): ChatId.ID = ChatId.of(this) as ChatId.ID
fun String.toChatId(): ChatId.Username = ChatId.of(this) as ChatId.Username

/**
 * Applies a [block] to the message text after [conversion] to another type or returns null if it gets null at any stage
 * */
fun <T> MessageHandler.messageText(conversion: (String) -> T?, block: (T) -> Unit): T? {
    return message.text?.let(conversion)?.also(block)
}

/**
 * Applies a [block] to the message text after [verification] or returns null if it gets null at any stage
 * */
fun MessageHandler.messageText(verification: (String) -> String? = { it }, block: (String) -> Unit): String? =
    messageText<String>(verification, block)

/**
 * Applies a [block] to the largest size of the photo sent or returns null if the message doesn't have the photo
 * */
fun MessageHandler.messagePhoto(block: (PhotoSize) -> Unit): Any? {
    return message.photo?.last()?.also(block)
}

/**
 * Applies a [block] to photos sent or returns null if the message doesn't have photos
 * */
fun MessageHandler.messagePhotos(block: (List<PhotoSize>) -> Unit): Any? {
    return message.photo?.also(block)
}

/**
 * Applies a [block] to the audio sent or returns null if the message doesn't have the audio
 * */
fun MessageHandler.messageAudio(block: (Audio) -> Unit): Any? {
    return message.audio?.also(block)
}

/**
 * Applies a [block] to the document sent or returns null if the message doesn't have the document
 * */
fun MessageHandler.messageDocument(block: (Document) -> Unit): Any? {
    return message.document?.also(block)
}

/**
 * Applies a [block] to the video sent or returns null if the message doesn't have the video
 * */
fun MessageHandler.messageVideo(block: (Video) -> Unit): Any? {
    return message.video?.also(block)
}

/**
 * Applies a [block] to the animation sent or returns null if the message doesn't have the animation
 * */
fun MessageHandler.messageAnimation(block: (Animation) -> Unit): Any? {
    return message.animation?.also(block)
}

/**
 * Applies a [block] to the voice sent or returns null if the message doesn't have the voice
 * */
fun MessageHandler.messageVoice(block: (Voice) -> Unit): Any? {
    return message.voice?.also(block)
}

/**
 * Applies a [block] to the video note sent or returns null if the message doesn't have the video note
 * */
fun MessageHandler.messageVideoNote(block: (VideoNote) -> Unit): Any? {
    return message.videoNote?.also(block)
}

/**
 * Applies a [block] to the location sent or returns null if the message doesn't have the location
 * */
fun MessageHandler.messageLocation(block: (Location) -> Unit): Any? {
    return message.location?.also(block)
}

/**
 * Applies a [block] to the venue sent or returns null if the message doesn't have the venue
 * */
fun MessageHandler.messageVenue(block: (Venue) -> Unit): Any? {
    return message.venue?.also(block)
}

/**
 * Applies a [block] to the contact sent or returns null if the message doesn't have the contact
 * */
fun MessageHandler.messageContact(block: (Contact) -> Unit): Any? {
    return message.contact?.also(block)
}

/**
 * Applies a [block] to the poll sent or returns null if the message doesn't have the poll
 * */
fun MessageHandler.messagePoll(block: (Poll) -> Unit): Any? {
    return message.poll?.also(block)
}

/**
 * Applies a [block] to the dice sent or returns null if the message doesn't have the dice
 * */
fun MessageHandler.messageDice(block: (Dice) -> Unit): Any? {
    return message.dice?.also(block)
}
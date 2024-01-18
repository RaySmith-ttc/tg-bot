package ru.raysmith.tgbot.utils

import ru.raysmith.tgbot.core.handler.base.MessageHandler
import ru.raysmith.tgbot.model.network.Location
import ru.raysmith.tgbot.model.network.Poll
import ru.raysmith.tgbot.model.network.Venue
import ru.raysmith.tgbot.model.network.media.*
import ru.raysmith.tgbot.model.network.message.Dice

/**
 * Applies a [onResult] to the message object after [convert] to another type and [verify] or returns null if [convert] returns null or [verify] returns false
 *
 * Example:
 * ```
 *  messageText<Int>()
 *      .convert { it.toInt() }
 *      .verification { it > 0 }
 *      .use {
 *          send("correct value: $it")
 *      } ?: send("incorrect")
 * ```
 * */
class MessageConverter<T>(private var value: T?, private var hint: String? = null, private var onNull: ((String?) -> Unit)? = null) {

    /** Verifies the current value. Returns null and [hint] in [onResult] and [hint] in [onNull] blocks if it returns false */
    suspend fun verify(hint: String? = null, block: suspend (T) -> Boolean): MessageConverter<T> {
        if (value?.let { block(it) } == false) {
            this.value = null
            this.hint = hint
        }
        return this
    }

    /** Converts type for message object to another. Returns null in [onResult] and [hint] in [onNull] blocks if it returns null*/
    suspend fun <R> convert(hint: String? = null, block: suspend (T) -> R?): MessageConverter<R> {
        return value?.let { MessageConverter(block(it), hint, onNull) } ?: MessageConverter(null, hint, onNull)
    }

    /** Applies [block] if **current** result is null */
    suspend fun onNull(block: suspend (hint: String?) -> Unit): MessageConverter<T> {
        if (value == null) {
            block(hint)
        }
        return this
    }

    /** Applies [block] to result or returns null */
    suspend fun onResult(block: suspend (T) -> Unit): T? {
        if (value != null) {
            block(value!!)
        }

        return value
    }
}

// Text
/** Returns [MessageConverter] instance for message text */
fun MessageHandler.messageText() = MessageConverter(message.text)

/** Applies a [block] to the message text or returns null if the message doesn't contain text. */
suspend fun MessageHandler.messageText(block: suspend (String) -> Unit) = message.text?.also { block(it) }


// Photo
/** Returns [MessageConverter] instance for message photo's biggest size */
fun MessageHandler.messagePhoto() = MessageConverter(message.photo?.last())

/** Applies a [block] to the message photo's biggest size or returns null if message doesn't contain photo */
suspend fun MessageHandler.messagePhoto(block: suspend (PhotoSize) -> Unit) = message.photo?.last()?.also { block(it) }


// Photos
/** Returns [MessageConverter] instance for message photos */
fun MessageHandler.messagePhotos() = MessageConverter(message.photo)

/** Applies a [block] to the message photos or returns null if message doesn't contain photos */
suspend fun MessageHandler.messagePhotos(block: suspend (List<PhotoSize>) -> Unit) = message.photo?.also { block(it) }


// Document
/** Returns [MessageConverter] instance for message document */
fun MessageHandler.messageDocument() = MessageConverter(message.document)

/** Applies a [block] to the message document or returns null if message doesn't contain document */
suspend fun MessageHandler.messageDocument(block: suspend (Document) -> Unit) = message.document?.also { block(it) }


// Image
/** Returns [MessageConverter] instance for message image (document or compressed photo) */
fun MessageHandler.messageImage() = MessageConverter(message.photo?.last() ?: message.document?.let { if (it.mimeType?.startsWith("image/") == true) it else null })

/** Applies a [block] to the message image (document or compressed photo) or returns null if message doesn't contain image */
fun MessageHandler.messageImage(block: (Media) -> Unit) = (message.photo?.last() ?: message.document?.let { if (it.mimeType?.startsWith("image/") == true) it else null })?.also { block(it) }


// Audio
/** Returns [MessageConverter] instance for message audio */
fun MessageHandler.messageAudio() = MessageConverter(message.audio)

/** Applies a [block] to the message audio or returns null if message doesn't contain audio */
suspend fun MessageHandler.messageAudio(block: suspend (Audio) -> Unit) = message.audio?.also { block(it) }


// Video
/** Returns [MessageConverter] instance for message video */
fun MessageHandler.messageVideo() = MessageConverter(message.video)

/** Applies a [block] to the message video or returns null if message doesn't contain video */
suspend fun MessageHandler.messageVideo(block: suspend (Video) -> Unit) = message.video?.also { block(it) }


// Animation
/** Returns [MessageConverter] instance for message animation */
fun MessageHandler.messageAnimation() = MessageConverter(message.animation)

/** Applies a [block] to the message animation or returns null if message doesn't contain animation */
suspend fun MessageHandler.messageAnimation(block: suspend (Animation) -> Unit) = message.animation?.also { block(it) }


// Voice
/** Returns [MessageConverter] instance for message voice */
fun MessageHandler.messageVoice() = MessageConverter(message.voice)

/** Applies a [block] to the message voice or returns null if message doesn't contain voice */
suspend fun MessageHandler.messageVoice(block: suspend (Voice) -> Unit) = message.voice?.also { block(it) }


// VideoNote
/** Returns [MessageConverter] instance for message video note */
fun MessageHandler.messageVideoNote() = MessageConverter(message.videoNote)

/** Applies a [block] to the message video note or returns null if message doesn't contain video note */
suspend fun MessageHandler.messageVideoNote(block: suspend (VideoNote) -> Unit) = message.videoNote?.also { block(it) }


// Location
/** Returns [MessageConverter] instance for message location */
fun MessageHandler.messageLocation() = MessageConverter(message.location)

/** Applies a [block] to the message location or returns null if message doesn't contain location */
suspend fun MessageHandler.messageLocation(block: suspend (Location) -> Unit) = message.location?.also { block(it) }


// Venue
/** Returns [MessageConverter] instance for message venue */
fun MessageHandler.messageVenue() = MessageConverter(message.venue)

/** Applies a [block] to the message venue or returns null if message doesn't contain venue */
suspend fun MessageHandler.messageVenue(block: suspend (Venue) -> Unit) = message.venue?.also { block(it) }


// Contact
/** Returns [MessageConverter] instance for message contact */
fun MessageHandler.messageContact() = MessageConverter(message.contact)

/** Applies a [block] to the message contact or returns null if message doesn't contain contact */
suspend fun MessageHandler.messageContact(block: suspend (Contact) -> Unit) = message.contact?.also { block(it) }


// Poll
/** Returns [MessageConverter] instance for message poll */
fun MessageHandler.messagePoll() = MessageConverter(message.poll)

/** Applies a [block] to the message poll or returns null if message doesn't contain poll */
suspend fun MessageHandler.messagePoll(block: suspend (Poll) -> Unit) = message.poll?.also { block(it) }


// Dice
/** Returns [MessageConverter] instance for message dice */
fun MessageHandler.messageDice() = MessageConverter(message.dice)

/** Applies a [block] to the message dice or returns null if message doesn't contain dice */
suspend fun MessageHandler.messageDice(block: suspend (Dice) -> Unit) = message.dice?.also { block(it) }


// Any media
/** Returns [MessageConverter] instance for any message media */
fun MessageHandler.messageAnyMedia() = MessageConverter(message.getMedia())

/** Applies a [block] to any message media or returns null if message doesn't contain media */
suspend fun MessageHandler.messageAnyMedia(block: suspend (Media) -> Unit) = (
    message.photo?.lastOrNull() ?: message.document ?: message.audio ?: message.video ?:  message.animation ?:
    message.voice ?: message.videoNote
)?.also { block(it) }

// Users shared
/** Returns [MessageConverter] instance for shared users */
fun MessageHandler.messageUsersShared() = MessageConverter(message.usersShared)

// Chat shared
/** Returns [MessageConverter] instance for shared users */
fun MessageHandler.messageChatShared() = MessageConverter(message.chatShared)
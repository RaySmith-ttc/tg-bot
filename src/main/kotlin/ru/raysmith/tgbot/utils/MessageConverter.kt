package ru.raysmith.tgbot.utils

import ru.raysmith.tgbot.core.handler.MessageHandler
import ru.raysmith.tgbot.model.network.Location
import ru.raysmith.tgbot.model.network.Poll
import ru.raysmith.tgbot.model.network.Venue
import ru.raysmith.tgbot.model.network.media.*
import ru.raysmith.tgbot.model.network.message.Dice

/**
 * Applies a [use] to the message object after [convert] to another type and [verify] or returns null if [convert] returns null or [verify] returns false
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
class MessageConverter<T>(private var value: T?) {

    /** Verifies the current value. Returns null in [use] block if it returns false */
    fun verify(block: (T) -> Boolean): MessageConverter<T> {
        if (value?.let(block) == false) {
            value = null
        }
        return this
    }

    /** Converts type for message object to another */
    fun <R> convert(block: (T) -> R?): MessageConverter<R> {
        return value?.let { MessageConverter(block(it)) } ?: MessageConverter(null)
    }

    /** Applies [block] to result or returns null */
    fun use(block: (T) -> Unit): T? {
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
fun MessageHandler.messageText(block: (String) -> Unit) = message.text?.also(block)


// Photo
/** Returns [MessageConverter] instance for message photo's biggest size */
fun MessageHandler.messagePhoto() = MessageConverter(message.photo?.last())

/** Applies a [block] to the message photo's biggest size or returns null if message doesn't contain photo */
fun MessageHandler.messagePhoto(block: (PhotoSize) -> Unit) = message.photo?.last()?.also(block)


// Photos
/** Returns [MessageConverter] instance for message photos */
fun MessageHandler.messagePhotos() = MessageConverter(message.photo)

/** Applies a [block] to the message photos or returns null if message doesn't contain photos */
fun MessageHandler.messagePhotos(block: (List<PhotoSize>) -> Unit) = message.photo?.also(block)


// Document
/** Returns [MessageConverter] instance for message document */
fun MessageHandler.messageDocument() = MessageConverter(message.document)

/** Applies a [block] to the message document or returns null if message doesn't contain document */
fun MessageHandler.messageDocument(block: (Document) -> Unit) = message.document?.also(block)


// Image
/** Returns [MessageConverter] instance for message image (document or compressed photo) */
fun MessageHandler.messageImage() = MessageConverter(message.photo?.last() ?: message.document?.let { if (it.mimeType?.startsWith("image/") == true) it else null })

/** Applies a [block] to the message image (document or compressed photo) or returns null if message doesn't contain image */
fun MessageHandler.messageImage(block: (Media) -> Unit) = (message.photo?.last() ?: message.document?.let { if (it.mimeType?.startsWith("image/") == true) it else null })?.also(block)


// Audio
/** Returns [MessageConverter] instance for message audio */
fun MessageHandler.messageAudio() = MessageConverter(message.audio)

/** Applies a [block] to the message audio or returns null if message doesn't contain audio */
fun MessageHandler.messageAudio(block: (Audio) -> Unit) = message.audio?.also(block)


// Video
/** Returns [MessageConverter] instance for message video */
fun MessageHandler.messageVideo() = MessageConverter(message.video)

/** Applies a [block] to the message video or returns null if message doesn't contain video */
fun MessageHandler.messageVideo(block: (Video) -> Unit) = message.video?.also(block)


// Animation
/** Returns [MessageConverter] instance for message animation */
fun MessageHandler.messageAnimation() = MessageConverter(message.animation)

/** Applies a [block] to the message animation or returns null if message doesn't contain animation */
fun MessageHandler.messageAnimation(block: (Animation) -> Unit) = message.animation?.also(block)


// Voice
/** Returns [MessageConverter] instance for message voice */
fun MessageHandler.messageVoice() = MessageConverter(message.voice)

/** Applies a [block] to the message voice or returns null if message doesn't contain voice */
fun MessageHandler.messageVoice(block: (Voice) -> Unit) = message.voice?.also(block)


// VideoNote
/** Returns [MessageConverter] instance for message video note */
fun MessageHandler.messageVideoNote() = MessageConverter(message.videoNote)

/** Applies a [block] to the message video note or returns null if message doesn't contain video note */
fun MessageHandler.messageVideoNote(block: (VideoNote) -> Unit) = message.videoNote?.also(block)


// Location
/** Returns [MessageConverter] instance for message location */
fun MessageHandler.messageLocation() = MessageConverter(message.location)

/** Applies a [block] to the message location or returns null if message doesn't contain location */
fun MessageHandler.messageLocation(block: (Location) -> Unit) = message.location?.also(block)


// Venue
/** Returns [MessageConverter] instance for message venue */
fun MessageHandler.messageVenue() = MessageConverter(message.venue)

/** Applies a [block] to the message venue or returns null if message doesn't contain venue */
fun MessageHandler.messageVenue(block: (Venue) -> Unit) = message.venue?.also(block)


// Contact
/** Returns [MessageConverter] instance for message contact */
fun MessageHandler.messageContact() = MessageConverter(message.contact)

/** Applies a [block] to the message contact or returns null if message doesn't contain contact */
fun MessageHandler.messageContact(block: (Contact) -> Unit) = message.contact?.also(block)


// Poll
/** Returns [MessageConverter] instance for message poll */
fun MessageHandler.messagePoll() = MessageConverter(message.poll)

/** Applies a [block] to the message poll or returns null if message doesn't contain poll */
fun MessageHandler.messagePoll(block: (Poll) -> Unit) = message.poll?.also(block)


// Dice
/** Returns [MessageConverter] instance for message dice */
fun MessageHandler.messageDice() = MessageConverter(message.dice)

/** Applies a [block] to the message dice or returns null if message doesn't contain dice */
fun MessageHandler.messageDice(block: (Dice) -> Unit) = message.dice?.also(block)


// Any media
/** Returns [MessageConverter] instance for any message media */
fun MessageHandler.messageAnyMedia() = MessageConverter(message.getMedia())

/** Applies a [block] to any message media or returns null if message doesn't contain media */
fun MessageHandler.messageAnyMedia(block: (Media) -> Unit) = (
        message.photo?.lastOrNull() ?: message.document ?: message.audio ?: message.video ?:  message.animation ?:
        message.voice ?: message.videoNote
)?.also(block)

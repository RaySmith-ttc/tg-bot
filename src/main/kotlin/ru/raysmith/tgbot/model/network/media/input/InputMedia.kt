package ru.raysmith.tgbot.model.network.media.input

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable

/**
 * This object represents the content of a media message to be sent. It should be one of
 *
 * - [InputMediaAnimation]
 * - [InputMediaDocument]
 * - [InputMediaAudio]
 * - [InputMediaPhoto]
 * - [InputMediaVideo]
 * */
@Polymorphic
@Serializable
sealed class InputMedia {

    /**
     * File to send. Pass a file_id to send a file that exists on the Telegram servers (recommended),
     * pass an HTTP URL for Telegram to get a file from the Internet, or pass “attach://<file_attach_name>”
     * to upload a new one using multipart/form-data under <file_attach_name> name.
     *
     * @see <a href="https://core.telegram.org/bots/api#sending-files">More info on Sending Files »</a>
     * */
    abstract val media: String

    /** Type of the result */
    abstract val type: String
}

/**
 * This object represents the content of a media message to be sent. It should be one of
 *
 * - [InputMediaDocument]
 * - [InputMediaAudio]
 * - [InputMediaPhoto]
 * - [InputMediaVideo]
 * */
@Polymorphic
@Serializable
sealed class InputMediaGroup : InputMedia()

@Polymorphic
@Serializable
sealed class InputMediaGroupWithThumbnail : InputMediaGroup() {
    /**
     * Thumbnail of the file sent; can be ignored if thumbnail generation for the file is supported server-side.
     * The thumbnail should be in JPEG format and less than 200 kB in size. A thumbnail's width and height
     * should not exceed 320. Ignored if the file is not uploaded using multipart/form-data.
     * Thumbnails can't be reused and can be only uploaded as a new file, so you can pass “attach://<file_attach_name>”
     * if the thumbnail was uploaded using multipart/form-data under <file_attach_name>.
     *
     * @see <a href="https://core.telegram.org/bots/api#sending-files">More info on Sending Files »</a>
     * */
    abstract val thumbnail: String?
}
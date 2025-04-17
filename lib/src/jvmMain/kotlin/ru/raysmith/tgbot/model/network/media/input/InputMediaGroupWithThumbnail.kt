package ru.raysmith.tgbot.model.network.media.input

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable

/**
 * This object represents the content of a media message with thumbnail to be sent. It should be one of
 *
 * - [InputMediaDocument]
 * - [InputMediaAudio]
 * - [InputMediaVideo]
 * */
@Polymorphic // TODO delete?
@Serializable
sealed class InputMediaGroupWithThumbnail : InputMediaGroup() {
    /**
     * Thumbnail of the file sent; can be ignored if thumbnail generation for the file is supported server-side.
     * The thumbnail should be in JPEG format and less than 200 kB in size. A thumbnail's width and height
     * should not exceed 320. Ignored if the file is not uploaded using multipart/form-data.
     * Thumbnails can't be reused and can be only uploaded as a new file, so you can pass “attach://<file_attach_name>”
     * if the thumbnail was uploaded using multipart/form-data under <file_attach_name>.
     * [More info on Sending Files »](https://core.telegram.org/bots/api#sending-files)
     * */
    abstract val thumbnail: String?
}
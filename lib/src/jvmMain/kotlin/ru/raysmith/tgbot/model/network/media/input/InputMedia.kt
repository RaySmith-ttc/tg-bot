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
sealed class InputMedia : InputMediaOrInputPaidMedia {

    /**
     * File to send. Pass a file_id to send a file that exists on the Telegram servers (recommended),
     * pass an HTTP URL for Telegram to get a file from the Internet, or pass “attach://<file_attach_name>”
     * to upload a new one using multipart/form-data under <file_attach_name> name.
     * [More info on Sending Files »](https://core.telegram.org/bots/api#sending-files)
     * */
    abstract val media: String

    /** Type of the result */
    abstract val type: String
}
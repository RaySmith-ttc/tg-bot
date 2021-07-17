package ru.raysmith.tgbot.model.network.inputmedia

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a photo to be sent.
 *
 * @param type 	Type of the result, must be photo
 * @param media File to send. Pass a file_id to send a file that exists on the Telegram servers (recommended), pass an HTTP URL for Telegram to get a file from the Internet, or pass “attach://<file_attach_name>” to upload a new one using multipart/form-data under <file_attach_name> name.
 * @param caption Caption of the photo to be sent, 0-1024 characters after entities parsing
 * @param parseMode Mode for parsing entities in the photo caption.
 *
 * @see <a href="https://core.telegram.org/bots/api#sending-files">More info on Sending Files »</a>
 * */
@Serializable
data class InputMediaPhoto(
    @SerialName("type") val type: String,
    @SerialName("media") val media: String,
    @SerialName("caption") val caption: String? = null,
    @SerialName("parse_mode") val parseMode: String? = null
) : InputMedia()
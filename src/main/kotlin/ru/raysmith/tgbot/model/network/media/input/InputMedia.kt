package ru.raysmith.tgbot.model.network.media.input

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import ru.raysmith.tgbot.network.TelegramApi

/**
 * This object represents the content of a media message to be sent. It should be one of
 *
 * - InputMediaAnimation
 * - InputMediaDocument
 * - InputMediaAudio
 * - [InputMediaPhoto]
 * - InputMediaVideo
 * */
@Polymorphic
@Serializable
sealed class InputMedia {

    /** Type of the result */
    abstract val type: String
}
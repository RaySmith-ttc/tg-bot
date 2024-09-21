package ru.raysmith.tgbot.model.network.media.input

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable

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
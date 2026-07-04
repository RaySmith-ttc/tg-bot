package ru.raysmith.tgbot.model.api.media.input

/**
 * This object represents the content of a media message to be sent. It should be one of
 *
 * - [InputMediaAudio]
 * - [InputMediaDocument]
 * - [InputMediaLivePhoto]
 * - [InputMediaPhoto]
 * - [InputMediaVideo]
 * */
// used just for restriction the media field in the sendMediaGroup API method // TODO move to bot models?
sealed interface InputMediaGroup : InputWithMedia {

    /** Type of the media */
    val type: String
}

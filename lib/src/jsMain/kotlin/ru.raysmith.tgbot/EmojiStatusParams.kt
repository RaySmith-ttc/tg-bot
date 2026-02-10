package ru.raysmith.tgbot

import kotlinx.js.JsPlainObject

/**
 * This object describes additional settings for setting an emoji status.
 *
 * @property duration The duration for which the status will remain set, in seconds.
 * */
@JsPlainObject
external interface EmojiStatusParams {
    val duration: Int?
}


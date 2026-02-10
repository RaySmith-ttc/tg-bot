package ru.raysmith.tgbot.events

import kotlinx.js.JsPlainObject

/**
 * @property isUpdated Whether the token was updated
 * */
@JsPlainObject
external interface BiometricTokenUpdated {
    val isUpdated: Boolean
}

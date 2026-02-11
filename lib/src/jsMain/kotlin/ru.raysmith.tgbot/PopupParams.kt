package ru.raysmith.tgbot

import kotlinx.js.JsPlainObject

// TODO rewrite other docs like this
/**
 * This object describes the native popup.
 *
 * @property message The message to be displayed in the body of the popup, 1-256 characters.
 * @property title *Optional*. The text to be displayed in the popup title, 0-64 characters.
 * @property buttons *Optional*. List of buttons to be displayed in the popup, 1-3 buttons. Set to [{“type”:“close”}]
 *  by default.
 * */
@JsPlainObject
external interface PopupParams {
    val message: String
    val title: String?
    val buttons: Array<PopupButton>?
}

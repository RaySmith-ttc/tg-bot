package ru.raysmith.tgbot

// TODO rewrite other docs like this
/**
 * This object describes the native popup.
 *
 * @property message The message to be displayed in the body of the popup, 1-256 characters.
 * @property title *Optional*. The text to be displayed in the popup title, 0-64 characters.
 * @property buttons *Optional*. List of buttons to be displayed in the popup, 1-3 buttons. Set to [{“type”:“close”}]
 * by default.
 * */
external interface PopupParams {
    var title: String?
    var message: String
    var buttons: Array<PopupButton>?
}
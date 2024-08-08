package ru.raysmith.tgbot

/** This object describes the native popup. */
external interface PopupParams {

    /** The text to be displayed in the popup title, 0-64 characters. */
    var title: String?

    /** The message to be displayed in the body of the popup, 1-256 characters. */
    var message: String

    /** List of buttons to be displayed in the popup, 1-3 buttons. Set to [{“type”:“close”}] by default. */
    var buttons: List<PopupButton>?
}
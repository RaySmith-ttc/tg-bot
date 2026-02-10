package ru.raysmith.tgbot

import kotlinx.js.JsPlainObject

/** This object describes the native popup button. */
@JsPlainObject
external interface PopupButton {

    // TODO link to event
    /**
     * Identifier of the button, 0-64 characters. Set to empty string by default.
     * If the button is pressed, its id is returned in the callback and the *popupClosed* event.
     * */
    val id: String?

    // TODO link to event
    /** Type of the button. Set to [PopupButtonType.default] by default. */
    val type: PopupButtonType?

    /**
     * The text to be displayed on the button, 0-64 characters.
     * Required if [type] is [PopupButtonType.default] or [PopupButtonType.destructive]. Irrelevant for other types.
     * */
    val text: String?
}

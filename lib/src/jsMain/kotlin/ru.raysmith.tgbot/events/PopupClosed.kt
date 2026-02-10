package ru.raysmith.tgbot.events

import kotlinx.js.JsPlainObject

/**
 * @property buttonId Value of the field `id` of the pressed button.
 *  If no buttons were pressed, the field will be `null`.
 * */
@JsPlainObject
external interface PopupClosed {

    /** Value of the field `id` of the pressed button. If no buttons were pressed, the field will be `null`. */
    @JsName("button_id")
    val buttonId: String?
}

package ru.raysmith.tgbot

/** An object containing one or several fields that need to be changed */
external interface MainButtonParams {

    /** button text */
    var text: String

    /** button color */
    var color: String

    /** button text color */
    @JsName("text_color")
    var textColor: String

    /** enable the button */
    @JsName("is_active")
    var isActive: Boolean

    /** show the button */
    @JsName("is_visible")
    var isVisible: Boolean
}
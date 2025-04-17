package ru.raysmith.tgbot

import web.cssom.Color

/** An object containing one or several fields that need to be changed */
external interface MainButtonParams {

    /** button text */
    var text: String

    /** button color */
    var color: Color

    /** button text color */
    @JsName("text_color")
    var textColor: Color

    /**
     * enable shine effect
     *
     * @since Bot API 7.10
     * */
    @JsName("has_shine_effect")
    var hasShineEffect: Color

    /** position of the secondary button */
    var position: BottomButtonPosition

    /** enable the button */
    @JsName("is_active")
    var isActive: Boolean

    /** show the button */
    @JsName("is_visible")
    var isVisible: Boolean
}
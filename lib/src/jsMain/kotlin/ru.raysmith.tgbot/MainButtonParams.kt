package ru.raysmith.tgbot

import kotlinx.js.JsPlainObject
import web.cssom.Color

/** An object containing one or several fields that need to be changed */
@JsPlainObject
external interface MainButtonParams {

    /**
     * button icon emoji id
     *
     * @since Bot API 9.5+
     * */
    @JsName("icon_custom_emoji_id ")
    val iconCustomEmojiId: String?

    /** button text */
    val text: String?

    /** button color */
    val color: Color?

    /** button text color */
    @JsName("text_color")
    val textColor: Color?

    /**
     * enable shine effect
     *
     * @since Bot API 7.10
     * */
    @JsName("has_shine_effect")
    val hasShineEffect: Color?

    /** position of the secondary button */
    val position: BottomButtonPosition?

    /** enable the button */
    @JsName("is_active")
    val isActive: Boolean?

    /** show the button */
    @JsName("is_visible")
    val isVisible: Boolean?
}

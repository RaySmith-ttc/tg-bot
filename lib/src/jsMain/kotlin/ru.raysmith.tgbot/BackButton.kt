package ru.raysmith.tgbot

import web.function.VoidFunction

/**
 * This object controls the **back** button, which can be displayed in the header of the Mini App in the
 * Telegram interface.
 * */
external interface BackButton {

    /** Shows whether the button is visible. Set to *false* by default. */
    val isVisible: Boolean

    /**
     * A method that sets the button press event handler.
     * An alias for [Telegram.WebApp.onEvent][WebApp.onEvent]`(EventType.backButtonClicked, callback)`
     *
     * @since Bot API 6.1
     * */
    val onClick: (callback: VoidFunction) -> BackButton

    /**
     * A method that removes the button press event handler.
     * An alias for [Telegram.WebApp.offEvent][WebApp.offEvent]`(EventType.backButtonClicked, callback)`
     *
     * @since Bot API 6.1
     * */
    val offClick: (callback: VoidFunction) -> BackButton

    /**
     * A method to make the button active and visible.
     *
     * @since Bot API 6.1
     * */
    val show: () -> BackButton

    /**
     * A method to hide the button.
     *
     * @since Bot API 6.1
     * */
    val hide: () -> BackButton
}
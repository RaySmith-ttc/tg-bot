package ru.raysmith.tgbot

import web.scheduling.VoidFunction

/**
 * This object controls the **back** button, which can be displayed in the header of the Mini App in the
 * Telegram interface.
 * */
external interface BackButton {

    /** Shows whether the button is visible. Set to *false* by default. */
    val isVisible: Boolean

    // TODO ? link backButtonClicked
    /**
     * A method that sets the button press event handler.
     * An alias for [Telegram.WebApp.onEvent][WebApp.onEvent]`('backButtonClicked', callback)`
     *
     * @since Bot API 6.1
     * */
    val onClick: (callback: VoidFunction) -> Unit

    // TODO ? link backButtonClicked
    /**
     * A method that removes the button press event handler.
     * An alias for [Telegram.WebApp.offEvent][WebApp.offEvent]`('backButtonClicked', callback)`
     *
     * @since Bot API 6.1
     * */
    val offClick: (callback: VoidFunction) -> Unit

    /**
     * A method to make the button active and visible.
     *
     * @since Bot API 6.1
     * */
    val show: VoidFunction

    /**
     * A method to hide the button.
     *
     * @since Bot API 6.1
     * */
    val hide: VoidFunction
}
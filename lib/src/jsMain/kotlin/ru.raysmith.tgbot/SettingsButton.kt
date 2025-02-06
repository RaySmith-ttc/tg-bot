package ru.raysmith.tgbot

import web.scheduling.VoidFunction

/** This object controls the **Settings** item in the context menu of the Mini App in the Telegram interface. */
external interface SettingsButton {

    /** Shows whether the context menu item is visible. Set to *false* by default. */
    val isVisible: Boolean

    /**
     * A method that sets the press event handler for the Settings item in the context menu.
     * An alias for [Telegram.WebApp.onEvent][WebApp.onEvent]`(`[EventType.settingsButtonClicked]`, callback)`
     *
     * @since Bot API 7.0
     * */
    val onClick: (callback: VoidFunction) -> SettingsButton

    /**
     * A method that removes the press event handler from the Settings item in the context menu.
     * An alias for [Telegram.WebApp.onEvent][WebApp.offEvent]`(`[EventType.settingsButtonClicked]`, callback)`
     *
     * @since Bot API 7.0
     * */
    val offClick: (callback: VoidFunction) -> SettingsButton

    /**
     * A method to make the Settings item in the context menu visible.
     *
     * @since API 7.0
     * */
    val show: () -> SettingsButton

    /**
     * A method to hide the Settings item in the context menu.
     *
     * @since API 7.0
     * */
    val hide: () -> SettingsButton
}


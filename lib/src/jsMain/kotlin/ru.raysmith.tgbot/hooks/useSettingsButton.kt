package ru.raysmith.tgbot.hooks

import kotlinx.js.JsPlainObject
import react.useMemo
import react.useState
import ru.raysmith.tgbot.copyOf
import ru.raysmith.tgbot.webApp
import web.function.VoidFunction

fun useSettingsButton(): SettingsButtonHookType {
    var settingsButton by useState(webApp.SettingsButton)

    val onClick: (callback: VoidFunction) -> Unit = { callback: VoidFunction ->
        settingsButton = copyOf(webApp.SettingsButton.onClick(callback))
    }

    val offClick: (callback: VoidFunction) -> Unit = { callback: VoidFunction ->
        settingsButton = copyOf(webApp.SettingsButton.offClick(callback))
    }

    val show = {
        settingsButton = copyOf(webApp.SettingsButton.show())
    }

    val hide = {
        settingsButton = copyOf(webApp.SettingsButton.hide())
    }

    return useMemo(settingsButton) {
        SettingsButtonHookType(
            isVisible = settingsButton.isVisible,
            onClick = onClick,
            offClick = offClick,
            show = show,
            hide = hide,
        )
    }
}

@JsPlainObject
external interface SettingsButtonHookType {

    /** Shows whether the context menu item is visible. Set to false by default */
    val isVisible: Boolean

    /** A method that sets the press event handler for the Settings item in the context menu */
    val onClick: (callback: VoidFunction) -> Unit

    /** A method that removes the press event handler from the Settings item in the context menu */
    val offClick: (callback: VoidFunction) -> Unit

    /** A method to make the Settings item in the context menu visible */
    val show: VoidFunction

    /** A method to hide the Settings item in the context menu */
    val hide: VoidFunction
}

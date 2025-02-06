package ru.raysmith.tgbot.hooks

import js.objects.jso
import react.useMemo
import react.useState
import ru.raysmith.tgbot.webApp
import web.scheduling.VoidFunction

fun useSettingsButton(): SettingsButtonHookType {
    var isVisible by useState(webApp.SettingsButton.isVisible)

    val onClick: (callback: VoidFunction) -> Unit = { callback: VoidFunction ->
        webApp.SettingsButton.onClick(callback)
    }

    val offClick: (callback: VoidFunction) -> Unit = { callback: VoidFunction ->
        webApp.SettingsButton.offClick(callback)
    }

    val show = {
        isVisible = webApp.SettingsButton.show().isVisible
    }

    val hide = {
        isVisible = webApp.SettingsButton.hide().isVisible
    }

    return useMemo(isVisible) {
        jso {
            this.isVisible = isVisible
            this.onClick = onClick
            this.offClick = offClick
            this.show = show
            this.hide = hide
        }
    }
}

external interface SettingsButtonHookType {

    /** Shows whether the context menu item is visible. Set to false by default */
    var isVisible: Boolean

    /** A method that sets the press event handler for the Settings item in the context menu */
    var onClick: (callback: VoidFunction) -> Unit

    /** A method that removes the press event handler from the Settings item in the context menu */
    var offClick: (callback: VoidFunction) -> Unit

    /** A method to make the Settings item in the context menu visible */
    var show: VoidFunction

    /** A method to hide the Settings item in the context menu */
    var hide: VoidFunction
}
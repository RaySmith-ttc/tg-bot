package ru.raysmith.tgbot.hooks

import js.objects.jso
import react.useMemo
import react.useState
import ru.raysmith.tgbot.copyOf
import ru.raysmith.tgbot.webApp
import web.function.VoidFunction

/**
 * This hook controls the back button, which can be displayed in the header of the Mini App in the Telegram interface.
 * */
fun useBackButton(): BackButtonHookType {
    var backButton by useState(webApp.BackButton)

    val onClick: (callback: VoidFunction) -> Unit = { callback: VoidFunction ->
        backButton = copyOf(webApp.BackButton.onClick(callback))
    }

    val offClick: (callback: VoidFunction) -> Unit = { callback: VoidFunction ->
        backButton = copyOf(webApp.BackButton.offClick(callback))
    }

    val show = {
        backButton = copyOf(webApp.BackButton.show())
    }

    val hide = {
        backButton = copyOf(webApp.BackButton.hide())
    }

    return useMemo(backButton) {
        jso {
            this.isVisible = backButton.isVisible
            this.onClick = onClick
            this.offClick = offClick
            this.show = show
            this.hide = hide
        }
    }
}

// TODO simplify javadoc in other hooks like this
external interface BackButtonHookType {

    /** Shows whether the button is visible. */
    var isVisible: Boolean

    /** A method that sets the button press event handler. */
    var onClick: (callback: VoidFunction) -> Unit

    /** A method that removes the button press event handler */
    var offClick: (callback: VoidFunction) -> Unit

    /** A method to make the button active and visible */
    var show: VoidFunction

    /** A method to hide the button */
    var hide: VoidFunction
}
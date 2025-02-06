package ru.raysmith.tgbot.hooks

import js.objects.jso
import react.*
import ru.raysmith.tgbot.EventType
import ru.raysmith.tgbot.events.ViewportChangedEvent
import ru.raysmith.tgbot.webApp
import web.scheduling.VoidFunction

/**
 * This hook provides information and control over the state of the App.
 * */
fun useWebAppState(): WebAppStateHookType {
    var isActive by useState(webApp.isActive)
    var isClosingConfirmationEnabled by useState(webApp.isClosingConfirmationEnabled)

    useEffectOnce {
        webApp.onEvent(EventType.activated) {
            isActive = true
        }
        webApp.onEvent(EventType.deactivated) {
            isActive = false
        }
    }

    val enableClosingConfirmation = {
        webApp.enableClosingConfirmation()
        isClosingConfirmationEnabled = true
    }

    val disableClosingConfirmation = {
        webApp.disableClosingConfirmation()
        isClosingConfirmationEnabled = false
    }

    return useMemo(
        isActive,
        isClosingConfirmationEnabled,
    ) {
        jso {
            this.isActive = isActive
            this.isClosingConfirmationEnabled = isClosingConfirmationEnabled
            this.enableClosingConfirmation = enableClosingConfirmation
            this.disableClosingConfirmation = disableClosingConfirmation
        }
    }
}

external interface WebAppStateHookType {

    /**
     * *True*, if the Mini App is currently active. *False*, if the Mini App is minimized.
     * @since Bot API 8.0
     * */
    var isActive: Boolean

    /**
     * *True*, if the confirmation dialog is enabled while the user is trying to close the Mini App.
     * *False*, if the confirmation dialog is disabled.
     * */
    var isClosingConfirmationEnabled: Boolean

    /**
     * A method that enables a confirmation dialog while the user is trying to close the Mini App.
     *
     * @since Bot API 6.2
     * */
    var enableClosingConfirmation: VoidFunction

    /**
     * A method that disables the confirmation dialog while the user is trying to close the Mini App.
     *
     * @since Bot API 6.2
     * */
    var disableClosingConfirmation: VoidFunction
}
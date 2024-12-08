package ru.raysmith.tgbot.hooks

import js.objects.jso
import react.useEffectOnce
import react.useMemo
import react.useState
import ru.raysmith.tgbot.*
import ru.raysmith.tgbot.events.ViewportChangedEvent
import web.scheduling.VoidFunction

fun useInsets(): InsetsHookType {
    var safeArea by useState(webApp.safeAreaInset)
    var contentSafeArea by useState(webApp.contentSafeAreaInset)

    useEffectOnce {
        webApp.onEvent(EventType.safeAreaChanged) {
            safeArea = webApp.safeAreaInset
        }
        webApp.onEvent(EventType.contentSafeAreaChanged) {
            contentSafeArea = webApp.contentSafeAreaInset
        }
    }

    return useMemo(
        safeArea,
        contentSafeArea,
    ) {
        jso {
            this.safeArea = safeArea
            this.contentSafeArea = contentSafeArea
        }
    }
}

external interface InsetsHookType {

    /**
     * An object representing the device's safe area insets, accounting for system UI elements like notches or
     * navigation bars.
     * */
    var safeArea: SafeAreaInset

    /**
     * An object representing the safe area for displaying content within the app, free from overlapping
     * Telegram UI elements.
     * */
    var contentSafeArea: ContentSafeAreaInset
}
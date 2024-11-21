package ru.raysmith.tgbot.hooks

import js.objects.jso
import react.useCallback
import react.useMemo
import react.useState
import ru.raysmith.tgbot.webApp
import web.scheduling.VoidFunction

fun useBackButton(): BackButtonHookType {
    var bb by useState(webApp.BackButton)

    val onClick = useCallback(bb) { callback: VoidFunction ->
        bb = bb.onClick(callback)
    }

    val offClick = useCallback(bb) { callback: VoidFunction ->
        bb = bb.offClick(callback)
    }

    val show = useCallback(bb) {
        bb = bb.show()
    }

    val hide = useCallback(bb) {
        bb = bb.hide()
    }

    return useMemo(bb) {
        jso {
            this.isVisible = bb.isVisible
            this.onClick = onClick
            this.offClick = offClick
            this.show = show
            this.hide = hide
        }
    }
}

external interface BackButtonHookType {
    var isVisible: Boolean
    var onClick: (callback: VoidFunction) -> Unit
    var offClick: (callback: VoidFunction) -> Unit
    var show: VoidFunction
    var hide: VoidFunction
}
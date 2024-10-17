package ru.raysmith.tgbot.hooks

import js.objects.jso
import react.useEffect
import react.useEffectOnce
import react.useMemo
import react.useState
import ru.raysmith.tgbot.EventType
import ru.raysmith.tgbot.webApp

fun useViewportChanged(): ViewportChangedType {
    var viewportHeight by useState(webApp.viewportHeight)
    var viewportStableHeight by useState(webApp.viewportStableHeight)
    var isExpanded by useState(webApp.isExpanded)
    var isStateStable by useState(true)

    useEffectOnce {
        val event = { payload: dynamic ->
            println("asd")
            if (payload.isStateStable as Boolean) {
                isExpanded = webApp.isExpanded
            }

            viewportHeight = webApp.viewportHeight
            viewportStableHeight = webApp.viewportStableHeight
            isStateStable = payload.isStateStable as Boolean
        }

        println("add event")
        webApp.onEvent("viewportChanged".asDynamic(), event)

//        return@useEffect {
//            webApp.offEvent(EventType.viewportChanged, event)
//        }.unsafeCast<Unit>()
    }

    return useMemo(viewportHeight, viewportStableHeight, isExpanded, isStateStable) {
        jso {
            this.viewportHeight = viewportHeight
            this.viewportStableHeight = viewportStableHeight
            this.isExpanded = isExpanded
            this.isStateStable = isStateStable
        }
    }
}

external interface ViewportChangedType {
    var viewportHeight: Float
    var viewportStableHeight: Float
    var isExpanded: Boolean
    var isStateStable: Boolean
}
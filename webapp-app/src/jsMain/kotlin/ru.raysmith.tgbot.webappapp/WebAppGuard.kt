package ru.raysmith.tgbot.webappapp

import js.process
import kotlinx.coroutines.launch
import mui.material.Alert
import react.FC
import react.PropsWithChildren
import react.useEffectOnce
import react.useState
import ru.raysmith.tgbot.webApp
import ru.raysmith.tgbot.webappapp.components.FullPageLoading

val WebAppGuard = FC<PropsWithChildren> { props ->
    var validated by useState(if (process.env.WEBAPP_GUARD_ENABLED) null else true)

    println("process.env.WEBAPP_GUARD_ENABLED: ${process.env.WEBAPP_GUARD_ENABLED}")

    useEffectOnce {
        mainScope.launch {
            validated = API.verifyInitData(webApp.initData)
        }
    }

    if (validated == null) {
        FullPageLoading {

        }
        return@FC
    }

    if (process.env.WEBAPP_GUARD_ENABLED && !validated!!) {
        Alert {
            +"Init data is not validated"
            severity = mui.material.AlertColor.error.unsafeCast<String>()
        }
    } else {
        +props.children
    }
}
package ru.raysmith.tgbot.webappapp

import js.process
import kotlinx.coroutines.launch
import mui.material.Box
import mui.material.CircularProgress
import mui.material.Typography
import mui.system.sx
import react.FC
import react.PropsWithChildren
import react.useEffectOnce
import react.useState
import ru.raysmith.tgbot.webApp
import web.cssom.AlignItems
import web.cssom.Display
import web.cssom.JustifyContent
import web.cssom.vh

val WebAppGuard = FC<PropsWithChildren> { props ->
    var verified by useState(if (process.env.WEBAPP_GUARD_ENABLED) null else true)

    useEffectOnce {
        apiScope.launch {
            verified = API.verifyInitData(webApp.initData)
        }
    }

    if (verified == null) {
        Box {
            sx {
                width = 100.vh
                height = 100.vh
                display = Display.flex
                justifyContent = JustifyContent.center
                alignItems = AlignItems.center
            }

            CircularProgress {

            }
        }
        return@FC
    }

    if (process.env.WEBAPP_GUARD_ENABLED && !verified!!) { // TODO improve unverified page
        Typography {
            +"Not verified"
        }
    } else {
        +props.children
    }
}
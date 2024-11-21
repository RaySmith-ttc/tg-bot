package ru.raysmith.tgbot.webappapp

import js.process
import kotlinx.coroutines.launch
import mui.material.Typography
import react.FC
import react.PropsWithChildren
import react.useEffectOnce
import react.useState
import ru.raysmith.tgbot.webApp
import ru.raysmith.tgbot.webappapp.components.FullPageLoading

val WebAppGuard = FC<PropsWithChildren> { props ->
    var verified by useState(if (process.env.WEBAPP_GUARD_ENABLED) null else true)

    useEffectOnce {
        mainScope.launch {
            verified = API.verifyInitData(webApp.initData)
        }
    }

    if (verified == null) {
        FullPageLoading {

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
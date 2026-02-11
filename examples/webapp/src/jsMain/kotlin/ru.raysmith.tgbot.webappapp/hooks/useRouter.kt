package ru.raysmith.tgbot.webappapp.hooks

import js.objects.unsafeJso
import kotlinx.browser.window
import react.useMemo
import tanstack.react.router.useRouter

external interface RouterProps {
    var back: () -> Unit
    var forward: () -> Unit
    var reload: () -> Unit
    var push: (href: String) -> Unit
//    var replace: (href: String) -> Unit
}

fun useRouter(): RouterProps {
    val router = useRouter()

    return useMemo(router) {
        unsafeJso {
            back = { router.asDynamic().history.back() }
            forward = { router.asDynamic().history.forward() }
            reload = { window.location.reload() }
            push = { router.asDynamic().navigate(to = it) }
//            replace = { router.asDynamic().navigate(to = it, replace = true) }
        }
    }
}

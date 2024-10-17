package ru.raysmith.tgbot.webappapp.hooks

import js.objects.jso
import kotlinx.browser.window
import react.router.useNavigate
import react.useMemo

external interface RouterProps {
    var back: () -> Unit
    var forward: () -> Unit
    var reload: () -> Unit
    var push: (href: String) -> Unit
    var replace: (href: String) -> Unit
}

fun useRouter(): RouterProps {
    val navigate = useNavigate()
    return useMemo(navigate) {
        jso {
            back = { navigate(-1) }
            forward = { navigate(1) }
            reload = { window.location.reload() }
            push = { navigate(it) }
            replace = { navigate(it, jso { replace = true }) }
        }
    }
}
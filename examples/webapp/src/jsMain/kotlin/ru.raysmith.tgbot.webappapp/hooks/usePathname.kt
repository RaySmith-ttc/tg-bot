package ru.raysmith.tgbot.webappapp.hooks

import react.useMemo
import tanstack.react.router.useLocation

fun usePathname(): String {
    val pathname = useLocation().pathname

    return useMemo(pathname) { pathname }
}

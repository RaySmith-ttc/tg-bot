package ru.raysmith.tgbot.webappapp.hooks

import react.router.useLocation
import react.useMemo

fun usePathname(): String {
    val pathname = useLocation().pathname

    return useMemo(pathname) { pathname }
}
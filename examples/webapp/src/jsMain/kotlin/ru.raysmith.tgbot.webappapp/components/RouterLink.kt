package ru.raysmith.tgbot.webappapp.components

import mui.material.LinkProps
import react.FC
import tanstack.react.router.Link
import tanstack.router.core.RoutePath

external interface RouterLinkProps : LinkProps

val RouterLink = FC<RouterLinkProps> { props ->
    Link {
        ref = props.ref
        to = RoutePath(props.href ?: error("href is required"))
        +props
    }
}

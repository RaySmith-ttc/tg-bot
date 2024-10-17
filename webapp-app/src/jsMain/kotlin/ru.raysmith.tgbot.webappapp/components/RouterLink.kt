package ru.raysmith.tgbot.webappapp.components

import mui.material.LinkProps
import react.ForwardRef
import react.router.dom.Link

external interface RouterLinkProps : LinkProps

val RouterLink = ForwardRef<RouterLinkProps> { props ->
    Link {
        ref = props.ref
        to = props.href ?: error("href is required")
        +props
    }
}

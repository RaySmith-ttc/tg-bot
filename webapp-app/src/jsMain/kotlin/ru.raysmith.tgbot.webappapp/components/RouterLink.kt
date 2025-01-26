package ru.raysmith.tgbot.webappapp.components

import mui.material.LinkProps
import react.FC
import react.router.dom.Link

external interface RouterLinkProps : LinkProps

val RouterLink = FC<RouterLinkProps> { props ->
    Link {
        ref = props.ref
        to = props.href ?: error("href is required")
        +props
    }
}

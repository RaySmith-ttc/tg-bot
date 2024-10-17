package ru.raysmith.tgbot.webappapp.components

import mui.icons.material.ArrowBack
import mui.material.*
import mui.material.styles.TypographyVariant
import mui.system.sx
import react.FC
import react.PropsWithChildren
import react.dom.html.ReactHTML
import ru.raysmith.tgbot.webappapp.hooks.usePathname
import ru.raysmith.tgbot.webappapp.hooks.useRouter
import ru.raysmith.tgbot.webappapp.router.Paths
import ru.raysmith.tgbot.webappapp.wrappers.mr
import web.cssom.number

external interface HeaderProps : PropsWithChildren

val Header = FC<HeaderProps> { props ->
    val router = useRouter()
    val pathname = usePathname()
    console.log(pathname)

    Box {
        sx { flexGrow = number(1.0) }

        AppBar {
            position = AppBarPosition.fixed

            Toolbar {
                if (pathname != Paths.root) {
                    IconButton {
                        size = Size.large
                        edge = IconButtonEdge.start
                        color = IconButtonColor.inherit
                        sx { mr = 2 }

                        ArrowBack {
                            onClick = {
                                router.back()
                            }
                        }
                    }
                }

                Typography {
                    variant = TypographyVariant.h6
                    component = ReactHTML.div
                    sx { flexGrow = number(1.0) }
                    +"WebApp"
                }

                +props.children
            }
        }
    }

    Toolbar {

    }
}
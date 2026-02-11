package ru.raysmith.tgbot.webappapp.components

import kotlinx.browser.window
import mui.icons.material.ArrowBack
import mui.material.*
import mui.material.styles.TypographyVariant
import mui.system.sx
import react.FC
import react.PropsWithChildren
import react.dom.html.ReactHTML.div
import ru.raysmith.tgbot.CssVar
import ru.raysmith.tgbot.hooks.useViewport
import ru.raysmith.tgbot.webappapp.hooks.usePathname
import ru.raysmith.tgbot.webappapp.hooks.useRouter
import ru.raysmith.tgbot.webappapp.router.Paths
import ru.raysmith.tgbot.webappapp.wrappers.mr
import web.cssom.Auto
import web.cssom.PaddingBottom
import web.cssom.number
import web.cssom.px
import web.dom.Element

external interface HeaderProps : PropsWithChildren

val AppBar = FC<HeaderProps> { props ->
    val router = useRouter()
    val pathname = usePathname()
    val viewport = useViewport()

    if (viewport.isExpanded) {
        return@FC
    }

    Box {
        sx {
            flexGrow = number(1.0)
        }

        Slide {
            direction = SlideDirection.up
            `in` = viewport.isStateStable
            asDynamic().mountOnEnter = true
            asDynamic().unmountOnExit = true
            container = window.document.body.unsafeCast<Element>()

            AppBar {
                position = AppBarPosition.fixed
                sx {
                    top = Auto.auto
                    bottom = "calc(100vh - ${CssVar.tgViewportHeight(0.px)})".unsafeCast<PaddingBottom>()
                }

                Toolbar {
                    if (!viewport.isExpanded && pathname != Paths.root) {
                        IconButton {
                            edge = IconButtonEdge.start
                            color = IconButtonColor.inherit
                            sx { mr = 2 }
                            onClick = {
                                router.back()
                            }

                            ArrowBack {}
                        }
                    }

                    Typography {
                        variant = TypographyVariant.h6
                        component = div
                        sx { flexGrow = number(1.0) }
                        +"WebApp"
                    }

                    +props.children
                }
            }
        }
    }
}
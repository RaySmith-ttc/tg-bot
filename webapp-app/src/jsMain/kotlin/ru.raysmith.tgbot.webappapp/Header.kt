package ru.raysmith.tgbot.webappapp

import mui.icons.material.ArrowBack
import mui.material.*
import mui.material.styles.TypographyVariant
import mui.system.sx
import react.FC
import react.Props
import react.dom.html.ReactHTML
import web.cssom.number

val Header = FC<Props> {
    val router = useRouter()
    val pathname = usePathname()

    Box {
        sx { flexGrow = number(1.0) }

        AppBar {
            position = AppBarPosition.fixed

            Toolbar {
                IconButton {
                    size = Size.large
                    edge = IconButtonEdge.start
                    color = IconButtonColor.inherit
                    sx { mr = 2 }

                    if (pathname != Paths.root) {
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
            }
        }
    }
}
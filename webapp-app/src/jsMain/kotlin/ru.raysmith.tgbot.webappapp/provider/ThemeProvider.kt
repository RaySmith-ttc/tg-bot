package ru.raysmith.tgbot.webappapp.provider

import js.objects.jso
import mui.material.AppBarProps
import mui.material.ButtonProps
import mui.material.CssBaseline
import mui.material.LinkProps
import mui.material.styles.createTheme
import mui.system.sx
import react.FC
import react.PropsWithChildren
import ru.raysmith.tgbot.hooks.useThemeParams
import web.cssom.Color

val ThemeProvider = FC<PropsWithChildren> { props ->
    val settings = useSettingsContext()
    val tgTheme = useThemeParams()

    mui.material.styles.ThemeProvider {
        this.theme = createTheme(
            if (settings.useTgTheme) jso {
                components = jso {
                    MuiLink = jso {
                        defaultProps = jso<LinkProps> {
                            color = tgTheme.linkColor
                        }
                    }
                    MuiButton = jso {
                        defaultProps = jso<ButtonProps> {
                            sx {
                                backgroundColor = tgTheme.buttonColor
                                color = tgTheme.buttonTextColor
                            }
                        }
                    }
                    MuiAppBar = jso {
                        defaultProps = jso<AppBarProps> {
                            sx {
                                backgroundColor = tgTheme.headerBgColor
                                color = tgTheme.headerColor
                            }
                        }
                    }
                }
                palette = jso {
                    background = jso {
                        default = tgTheme.bgColor.unsafeCast<String>()
                        paper = tgTheme.secondaryBgColor.unsafeCast<String>()
                    }
                    text = jso {
                        primary = tgTheme.textColor ?: Color.currentcolor
                    }
                    divider = tgTheme.hintColor.unsafeCast<String>()
                }
            } else jso {}
        )
        CssBaseline {}
        +props.children
    }
}
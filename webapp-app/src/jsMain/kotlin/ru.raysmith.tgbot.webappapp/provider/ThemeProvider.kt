package ru.raysmith.tgbot.webappapp.provider

import js.objects.jso
import mui.material.*
import mui.material.styles.PaletteColor
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
                    mode = tgTheme.colorScheme.unsafeCast<PaletteMode>()
                    background = jso {
                        default = settings.backgroundColor ?: tgTheme.backgroundColor.unsafeCast<String>()
                        paper = tgTheme.secondaryBgColor.unsafeCast<String>()
                    }
                    text = jso {
                        primary = tgTheme.textColor ?: Color.currentcolor
                    }
                    divider = tgTheme.hintColor.unsafeCast<String>()
                    primary = jso {
                        main = tgTheme.buttonColor.unsafeCast<PaletteColor>()
                    }
                    secondary = jso {
                        main = tgTheme.accentTextColor.unsafeCast<PaletteColor>()
                    }
                    error = jso {
                        main = tgTheme.destructiveTextColor.unsafeCast<PaletteColor>()
                    }
                }
                typography = jso {
                    fontSize = 12
                }
            } else jso {}
        )
        CssBaseline {}
        +props.children
    }
}
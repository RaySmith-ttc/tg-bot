package ru.raysmith.tgbot.webappapp.provider

import csstype.PropertiesBuilder
import js.objects.jso
import mui.material.CssBaseline
import mui.material.LinkProps
import mui.material.PaletteMode
import mui.material.styles.PaletteColor
import mui.material.styles.createTheme
import react.FC
import react.PropsWithChildren
import ru.raysmith.tgbot.hooks.useThemeParams
import web.cssom.Border
import web.cssom.Color
import web.cssom.LineStyle
import web.cssom.px

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
                    MuiTableCell = jso {
                        styleOverrides = jso {
                            root = jso<PropertiesBuilder> {
                                if (tgTheme.hintColor != null) {
                                    borderBottom = Border(1.px, LineStyle.solid, tgTheme.hintColor!!)
                                }
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
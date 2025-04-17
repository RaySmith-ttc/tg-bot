package ru.raysmith.tgbot.webappapp.provider

import csstype.PropertiesBuilder
import js.objects.jso
import mui.material.CssBaseline
import mui.material.LinkProps
import mui.material.PaletteMode
import mui.material.Size
import mui.material.styles.PaletteColor
import mui.material.styles.createTheme
import react.FC
import react.PropsWithChildren
import ru.raysmith.tgbot.hooks.useThemeParams
import ru.raysmith.tgbot.webappapp.isNotNullOrUndefined
import web.cssom.Border
import web.cssom.LineStyle
import web.cssom.px

val ThemeProvider = FC<PropsWithChildren> { props ->
    val settings = useSettingsContext()
    val tgTheme = useThemeParams()

    mui.material.styles.ThemeProvider {
        this.theme = createTheme(
            if (settings.useTgTheme) jso {
                components = jso {
                    if (tgTheme.linkColor.isNotNullOrUndefined()) {
                        MuiLink = jso {
                            defaultProps = jso<LinkProps> {
                                color = tgTheme.linkColor
                            }
                        }
                    }
                    if (tgTheme.hintColor.isNotNullOrUndefined()) {
                        MuiTableCell = jso {
                            styleOverrides = jso {
                                root = jso<PropertiesBuilder> {
                                    borderBottom = Border(1.px, LineStyle.solid, tgTheme.hintColor!!)
                                }
                            }
                        }
                    }
                    MuiButtonGroup = jso {
                        defaultProps = jso {
                            size = Size.large
                        }
                    }
                    MuiButton = jso {
                        defaultProps = jso {
                            size = Size.large
                        }
                    }
                }
                palette = jso {
                    mode = tgTheme.colorScheme.unsafeCast<PaletteMode>()
                    background = jso {
                        if (settings.backgroundColor.isNotNullOrUndefined() || tgTheme.backgroundColor != undefined) {
                            default = settings.backgroundColor ?: tgTheme.backgroundColor.unsafeCast<String>()
                        }
                        if (tgTheme.secondaryBgColor.isNotNullOrUndefined()) {
                            paper = tgTheme.secondaryBgColor.unsafeCast<String>()
                        }
                    }
                    text = jso {
                        if (tgTheme.textColor.isNotNullOrUndefined()) {
                            primary = tgTheme.textColor!!
                        }
                    }
                    if (tgTheme.hintColor.isNotNullOrUndefined()) {
                        divider = tgTheme.hintColor.unsafeCast<String>()
                    }
                    if (tgTheme.buttonColor.isNotNullOrUndefined()) {
                        primary = jso {
                            main = tgTheme.buttonColor.unsafeCast<PaletteColor>()
                        }
                    }
                    if (tgTheme.accentTextColor.isNotNullOrUndefined()) {
                        secondary = jso {
                            main = tgTheme.accentTextColor.unsafeCast<PaletteColor>()
                        }
                    }

                    if (tgTheme.destructiveTextColor.isNotNullOrUndefined()) {
                        error = jso {
                            main = tgTheme.destructiveTextColor?.unsafeCast<PaletteColor>()
                        }
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


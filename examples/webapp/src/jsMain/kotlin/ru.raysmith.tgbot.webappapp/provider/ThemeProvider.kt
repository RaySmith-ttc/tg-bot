package ru.raysmith.tgbot.webappapp.provider

import js.objects.unsafeJso
import mui.material.CssBaseline
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
            if (settings.useTgTheme) unsafeJso {
                components = unsafeJso {
                    if (tgTheme.linkColor.isNotNullOrUndefined()) {
                        MuiLink = unsafeJso {
                            defaultProps = unsafeJso {
                                color = tgTheme.linkColor
                            }
                        }
                    }
                    if (tgTheme.hintColor.isNotNullOrUndefined()) {
                        MuiTableCell = unsafeJso {
                            styleOverrides = unsafeJso {
                                root = unsafeJso {
                                    borderBottom = Border(1.px, LineStyle.solid, tgTheme.hintColor!!)
                                }
                            }
                        }
                    }
                    MuiButtonGroup = unsafeJso {
                        defaultProps = unsafeJso {
                            size = Size.large
                        }
                    }
                    MuiButton = unsafeJso {
                        defaultProps = unsafeJso {
                            size = Size.large
                        }
                    }
                }
                palette = unsafeJso {
                    mode = tgTheme.colorScheme.unsafeCast<PaletteMode>()
                    background = unsafeJso {
                        @Suppress("SENSELESS_COMPARISON")
                        if (settings.backgroundColor.isNotNullOrUndefined() || tgTheme.backgroundColor != undefined) {
                            default = settings.backgroundColor ?: tgTheme.backgroundColor.unsafeCast<String>()
                        }
                        if (tgTheme.secondaryBgColor.isNotNullOrUndefined()) {
                            paper = tgTheme.secondaryBgColor.unsafeCast<String>()
                        }
                    }
                    text = unsafeJso {
                        if (tgTheme.textColor.isNotNullOrUndefined()) {
                            primary = tgTheme.textColor!!
                        }
                    }
                    if (tgTheme.hintColor.isNotNullOrUndefined()) {
                        divider = tgTheme.hintColor.unsafeCast<String>()
                    }
                    if (tgTheme.buttonColor.isNotNullOrUndefined()) {
                        primary = unsafeJso {
                            main = tgTheme.buttonColor.unsafeCast<PaletteColor>()
                        }
                    }
                    if (tgTheme.accentTextColor.isNotNullOrUndefined()) {
                        secondary = unsafeJso {
                            main = tgTheme.accentTextColor.unsafeCast<PaletteColor>()
                        }
                    }

                    if (tgTheme.destructiveTextColor.isNotNullOrUndefined()) {
                        error = unsafeJso {
                            main = tgTheme.destructiveTextColor?.unsafeCast<PaletteColor>()
                        }
                    }
                }
                typography = unsafeJso {
                    fontSize = 12
                }
            } else unsafeJso {}
        )
        CssBaseline {}
        +props.children
    }
}


package ru.raysmith.tgbot.webappapp.pages.theme

import js.objects.Object
import mui.material.*
import mui.system.responsive
import react.*
import ru.raysmith.tgbot.hooks.useThemeParams
import ru.raysmith.tgbot.webappapp.camelCaseToSnakeCase
import ru.raysmith.tgbot.webappapp.capitalize
import ru.raysmith.tgbot.webappapp.components.ColorBlock
import ru.raysmith.tgbot.webappapp.components.ControlsPaperStack
import ru.raysmith.tgbot.webappapp.components.DefaultValuesHintAlert
import ru.raysmith.tgbot.webappapp.components.datadisplay.DataDisplayTableRow
import ru.raysmith.tgbot.webappapp.decapitalize
import ru.raysmith.tgbot.webappapp.pages.BaseSubPageLayout
import ru.raysmith.tgbot.webappapp.provider.useSettingsContext
import web.cssom.BackgroundColor
import web.cssom.Color
import web.cssom.NamedColor

val ThemePage = FC<Props> {
    val tp = useThemeParams()
    val settings = useSettingsContext()

    val tpKeys = Object.keys(tp).filter { it != "colorScheme" }.filter { jsTypeOf(tp.asDynamic()[it]) == "string" }

    BaseSubPageLayout {
        title = "Theme"

        FormControlLabel {
            label = ReactNode("Use telegram theme")
            control = Switch.create {
                checked = settings.useTgTheme
                onChange = { _, checked ->
                    println(checked)
                    settings.setToggleUseTgTheme(checked)
                }
            }
        }

        Table {
            TableBody {
                DataDisplayTableRow {
                    title = "colorScheme"
                    value = Typography.create { +tp.colorScheme.toString() }
                }

                tpKeys.forEach {
                    DataDisplayTableRow {
                        title = it
                        value = ColorBlock.create {
                            backgroundColor =
                                tp.asDynamic()[it.split("_").joinToString("") { w -> w.capitalize() }.decapitalize()]
                                    ?.unsafeCast<BackgroundColor>()
                                    ?: NamedColor.transparent
                        }
                    }
                }
            }
        }

        ControlsPaperStack {
            direction = responsive(StackDirection.column)
            spacing = responsive(2)

            ColorSelect {
                label = "Header color via ThemeParams"
                transformer = { tp.asDynamic()[it] }
                isKeyEqualColor = { tp.asDynamic()[it] == tp.headerColor }
                onChange = { tp.setHeaderColor(it) }

                MenuItem {
                    value = tp.bgColor
                    +"bgColor"
                }

                MenuItem {
                    value = tp.secondaryBgColor
                    +"secondaryBgColor"
                }
            }

            ColorSelect {
                label = "Header color via keywords"
                transformer = { it.camelCaseToSnakeCase() }
                isKeyEqualColor = { tp.asDynamic()[it] == tp.headerColor }
                onChange = { tp.setHeaderColor(it) }

                MenuItem {
                    value = "bg_color"
                    +"bg_color"
                }

                MenuItem {
                    value = "secondary_bg_color"
                    +"secondary_bg_color"
                }
            }

            Button {
                +"Set white header color via the #RRGGBB format"
                onClick = {
                    tp.setHeaderColor(NamedColor.white)
                }
            }
        }

        ControlsPaperStack {
            direction = responsive(StackDirection.column)
            spacing = responsive(2)

            ColorSelect {
                key = "3"
                label = "Background color"
                transformer = { it.camelCaseToSnakeCase() }
                isKeyEqualColor = { tp.asDynamic()[it] == tp.backgroundColor }
                onChange = {
                    tp.setBackgroundColor(it)
                    settings.setBackgroundColor(it)
                }

                MenuItem {
                    value = "bg_color"
                    +"bg_color"
                }

                MenuItem {
                    value = "secondary_bg_color"
                    +"secondary_bg_color"
                }
            }

            ColorSelect {
                label = "Bottom bar color"
                transformer = { it.camelCaseToSnakeCase() }
                isKeyEqualColor = { tp.asDynamic()[it] == tp.bottomBarColor }
                onChange = { tp.setBottomBarColor(it) }

                MenuItem {
                    value = "bg_color"
                    +"bg_color"
                }

                MenuItem {
                    value = "secondary_bg_color"
                    +"secondary_bg_color"
                }

                MenuItem {
                    value = "bottom_bar_bg_color"
                    +"bottom_bar_bg_color"
                }
            }


        }

        DefaultValuesHintAlert()
    }
}

private external interface ColorSelectProps : PropsWithChildren {
    var label: String
    var onChange: (color: Color) -> Unit
    var transformer: (key: String) -> String
    var isKeyEqualColor: (key: String) -> Boolean
}

private val ColorSelect = FC<ColorSelectProps> { props ->
    FormControl {
        fullWidth = true
        InputLabel {
            +props.label
        }

        Select {
            value = listOf("bgColor", "secondaryBgColor")
                .find { props.isKeyEqualColor(it) }
                ?.let { props.transformer(it) }
                ?: ""
            label = ReactNode(props.label)
            onChange = { event, _ ->
                val value = event.target.asDynamic().value.unsafeCast<Color>()
                props.onChange(value)
            }

            +props.children
        }
    }
}
package ru.raysmith.tgbot.webappapp.pages.theme

import js.objects.Object
import mui.material.FormControlLabel
import mui.material.Switch
import mui.material.Table
import mui.material.TableBody
import react.FC
import react.Props
import react.ReactNode
import react.create
import ru.raysmith.tgbot.hooks.useThemeParams
import ru.raysmith.tgbot.webappapp.components.datadisplay.DataDisplayTableRow
import ru.raysmith.tgbot.webappapp.pages.BaseSubPageLayout
import web.cssom.BackgroundColor
import web.cssom.NamedColor
import ru.raysmith.tgbot.webappapp.capitalize
import ru.raysmith.tgbot.webappapp.decapitalize
import ru.raysmith.tgbot.webappapp.provider.useSettingsContext

val ThemePage = FC<Props> {
    val tp = useThemeParams()
    val settings = useSettingsContext()

    BaseSubPageLayout {

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
                    value = ReactNode(tp.colorScheme.toString())
                }

                Object.keys(tp).filter { it != "colorScheme" }.forEach {
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
    }
}
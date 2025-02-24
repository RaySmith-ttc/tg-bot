package ru.raysmith.tgbot.webappapp.pages.state

import mui.material.Table
import mui.material.TableBody
import react.FC
import react.create
import ru.raysmith.tgbot.hooks.useWebAppState
import ru.raysmith.tgbot.webappapp.components.ToggleButtonsGroupControl
import ru.raysmith.tgbot.webappapp.components.datadisplay.DataDisplayCheckbox
import ru.raysmith.tgbot.webappapp.components.datadisplay.DataDisplayTableRow
import ru.raysmith.tgbot.webappapp.pages.BaseSubPageLayout

val StatePage = FC {
    val state = useWebAppState()

    BaseSubPageLayout {
        title = "App state"
        Table {
            TableBody {
                DataDisplayTableRow {
                    title = "isActive"
                    value = DataDisplayCheckbox.create { checked = state.isActive }
                }
                DataDisplayTableRow {
                    title = "isClosingConfirmationEnabled"
                    value = DataDisplayCheckbox.create { checked = state.isClosingConfirmationEnabled }

                    ToggleButtonsGroupControl {
                        value = state.isClosingConfirmationEnabled
                        items = mapOf(
                            ("Enable" to true) to { state.enableClosingConfirmation() },
                            ("Disable" to false) to { state.disableClosingConfirmation() }
                        )
                    }
                }
            }
        }
    }
}
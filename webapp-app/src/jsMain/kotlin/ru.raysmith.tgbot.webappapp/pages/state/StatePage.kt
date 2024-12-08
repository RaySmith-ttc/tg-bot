package ru.raysmith.tgbot.webappapp.pages.state

import mui.material.*
import mui.system.Breakpoint
import mui.system.responsive
import react.FC
import react.create
import ru.raysmith.tgbot.hooks.useWebAppState
import ru.raysmith.tgbot.webappapp.components.datadisplay.DataDisplayCheckbox
import ru.raysmith.tgbot.webappapp.components.datadisplay.DataDisplayTableRow
import ru.raysmith.tgbot.webappapp.pages.BaseSubPageLayout

val StatePage = FC {
    val state = useWebAppState()

    BaseSubPageLayout {
        Table {
            TableBody {
                DataDisplayTableRow {
                    title = "isActive"
                    value = DataDisplayCheckbox.create { checked = state.isActive }
                }
                DataDisplayTableRow {
                    title = "isClosingConfirmationEnabled"
                    value = DataDisplayCheckbox.create { checked = state.isClosingConfirmationEnabled }

                    Stack {
                        spacing = responsive(1)
                        direction = responsive(
                            Breakpoint.xs to StackDirection.column,
                            Breakpoint.sm to StackDirection.row,
                        )

                        Button {
                            +"Enable"
                            disabled = state.isClosingConfirmationEnabled
                            onClick = {
                                state.enableClosingConfirmation()
                            }
                        }

                        Button {
                            +"Disable"
                            disabled = !state.isClosingConfirmationEnabled
                            onClick = {
                                state.disableClosingConfirmation()
                            }
                        }
                    }
                }
            }
        }
    }
}
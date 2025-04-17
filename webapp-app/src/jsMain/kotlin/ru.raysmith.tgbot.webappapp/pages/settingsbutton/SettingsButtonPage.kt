package ru.raysmith.tgbot.webappapp.pages.settingsbutton

import mui.material.Button
import mui.material.Stack
import mui.material.StackDirection
import mui.system.responsive
import react.FC
import react.create
import ru.raysmith.tgbot.hooks.useSettingsButton
import ru.raysmith.tgbot.webappapp.components.ControlsPaperStack
import ru.raysmith.tgbot.webappapp.components.ToggleButtonsGroupControl
import ru.raysmith.tgbot.webappapp.components.applyControlButtonStyle
import ru.raysmith.tgbot.webappapp.components.datadisplay.DataDisplayCheckbox
import ru.raysmith.tgbot.webappapp.components.datadisplay.DataDisplayTable
import ru.raysmith.tgbot.webappapp.components.datadisplay.DataDisplayTableRow
import ru.raysmith.tgbot.webappapp.pages.BaseSubPageLayout
import web.prompts.alert

val SettingsButtonPage = FC {
    val settingsButton = useSettingsButton()

    val onClick = {
        alert("Settings button pressed")
    }

    BaseSubPageLayout {
        title = "Settings button"
        Stack {
            direction = responsive(StackDirection.column)
            spacing = responsive(2)

            DataDisplayTable {
                DataDisplayTableRow {
                    title = "isVisible"
                    value = DataDisplayCheckbox.create { checked = settingsButton.isVisible }

                    ToggleButtonsGroupControl {
                        value = settingsButton.isVisible
                        items = mapOf(
                            ("Show" to true) to { settingsButton.show() },
                            ("Hide" to false) to { settingsButton.hide() }
                        )
                    }
                }
            }

            ControlsPaperStack {
                title = "Custom onClick"

                Button {
                    +"onClick"
                    applyControlButtonStyle()
                    this.onClick = {
                        settingsButton.onClick(onClick)
                    }
                }

                Button {
                    +"offClick"
                    applyControlButtonStyle()
                    this.onClick = {
                        settingsButton.offClick(onClick)
                    }
                }
            }
        }
    }
}
package ru.raysmith.tgbot.webappapp.pages.backbutton

import mui.material.Button
import mui.material.Stack
import mui.material.StackDirection
import mui.system.responsive
import react.FC
import react.create
import react.useEffectOnce
import ru.raysmith.tgbot.hooks.useBackButton
import ru.raysmith.tgbot.webappapp.components.ControlsPaperStack
import ru.raysmith.tgbot.webappapp.components.ToggleButtonsGroupControl
import ru.raysmith.tgbot.webappapp.components.applyControlButtonStyle
import ru.raysmith.tgbot.webappapp.components.datadisplay.DataDisplayCheckbox
import ru.raysmith.tgbot.webappapp.components.datadisplay.DataDisplayTable
import ru.raysmith.tgbot.webappapp.components.datadisplay.DataDisplayTableRow
import ru.raysmith.tgbot.webappapp.pages.BaseSubPageLayout
import web.prompts.alert

val BackButtonPage = FC<BBProps> { props ->
    val backButton = useBackButton()

    val onClick = {
        alert("Back button pressed. Press offClick to prevent this alert.")
    }

    useEffectOnce {
        backButton.show()
    }

    BaseSubPageLayout {
        title = "Back button"
        Stack {
            direction = responsive(StackDirection.column)
            spacing = responsive(2)

            DataDisplayTable {
                DataDisplayTableRow {
                    title = "isVisible"
                    value = DataDisplayCheckbox.create { checked = backButton.isVisible }

                    ToggleButtonsGroupControl {
                        value = backButton.isVisible
                        items = mapOf(
                            ("Show" to true) to { backButton.show() },
                            ("Hide" to false) to { backButton.hide() }
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
                        backButton.onClick(onClick)
                        props.setIsBackButtonDefaultOnClickEnabled(false)
                    }
                }

                Button {
                    +"offClick"
                    applyControlButtonStyle()
                    this.onClick = {
                        backButton.offClick(onClick)
                        props.setIsBackButtonDefaultOnClickEnabled(true)
                    }
                }
            }
        }
    }
}
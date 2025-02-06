package ru.raysmith.tgbot.webappapp.pages.settingsbutton

import mui.material.*
import mui.system.responsive
import react.FC
import react.create
import ru.raysmith.tgbot.hooks.useSettingsButton
import ru.raysmith.tgbot.webappapp.components.datadisplay.DataDisplayCheckbox
import ru.raysmith.tgbot.webappapp.components.datadisplay.DataDisplayTableRow
import ru.raysmith.tgbot.webappapp.pages.BaseSubPageLayout
import web.prompts.alert

val SettingsButtonPage = FC {
    val settingsButton = useSettingsButton()

    val onClick = {
        alert("Settings button pressed")
    }

    BaseSubPageLayout {
        title = "Back button"
        Stack {
            direction = responsive(StackDirection.column)
            spacing = responsive(2)

            Table {
                TableBody {
                    DataDisplayTableRow {
                        title = "isVisible"
                        value = DataDisplayCheckbox.create { checked = settingsButton.isVisible }

                        Stack {
                            direction = responsive(StackDirection.row)
                            spacing = responsive(2)

                            Button {
                                disabled = settingsButton.isVisible
                                this.onClick = {
                                    settingsButton.show()
                                }
                                +"Show"
                            }

                            Button {
                                disabled = !settingsButton.isVisible
                                this.onClick = {
                                    settingsButton.hide()
                                }
                                +"Hide"
                            }
                        }
                    }
                }
            }

            Stack {
                direction = responsive(StackDirection.row)
                spacing = responsive(2)

                Button {
                    this.onClick = {
                        settingsButton.onClick(onClick)
                    }
                    +"onClick"
                }

                Button {
                    this.onClick = {
                        settingsButton.offClick(onClick)
                    }
                    +"offClick"
                }
            }
        }
    }
}
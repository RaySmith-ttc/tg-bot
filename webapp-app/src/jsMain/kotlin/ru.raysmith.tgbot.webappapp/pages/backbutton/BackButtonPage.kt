package ru.raysmith.tgbot.webappapp.pages.backbutton

import mui.material.*
import mui.system.responsive
import react.FC
import react.create
import react.useEffectOnce
import ru.raysmith.tgbot.hooks.useBackButton
import ru.raysmith.tgbot.webappapp.components.datadisplay.DataDisplayCheckbox
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

            Table {
                TableBody {
                    DataDisplayTableRow {
                        title = "isVisible"
                        value = DataDisplayCheckbox.create { checked = backButton.isVisible }

                        Stack {
                            direction = responsive(StackDirection.row)
                            spacing = responsive(2)

                            Button {
                                disabled = backButton.isVisible
                                this.onClick = {
                                    backButton.show()
                                }
                                +"Show"
                            }

                            Button {
                                disabled = !backButton.isVisible
                                this.onClick = {
                                    backButton.hide()
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
                        backButton.onClick(onClick)
                        props.setIsBackButtonDefaultOnClickEnabled(false)
                    }
                    +"onClick"
                }

                Button {
                    this.onClick = {
                        backButton.offClick(onClick)
                        props.setIsBackButtonDefaultOnClickEnabled(true)
                    }
                    +"offClick"
                }
            }
        }
    }
}
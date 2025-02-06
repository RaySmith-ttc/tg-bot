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
    val bb = useBackButton()

    val onClick = {
        alert("Back button pressed. Press offClick to prevent this alert.")

    }

    useEffectOnce {
        bb.show()
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
                        value = DataDisplayCheckbox.create { checked = bb.isVisible }

                        Stack {
                            direction = responsive(StackDirection.row)
                            spacing = responsive(2)

                            Button {
                                disabled = bb.isVisible
                                this.onClick = {
                                    bb.show()
                                }
                                +"Show"
                            }

                            Button {
                                disabled = !bb.isVisible
                                this.onClick = {
                                    bb.hide()
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
                        bb.onClick(onClick)
                        props.setIsBackButtonDefaultOnClickEnabled(false)
                    }
                    +"onClick"
                }

                Button {
                    this.onClick = {
                        bb.offClick(onClick)
                        props.setIsBackButtonDefaultOnClickEnabled(true)
                    }
                    +"offClick"
                }
            }
        }
    }
}
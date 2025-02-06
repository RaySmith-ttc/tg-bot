package ru.raysmith.tgbot.webappapp.pages.bottombutton

import js.objects.jso
import mui.material.*
import mui.system.Stack
import mui.system.StackDirection
import mui.system.responsive
import mui.system.sx
import react.FC
import react.Props
import react.create
import ru.raysmith.tgbot.BottomButtonPosition
import ru.raysmith.tgbot.BottomButtonType
import ru.raysmith.tgbot.hooks.BottomButtonHookType
import ru.raysmith.tgbot.webappapp.components.ColorBlock
import ru.raysmith.tgbot.webappapp.components.datadisplay.DataDisplayCheckbox
import ru.raysmith.tgbot.webappapp.components.datadisplay.DataDisplayTableRow
import ru.raysmith.utils.generateRandomString
import web.cssom.*

external interface BottomButtonViewProps : Props {
    var button: BottomButtonHookType
}

val BottomButtonView = FC<BottomButtonViewProps> { props ->
    Table {
        TableBody {
            DataDisplayTableRow {
                title = "type"
                value = Typography.create { +props.button.type.toString() }
            }
            DataDisplayTableRow {
                title = "text"
                value = Typography.create { +props.button.text.toString() }
            }
            DataDisplayTableRow {
                title = "color"
                value = ColorBlock.create { backgroundColor = props.button.color.unsafeCast<BackgroundColor>() }
            }
            DataDisplayTableRow {
                title = "textColor"
                value = ColorBlock.create { backgroundColor = props.button.textColor.unsafeCast<BackgroundColor>() }
            }
            DataDisplayTableRow {
                title = "isVisible"
                value = DataDisplayCheckbox.create { checked = props.button.isVisible }
                Switch {
                    checked = props.button.isVisible
                    onClick = {
                        if (props.button.isVisible) {
                            props.button.hide()
                        } else {
                            props.button.show()
                        }
                    }
                }
            }
            DataDisplayTableRow {
                title = "isActive"
                value = DataDisplayCheckbox.create { checked = props.button.isActive }
                Switch {
                    checked = props.button.isActive
                    onClick = {
                        if (props.button.isActive) {
                            props.button.disable()
                        } else {
                            props.button.enable()
                        }
                    }
                }
            }
            DataDisplayTableRow {
                title = "hasShineEffect"
                value = DataDisplayCheckbox.create { checked = props.button.hasShineEffect }
            }
            DataDisplayTableRow {
                title = "position"
                value = Typography.create { +props.button.position.toString() }

                if (props.button.type == BottomButtonType.secondary) {
                    Stack {
                        direction = responsive(StackDirection.row)
                        spacing = responsive(1)

                        Button {
                            +"Top"
                            onClick = {
                                props.button.setParams(jso {
                                    position = BottomButtonPosition.top
                                })
                            }
                        }

                        Button {
                            +"Left"
                            onClick = {
                                props.button.setParams(jso {
                                    position = BottomButtonPosition.left
                                })
                            }
                        }

                        Button {
                            +"Right"
                            onClick = {
                                props.button.setParams(jso {
                                    position = BottomButtonPosition.right
                                })
                            }
                        }

                        Button {
                            +"Bottom"
                            onClick = {
                                props.button.setParams(jso {
                                    position = BottomButtonPosition.bottom
                                })
                            }
                        }
                    }
                }
            }
            DataDisplayTableRow {
                title = "isProgressVisible"
                value = DataDisplayCheckbox.create { checked = props.button.isProgressVisible }

                Stack {
                    direction = responsive(StackDirection.row)
                    spacing = responsive(1)

                    Button {
                        +"Show"
                        onClick = {
                            props.button.showProgress(false)
                        }
                    }

                    Button {
                        +"Show and leave active"
                        onClick = {
                            props.button.showProgress(true)
                        }
                    }

                    Button {
                        +"Hide"
                        onClick = {
                            props.button.hideProgress()
                        }
                    }
                }
            }
        }
    }

    Button {
        +"Set random text"
        onClick = {
            props.button.setText(generateRandomString(8, ('a'..'z').toList()))
        }
    }

    Button {
        +"Set random color"
        onClick = {
            props.button.setParams(jso {
                color = getRandomColor()
            })
        }
    }

    Button {
        +"Set random text color"
        onClick = {
            props.button.setParams(jso {
                textColor = getRandomColor()
            })
        }
    }
}
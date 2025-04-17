package ru.raysmith.tgbot.webappapp.pages.bottombutton

import js.objects.jso
import mui.icons.material.FormatColorFill
import mui.icons.material.FormatColorText
import mui.icons.material.TextFields
import mui.material.Button
import mui.material.StackDirection
import mui.material.Typography
import mui.system.responsive
import mui.system.sx
import react.FC
import react.Props
import react.create
import ru.raysmith.tgbot.BottomButtonPosition
import ru.raysmith.tgbot.BottomButtonType
import ru.raysmith.tgbot.hooks.BottomButtonHookType
import ru.raysmith.tgbot.webappapp.components.ColorBlock
import ru.raysmith.tgbot.webappapp.components.ControlsPaperStack
import ru.raysmith.tgbot.webappapp.components.ToggleButtonsGroupControl
import ru.raysmith.tgbot.webappapp.components.applyControlButtonStyle
import ru.raysmith.tgbot.webappapp.components.datadisplay.DataDisplayCheckbox
import ru.raysmith.tgbot.webappapp.components.datadisplay.DataDisplayTable
import ru.raysmith.tgbot.webappapp.components.datadisplay.DataDisplayTableRow
import ru.raysmith.utils.generateRandomString
import web.cssom.AlignItems
import web.cssom.BackgroundColor

external interface BottomButtonViewProps : Props {
    var button: BottomButtonHookType
}

val BottomButtonView = FC<BottomButtonViewProps> { props ->
    DataDisplayTable {
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

            ToggleButtonsGroupControl {
                value = props.button.isVisible
                items = mapOf(
                    ("Show" to true) to { props.button.show() },
                    ("Hide" to false) to { props.button.hide() }
                )
            }
        }
        DataDisplayTableRow {
            title = "isActive"
            value = DataDisplayCheckbox.create { checked = props.button.isActive }

            ToggleButtonsGroupControl {
                value = props.button.isActive
                items = mapOf(
                    ("Enable" to true) to { props.button.enable() },
                    ("Disable" to false) to { props.button.disable() }
                )
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
                ToggleButtonsGroupControl {
                    value = props.button.position
                    items = mapOf(
                        ("Top" to BottomButtonPosition.top) to { props.button.setParams(jso { position = BottomButtonPosition.top }) },
                        ("Left" to BottomButtonPosition.left) to { props.button.setParams(jso { position = BottomButtonPosition.left }) },
                        ("Right" to BottomButtonPosition.right) to { props.button.setParams(jso { position = BottomButtonPosition.right }) },
                        ("Bottom" to BottomButtonPosition.bottom) to { props.button.setParams(jso { position = BottomButtonPosition.bottom }) }
                    )
                }
            }
        }
        DataDisplayTableRow {
            title = "isProgressVisible"
            value = DataDisplayCheckbox.create { checked = props.button.isProgressVisible }

            ToggleButtonsGroupControl {
                value = when {
                    props.button.isProgressVisible && props.button.isActive -> 1
                    props.button.isProgressVisible -> 2
                    else -> 3
                }
                items = mapOf(
                    ("Show and leave active" to 1) to { props.button.showProgress(true) },
                    ("Show" to 2) to { props.button.showProgress(false) },
                    ("Hide" to 3) to { props.button.hideProgress() }
                )
            }
        }
    }

    ControlsPaperStack {
        title = "Custom colors"
        direction = responsive(StackDirection.column)
        sx { alignItems = AlignItems.flexStart }

        Button {
            +"Set random text"
            applyControlButtonStyle()
            startIcon = TextFields.create()
            onClick = {
                props.button.setText(generateRandomString(8, ('a'..'z').toList()))
            }
        }

        Button {
            +"Set random color"
            applyControlButtonStyle()
            startIcon = FormatColorFill.create()
            onClick = {
                props.button.setParams(jso {
                    color = getRandomColor()
                })
            }
        }

        Button {
            +"Set random text color"
            applyControlButtonStyle()
            startIcon = FormatColorText.create()
            onClick = {
                props.button.setParams(jso {
                    textColor = getRandomColor()
                })
            }
        }
    }
}
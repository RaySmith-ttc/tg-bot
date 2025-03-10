package ru.raysmith.tgbot.webappapp.pages.viewport

import mui.material.Button
import mui.material.Stack
import mui.material.StackDirection
import mui.material.Typography
import mui.material.styles.TypographyVariant
import mui.system.responsive
import mui.system.sx
import react.FC
import react.Props
import react.create
import ru.raysmith.tgbot.hooks.useInsets
import ru.raysmith.tgbot.hooks.useViewport
import ru.raysmith.tgbot.webappapp.components.ButtonsGroupControl
import ru.raysmith.tgbot.webappapp.components.ToggleButtonsGroupControl
import ru.raysmith.tgbot.webappapp.components.datadisplay.DataDisplayCheckbox
import ru.raysmith.tgbot.webappapp.components.datadisplay.DataDisplayTable
import ru.raysmith.tgbot.webappapp.components.datadisplay.DataDisplayTableRow
import ru.raysmith.tgbot.webappapp.pages.BaseSubPageLayout
import ru.raysmith.tgbot.webappapp.wrappers.pt

val ViewportPage = FC<Props> {
    val viewport = useViewport()
    val insets = useInsets()

    BaseSubPageLayout {
        title = "Viewport"
        spacing = responsive(1)

        Typography {
            +"Viewport"
            variant = TypographyVariant.h6
            sx { pt = 2 }
        }

        DataDisplayTable {
            DataDisplayTableRow {
                title = "isExpanded"
                value = DataDisplayCheckbox.create { checked = viewport.isExpanded }

                Button {
                    +"Expand"
                    disabled = viewport.isExpanded
                    onClick = {
                        viewport.expand()
                    }
                }
            }
            DataDisplayTableRow {
                title = "isVerticalSwipesEnabled"
                value = DataDisplayCheckbox.create { checked = viewport.isVerticalSwipesEnabled }

                ToggleButtonsGroupControl {
                    value = viewport.isVerticalSwipesEnabled
                    items = mapOf(
                        ("Enable" to true) to { viewport.enableVerticalSwipes() },
                        ("Disable" to false) to { viewport.disableVerticalSwipes() }
                    )
                }
            }
            DataDisplayTableRow {
                title = "isStateStable"
                value = DataDisplayCheckbox.create { checked = viewport.isStateStable }
            }
            DataDisplayTableRow {
                title = "viewportHeight"
                value = Typography.create { +viewport.viewportHeight.toString() }
            }
            DataDisplayTableRow {
                title = "viewportStableHeight"
                value = Typography.create { +viewport.viewportStableHeight.toString() }
            }
            DataDisplayTableRow {
                title = "isFullscreen"
                value = DataDisplayCheckbox.create { checked = viewport.isFullscreen }

                ButtonsGroupControl {
                    items = mapOf(
                        "Request" to { viewport.requestFullscreen() },
                        "Exit" to { viewport.exitFullscreen() }
                    )
                }
            }
            DataDisplayTableRow {
                title = "fullscreenFailed"
                value = Typography.create { +viewport.fullscreenFailed.toString() }

                Stack {
                    direction = responsive(StackDirection.row)
                    spacing = responsive(1)
                }
            }
        }

        Typography {
            +"Insets"
            variant = TypographyVariant.h6
            sx { pt = 2 }
        }

        DataDisplayTable {
                DataDisplayTableRow {
                    title = "Safe area top"
                    value = Typography.create { +insets.safeArea.top.toString() }
                }
                DataDisplayTableRow {
                    title = "Safe area bottom"
                    value = Typography.create { +insets.safeArea.bottom.toString() }
                }
                DataDisplayTableRow {
                    title = "Safe area left"
                    value = Typography.create { +insets.safeArea.left.toString() }
                }
                DataDisplayTableRow {
                    title = "Safe area right"
                    value = Typography.create { +insets.safeArea.right.toString() }
                }

                DataDisplayTableRow {
                    title = "Content safe area top"
                    value = Typography.create { +insets.contentSafeArea.top.toString() }
                }
                DataDisplayTableRow {
                    title = "Content safe area bottom"
                    value = Typography.create { +insets.contentSafeArea.bottom.toString() }
                }
                DataDisplayTableRow {
                    title = "Content safe area left"
                    value = Typography.create { +insets.contentSafeArea.left.toString() }
                }
                DataDisplayTableRow {
                    title = "Content safe area right"
                    value = Typography.create { +insets.contentSafeArea.right.toString() }
                }
            }
    }
}
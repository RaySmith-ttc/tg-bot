package ru.raysmith.tgbot.webappapp.pages.viewport

import mui.material.*
import mui.material.styles.TypographyVariant
import mui.system.Breakpoint
import mui.system.responsive
import mui.system.sx
import react.FC
import react.Props
import react.ReactNode
import react.create
import ru.raysmith.tgbot.hooks.useInsets
import ru.raysmith.tgbot.hooks.useViewport
import ru.raysmith.tgbot.webappapp.components.datadisplay.DataDisplayCheckbox
import ru.raysmith.tgbot.webappapp.components.datadisplay.DataDisplayTableRow
import ru.raysmith.tgbot.webappapp.pages.BaseSubPageLayout
import web.cssom.AlignItems
import web.cssom.atrule.orientation

val ViewportPage = FC<Props> {
    val viewport = useViewport()
    val insets = useInsets()

    BaseSubPageLayout {
        title = "Viewport"
        Stack {
            spacing = responsive(1)
            sx { alignItems = AlignItems.center }

            Typography {
                +"Viewport"
                variant = TypographyVariant.h6
            }

            Table {
                TableBody {
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
                        value = Switch.create {
                            checked = viewport.isVerticalSwipesEnabled
                            onClick = {
                                if (viewport.isVerticalSwipesEnabled) {
                                    viewport.disableVerticalSwipes()
                                } else {
                                    viewport.enableVerticalSwipes()
                                }
                            }
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

                        Stack {
                            spacing = responsive(1)
                            direction = responsive(
                                Breakpoint.xs to StackDirection.column,
                                Breakpoint.sm to StackDirection.row,
                            )

                            Button {
                                +"Request"
                                onClick = {
                                    viewport.fullscreenFailedClear()
                                    viewport.requestFullscreen()
                                }
                            }

                            Button {
                                +"Exit"
                                onClick = {
                                    viewport.fullscreenFailedClear()
                                    viewport.exitFullscreen()
                                }
                            }
                        }
                    }
                    DataDisplayTableRow {
                        title = "fullscreenFailed"
                        value = Typography.create { +viewport.fullscreenFailed?.toString() }

                        Stack {
                            direction = responsive(StackDirection.row)
                            spacing = responsive(1)
                        }
                    }
                }
            }

            Typography {
                +"Insets"
                variant = TypographyVariant.h6
            }

            Table {
                TableBody {
                    DataDisplayTableRow {
                        title = "Safe area top"
                        value = Typography.create { +insets.safeArea.top.toString() }
                        bigTitleWidth = true
                    }
                    DataDisplayTableRow {
                        title = "Safe area bottom"
                        value = Typography.create { +insets.safeArea.bottom.toString() }
                        bigTitleWidth = true
                    }
                    DataDisplayTableRow {
                        title = "Safe area left"
                        value = Typography.create { +insets.safeArea.left.toString() }
                        bigTitleWidth = true
                    }
                    DataDisplayTableRow {
                        title = "Safe area right"
                        value = Typography.create { +insets.safeArea.right.toString() }
                        bigTitleWidth = true
                    }

                    DataDisplayTableRow {
                        title = "Content safe area top"
                        value = Typography.create { +insets.contentSafeArea.top.toString() }
                        bigTitleWidth = true
                    }
                    DataDisplayTableRow {
                        title = "Content safe area bottom"
                        value = Typography.create { +insets.contentSafeArea.bottom.toString() }
                        bigTitleWidth = true
                    }
                    DataDisplayTableRow {
                        title = "Content safe area left"
                        value = Typography.create { +insets.contentSafeArea.left.toString() }
                        bigTitleWidth = true
                    }
                    DataDisplayTableRow {
                        title = "Content safe area right"
                        value = Typography.create { +insets.contentSafeArea.right.toString() }
                        bigTitleWidth = true
                    }
                }
            }
        }
    }
}
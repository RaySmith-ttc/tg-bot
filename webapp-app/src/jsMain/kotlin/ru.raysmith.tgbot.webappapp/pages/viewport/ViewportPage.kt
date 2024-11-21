package ru.raysmith.tgbot.webappapp.pages.viewport

import mui.material.*
import mui.system.responsive
import mui.system.sx
import react.FC
import react.Props
import react.ReactNode
import react.create
import ru.raysmith.tgbot.hooks.useViewport
import ru.raysmith.tgbot.webappapp.components.datadisplay.DataDisplayCheckbox
import ru.raysmith.tgbot.webappapp.components.datadisplay.DataDisplayTableRow
import ru.raysmith.tgbot.webappapp.pages.BaseSubPageLayout
import web.cssom.AlignItems

val ViewportPage = FC<Props> {
    val viewport = useViewport()

    BaseSubPageLayout {
        Stack {
            spacing = responsive(1)

            sx {
                alignItems = AlignItems.center
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
                        value = ReactNode(viewport.viewportHeight.toString())
                    }
                    DataDisplayTableRow {
                        title = "viewportStableHeight"
                        value = ReactNode(viewport.viewportStableHeight.toString())
                    }
                }
            }
        }
    }
}
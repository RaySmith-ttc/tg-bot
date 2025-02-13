package ru.raysmith.tgbot.webappapp.pages.cloud

import mui.material.*
import mui.system.sx
import react.FC
import react.useReducer
import ru.raysmith.tgbot.BuiltinImplementation
import ru.raysmith.tgbot.hooks.CloudStorageHookState
import ru.raysmith.tgbot.hooks.useCloudStorage
import ru.raysmith.tgbot.webappapp.pages.BaseSubPageLayout
import web.cssom.OverflowWrap
import web.cssom.WhiteSpace
import web.cssom.WordWrap

private val ADDING_BATCH = "Adding 10 items".unsafeCast<CloudStorageHookState>()

@OptIn(BuiltinImplementation::class)
val CloudStoragePage = FC {
    val cloudStorage = useCloudStorage()
    val (waitAddingBatch, dispatch) = useReducer({ state: Int, action: Int -> state + action }, 0)

    BaseSubPageLayout {

        Typography {
            +"State: ${if (waitAddingBatch > 0) ADDING_BATCH else cloudStorage.state}"
        }

        Table {
            TableBody {
                cloudStorage.items.forEach { (key, value) ->
                    TableRow {
                        this.key = key
                        TableCell {
                            sx {
                                wordWrap = WordWrap.breakWord
                                whiteSpace = WhiteSpace.normal
                                overflowWrap = OverflowWrap.anywhere
                            }
                            Typography {
                                +key
                            }
                        }
                        TableCell {
                            sx {
                                wordWrap = WordWrap.breakWord
                                whiteSpace = WhiteSpace.normal
                                overflowWrap = OverflowWrap.anywhere
                            }
                            Typography {
                                +value
                            }
                        }
                    }
                }
            }
        }

        Button {
            +"Add 10 items"
            onClick = {
                val lastId = cloudStorage.items.keys.mapNotNull { it.toIntOrNull() }.maxOrNull() ?: 0

                dispatch(10)
                repeat(10) {
                    cloudStorage.setItem((it + lastId + 1).toString(), "value${it + lastId}") { error, isStored ->
                        println("CloudStoragePage.setItem: error = [${error}], isStored = [${isStored}]")
                        dispatch(-1)
                    }
                }
            }
        }

        Button {
            +"Delete all items"
            onClick = {
                cloudStorage.removeItems(cloudStorage.items.keys) { error, isRemoved ->
                    println("CloudStoragePage.removeItems: error = [${error}], isRemoved = [${isRemoved}]")
                }
            }
        }
    }
}
package ru.raysmith.tgbot.webappapp.pages.base

import mui.material.*
import mui.material.styles.TypographyVariant
import mui.system.sx
import react.*
import ru.raysmith.tgbot.WebAppUser
import ru.raysmith.tgbot.webApp
import ru.raysmith.tgbot.webappapp.components.datadisplay.DataDisplayCheckbox
import ru.raysmith.tgbot.webappapp.components.datadisplay.DataDisplayTableRow
import ru.raysmith.tgbot.webappapp.pages.BaseSubPageLayout
import ru.raysmith.tgbot.webappapp.wrappers.pt

val BaseInfoPage = FC<Props> {
    BaseSubPageLayout {
        title = "Base info"
        Table {
            TableBody {
                DataDisplayTableRow {
                    title = "Bot API version"
                    value = Typography.create { +webApp.version }
                }
                DataDisplayTableRow {
                    title = "Platform"
                    value = Typography.create { +webApp.platform }
                }
            }
        }

        Typography {
            +"User data"
            variant = TypographyVariant.h6
            sx { pt = 2 }
        }

        WebAppUserData {
            user = webApp.initDataUnsafe.user
        }

        Typography {
            +"Receiver data"
            variant = TypographyVariant.h6
            sx { pt = 2 }
        }

        WebAppUserData {
            user = webApp.initDataUnsafe.receiver
        }

        Typography {
            +"Chat data"
            variant = TypographyVariant.h6
            sx { pt = 2 }
        }

        Table {
            TableBody {
                DataDisplayTableRow {
                    title = "ID"
                    value = Typography.create { +webApp.initDataUnsafe.chat?.id.toString() }
                }
                DataDisplayTableRow {
                    title = "Type"
                    value = Typography.create { +webApp.initDataUnsafe.chat?.type }
                }
                DataDisplayTableRow {
                    title = "Title"
                    value = Typography.create { +webApp.initDataUnsafe.chat?.title.toString() }
                }
                DataDisplayTableRow {
                    title = "Username"
                    value = Typography.create { +webApp.initDataUnsafe.chat?.username.toString() }
                }
                DataDisplayTableRow {
                    title = "Photo"
                    value = Avatar.create { src = webApp.initDataUnsafe.chat?.photoUrl }
                }
            }
        }
    }
}

external interface WebAppUserDataProps : Props {
    var user: WebAppUser?
}

val WebAppUserData = FC<WebAppUserDataProps> { props ->
    Table {
        TableBody {
            DataDisplayTableRow {
                title = "ID"
                value = Typography.create { +props.user?.id.toString() }
            }
            DataDisplayTableRow {
                title = "Is bot"
                value = DataDisplayCheckbox.create { checked = props.user?.isBot }
            }
            DataDisplayTableRow {
                title = "First name"
                value = Typography.create { +props.user?.firstName.toString() }
            }
            DataDisplayTableRow {
                title = "Last name"
                value = Typography.create { +props.user?.lastName.toString() }
            }
            DataDisplayTableRow {
                title = "Username"
                value = Typography.create { +props.user?.username.toString() }
            }
            DataDisplayTableRow {
                title = "Language code"
                value = Typography.create { +props.user?.languageCode.toString() }
            }
            DataDisplayTableRow {
                title = "Is premium"
                value = props.user?.isPremium?.let { isPremium ->
                    DataDisplayCheckbox.create { checked = isPremium }
                } ?: Typography.create { +"null" }
            }
            DataDisplayTableRow {
                title = "Added to attachment menu"
                value = props.user?.addedToAttachmentMenu?.let { addedToAttachmentMenu ->
                    DataDisplayCheckbox.create { checked = addedToAttachmentMenu }
                } ?: Typography.create { +"null" }
            }
            DataDisplayTableRow {
                title = "Allows write to pm"
                value = props.user?.allowsWriteToPm?.let { allowsWriteToPm ->
                    DataDisplayCheckbox.create { checked = allowsWriteToPm }
                } ?: Typography.create { +"null" }
            }
            DataDisplayTableRow {
                title = "Photo url"
                value = Avatar.create { src = props.user?.photoUrl }
            }
        }
    }
}
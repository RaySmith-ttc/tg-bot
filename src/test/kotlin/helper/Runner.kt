package helper

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import ru.raysmith.tgbot.core.*
import ru.raysmith.tgbot.core.handler.CallbackQueryHandler
import ru.raysmith.tgbot.model.Currency
import ru.raysmith.tgbot.model.bot.TextMessage
import ru.raysmith.tgbot.model.bot.inlineKeyboard
import ru.raysmith.tgbot.model.network.CallbackQuery
import ru.raysmith.tgbot.model.network.chat.ChatMember
import ru.raysmith.tgbot.model.network.message.MessageEntityType
import ru.raysmith.tgbot.model.network.payment.LabeledPrice
import ru.raysmith.tgbot.utils.AdditionalRowsPosition
import ru.raysmith.tgbot.utils.datepicker.DatePicker
import ru.raysmith.tgbot.utils.datepicker.DatePickerState
import ru.raysmith.tgbot.utils.Pagination
import java.io.File
import java.util.*

val datePicker = DatePicker("date_picker").apply {
    locale = Locale.forLanguageTag("us")
//    monthLimitBack = 5
//    monthLimitForward = 3
//    allowFutureDays = false
//    allowPastDays = false
    years = 2000..2022
    yearsColumns = 4
    yearsRows = 2

    additionalRowsPosition = AdditionalRowsPosition.TOP
    additionalRows = {
        row("some btn", CallbackQuery.EMPTY_CALLBACK_DATA)
    }
    additionalRowsVisibleOnStates = setOf(DatePickerState.DAY, DatePickerState.YEAR)
}

val pagesData = 1..120

class Runner {

    val CALLBACK_PREFIX = "cb_"

    fun ISender.sendMain() {
//        send {
//            text = "main"
//            replyKeyboard {
//                inputFieldPlaceholder = "test"
//                row { button("send image") }
//                row { button("send image2") }
//                row { button("send other id") }
//            }
//        }
        send {
            text = "main"
            inlineKeyboard {
                row {
                    button {
                        text = "some pay"
                        callbackData = "pay"
                        pay = true
                    }
                }
                row("dsvewdfa", "dsvewdfa")
            }
        }
    }

    fun CallbackQueryHandler.sendHelloMessage(messageText: String, isEdit: Boolean = false) {

        val keyboard = inlineKeyboard {
            row {
                button("hello", "hello")
                button("world", "${CALLBACK_PREFIX}world")
            }
            row {
                button {
                    text = "!"
                    callbackData = "asd"
                }
            }
        }

        if (isEdit) {
            edit {
                text = messageText
                keyboardMarkup = keyboard
            }
        } else {
            send {
                text = messageText
                keyboardMarkup = keyboard
            }
        }
    }

    @Test
    fun run() = runBlocking {

        val logger = LoggerFactory.getLogger("bot")

        Bot(token = System.getenv("tg.test"))
            .onError { e -> logger.error(e.message) }
            .onUpdate { updates -> updates.forEach { println(it.callbackQuery?.data) } }
            .registerDatePicker(datePicker)
//            .registerPagination(pagination)
            .shutdownCommand("/shutdown")
            .start {
                handleMyChatMember {
                    if (newChatMember is ChatMember.ChatMemberMember) {
                        send("Я родился")
                    }
                }

                handlePreCheckoutQuery {
                    answerPreCheckoutQuery(true)
                }

                handleMessage {

                    if (messageText == "/start") {
                        sendMain()
                        return@handleMessage
                    }

                    else if (messageText == "send image") {
                        sendPhoto {
                            setPhoto("AgACAgIAAxkDAAPTYLzxe542hHekjNmlcA3vMMw7XVgAAlqyMRvfveFJJlmwc6u88jc3Bo-hLgADAQADAgADdwADuUkDAAEfBA")
                            caption = "Test image"
                        }
                        return@handleMessage
                    }

                    else if (messageText == "send image2") {
                        sendPhoto {
                            setPhoto(File("C:\\Users\\Ray\\Desktop\\image1.png")/*.readBytes(), "test", "image/jpg"*/)
                            caption = "Test image2"
                        }
                        return@handleMessage
                    }

                    else if (messageText == "send other id") {
                        send("1") {
                            text = "this is not sended"
                        }
                    } else {
                        send {
//                        textWithEntities {
//                            text = "Hello\nPhone: +7 950 555-55-55\nLink: vk.com"
//                            disableWebPagePreview = true
//                            entity(MessageEntityType.PHONE_NUMBER) {
//                                offset = text!!.indexOf("Phone: ") + 7
//                                length = 16
//                            }
//                        }

                            disableWebPagePreview = true

                            textWithEntities {
                                underline("Hello").text("\n")
                                bold("Phone: ").phoneNumber("+7 950 555-55-55")
                                text("\n")
                                italic("Link: ").textLink("vk", "vk.com")
                                text("\n")
                                text("end")

                                entity(MessageEntityType.UNDERLINE) {
                                    offset = 20
                                    length = 3
                                }
                            }

                            inlineKeyboard {
                                row {
                                    button("hello", "hello")
                                    button("world", "${CALLBACK_PREFIX}world")
                                }
                                row {
                                    button {
                                        text = "!"
                                        callbackData = "asd"
                                    }
                                }
                                row("send media group from files", "media_group_file")
                                row("send media group from id", "media_group_id")
                            }
                        }
                    }
                }

                handleCallbackQuery {
                    isDataEqual("media_group_file") {
                        sendMediaGroup {
                            photo("files/image1.png".asResources())
                            photo("files/image2.jpg".asResources())
                        }.also {
                            it.forEach {
                                println(it.photo)
                            }
                        }
                    }

                    isDataEqual("media_group_id") {
                        sendMediaGroup {
                            photo("AgACAgIAAxkDAAIInGHCc89QKcGelysXyncJDzAZWaKNAAJMtjEbhJARSv14GxGJpnGuAQADAgADcwADIwQ")
                            photo("AgACAgIAAxkDAAIIm2HCc8-GBzuHeX2wSbK25Pk_RK5bAAJLtjEbhJARSkZuPsUIDkxZAQADAgADcwADIwQ")
                        }
                    }

                    datePickerResult(datePicker) { date ->
                        edit {
                            text = "Date: $date"
                        }
                    }

                    isPage("pagination") { page -> sendPagination(page) }

                    isDataStartWith("pitem") { data ->
                        println("Item #$data")
                    }

                    isDataEqual("dpinit") {
                        edit {
                            text = "test"
                            inlineKeyboard {
                                createDatePicker(datePicker)
                            }
                        }
                    }

                    isDataEqual("pay") {
                        sendInvoice {
                            title = "test"
                            description = "Some desc"
                            payload = "payload"
                            currency = Currency.RUB
                            prices = listOf(
                                LabeledPrice("Item #1", 10000),
                                LabeledPrice("Item #2", 15000),
                                LabeledPrice("Item #3", 20000),
                            )
//                            needShippingAddress = true
//                            needPhoneNumber = true
//                            needEmail = true
//                            needName = true
//                            sendPhoneNumberToProvider = true
//                            sendEmailToProvider = true
//                            isFlexible = true
//                            disableNotification = true
//                            replyToMessageId = query.message?.messageId!!
//                            allowSendingWithoutReply = true
//                            maxTipAmount = 100000
//                            suggestedTipAmounts = listOf(10000, 20000)
//                            providerData = buildJsonObject { put("data", "some data") }
                        }
                    }
                    isDataStartWith("item_") { value ->
                        edit {
                            text = "Item #$value"
                            inlineKeyboard { row { button("back", "item_back") } }
                        }
                    }

                    isDataEqual("hello") {
                        sendHelloMessage("It was hello (${System.currentTimeMillis()})", true)
                    }
                    isDataStartWith(CALLBACK_PREFIX) { value ->
                        sendHelloMessage("Data is $value")
                    }

                    isUnhandled {
                        logger.warn("Unhandled query (${query.data}): $query")
                    }
                }

                handleCommand {
                    when(command.text) {
                        BotCommand.START -> {
                            sendMain()
                        }
                        "/dp" -> {
                            send {
                                text = "Date Picker"
                                inlineKeyboard {
                                    createDatePicker(datePicker)
                                }
                            }
                        }
                        "/pages" -> {
                            sendPagination(Pagination.PAGE_FIRST, isEdit = false)
                        }
                        "/long" -> {
                            Thread.sleep(5000)
                            send("success")
                        }
                    }
                }
            }
    }
}

fun <T> T.sendPagination(page: Long, isEdit: Boolean = true) where T : ISender, T : IEditor {
    fun TextMessage.setup() {
        text = "Choose item"
        inlineKeyboard {
            pagination(pagesData, "pagination", page, setup = {
                columns = 2
            }) {
                button(it.toString(), "pitem$it")
            }
            row("some btn", CallbackQuery.EMPTY_CALLBACK_DATA)
        }
    }

    if (isEdit) edit { setup() }
    else send { setup() }
}

fun String.asResources(): File {
    return ClassLoader.getSystemClassLoader().getResource(this)?.let {
        File(it.toURI())
    } ?: throw IllegalStateException("resource file '$this' not found")
}
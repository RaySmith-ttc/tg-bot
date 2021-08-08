package helper

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotCommand
import ru.raysmith.tgbot.core.ISender
import ru.raysmith.tgbot.core.handler.CallbackQueryHandler
import ru.raysmith.tgbot.model.bot.inlineKeyboard
import ru.raysmith.tgbot.model.network.chat.ChatMember
import ru.raysmith.tgbot.model.network.message.MessageEntityType
import ru.raysmith.tgbot.utils.PaginationRows

class Runner {

    val CALLBACK_PREFIX = "cb_"

    fun ISender.sendMain() {
        send {
            text = "main"
            replyKeyboard {
                inputFieldPlaceholder = "test"
                row { button("send image") }
                row { button("pageable") }
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

        Bot()
            .onError { e -> logger.error(e.message) }
            .onUpdate { updates -> updates.forEach { println(it) } }
            .start {
                handleMyChatMember {
                    if (newChatMember is ChatMember.ChatMemberMember) {
                        send("Я родился")
                    }
                }

                handleMessage {

                    if (messageText == "/start") {
                        sendMain()
                        return@handleMessage
                    }

                    else if (messageText == "send image") {
                        sendPhoto {
                            photo = "AgACAgIAAxkDAAPTYLzxe542hHekjNmlcA3vMMw7XVgAAlqyMRvfveFJJlmwc6u88jc3Bo-hLgADAQADAgADdwADuUkDAAEfBA"
                            caption = "Test image"
                        }
                        return@handleMessage
                    }

                    else if (messageText == "pageable") {
                        val data = (1..6).map(Int::toString)

                        val m = send {
                            text = "Choose item"
                            removeKeyboard()
                        }

                        send {
                            messageId = m.messageId
                            text = "Choose item"
                            inlineKeyboard {
                                row { button("test1", "asd") }
                                pagination(data, PaginationRows.PAGE_FIRST, "pg_first", "pg_", "pg_last") { item ->
                                    button(item, "item_$item")
                                }
                                row { button("test2", "asd") }
                            }
                        }
                        return@handleMessage
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

                            textWithEntities {
                                underline("Hello")
                                append("\n")
                                entity(MessageEntityType.UNDERLINE) {
                                    offset = currentTextLength
                                    length = 3
                                }
                                bold("Phone: ")
                                phoneNumber("+7 950 555-55-55")
                                append("\n")
                                italic("Link: ")
                                url("vk.com")
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
                            }
                        }
                    }
                }

                handleCallbackQuery {

                    isDataEqual("pg_first") {
                        val data = (1..6).map(Int::toString)

                        edit {
                            text = "Choose item"
                            inlineKeyboard {
                                pagination(data, PaginationRows.PAGE_FIRST, "pg_first", "pg_", "pg_last") { item ->
                                    button(item, "item_$item")
                                }
                            }
                        }
                    }
                    isDataEqual("pg_last") {
                        val data = (1..6).map(Int::toString)

                        edit {
                            text = "Choose item"
                            inlineKeyboard {
                                pagination(data, PaginationRows.PAGE_LAST, "pg_first", "pg_", "pg_last") { item ->
                                    button(item, "item_$item")
                                }
                            }
                        }
                    }
                    isDataStartWith("pg_") { _, value ->
                        val data = (1..6).map(Int::toString)

                        edit {
                            text = "Choose item"
                            inlineKeyboard {
                                pagination(data, value.toInt(), "pg_first", "pg_", "pg_last") { item ->
                                    button(item, "item_$item")
                                }
                            }
                        }
                    }
                    isDataEqual("item_back") {
                        val data = (1..6).map(Int::toString)

                        edit {
                            text = "Choose item"
                            inlineKeyboard {
                                pagination(data, PaginationRows.PAGE_FIRST, "pg_first", "pg_", "pg_last") { item ->
                                    button(item, "item_$item")
                                }
                            }
                        }
                    }
                    isDataStartWith("item_") { _, value ->
                        edit {
                            text = "Item #$value"
                            inlineKeyboard { row { button("back", "item_back") } }
                        }
                    }

                    isDataEqual("hello") {
                        sendHelloMessage("It was hello (${System.currentTimeMillis()})", true)
                    }
                    isDataStartWith(CALLBACK_PREFIX) { _, value ->
                        sendHelloMessage("Data is $value")
                    }
                    isUnknown {
                        logger.warn("Unknown query: $query")
                    }
                }

                handleCommand {
                    when(command.text) {
                        BotCommand.START -> {
                            send { sendMain() }
                        }
                    }
                }
            }
    }
}
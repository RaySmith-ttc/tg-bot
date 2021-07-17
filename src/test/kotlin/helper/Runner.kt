package helper

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import ru.raysmith.tgbot.core.*
import ru.raysmith.tgbot.model.bot.inlineKeyboard
import ru.raysmith.tgbot.model.network.message.MessageEntityType

class Runner {

    val CALLBACK_PREFIX = "cb_"

    fun EventHandler.sendMain() {
        send {
            text = "main"
            replyKeyboard {
                row { button("send image") }
            }
        }
    }

    fun EventHandler.sendHelloMessage(messageText: String, isEdit: Boolean = false) {

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
            .start {
                handleMessage {

                    if (messageText == "/start") {
                        sendMain()
                        return@handleMessage
                    }

                    if (messageText == "send image") {
                        sendPhoto {
//                            file = File("C:\\Users\\Ray\\Desktop\\Hearthstone Screenshot 05-21-21 16.30.18.png")
                            photo = "AgACAgIAAxkDAAPTYLzxe542hHekjNmlcA3vMMw7XVgAAlqyMRvfveFJJlmwc6u88jc3Bo-hLgADAQADAgADdwADuUkDAAEfBA"
                            caption = "Test image"
                        }
                        return@handleMessage
                    }

                    println(message)
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

                handleCallbackQuery {
                    isDataEqual("hello") {
                        sendHelloMessage("It was hello (${System.currentTimeMillis()})", true)
                    }
                    isDataStartWith(CALLBACK_PREFIX) { _, value ->
                        sendHelloMessage("Data is $value")
                    }
                    isUnknown {
                        logger.warn("Unknown query: $query")
                        answer()
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
package helper

import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.EventHandler
import ru.raysmith.tgbot.core.ISender
import ru.raysmith.tgbot.core.handler.CallbackQueryHandler
import ru.raysmith.tgbot.core.handler.isCommand
import ru.raysmith.tgbot.core.send
import ru.raysmith.tgbot.model.Currency
import ru.raysmith.tgbot.model.bot.MessageInlineKeyboard
import ru.raysmith.tgbot.model.bot.buildInlineKeyboard
import ru.raysmith.tgbot.model.network.CallbackQuery
import ru.raysmith.tgbot.model.network.chat.ChatMember
import ru.raysmith.tgbot.model.network.command.BotCommand
import ru.raysmith.tgbot.model.network.command.BotCommandScopeChat
import ru.raysmith.tgbot.model.network.command.BotCommandScopeDefault
import ru.raysmith.tgbot.model.network.message.MessageEntityType
import ru.raysmith.tgbot.model.network.message.ParseMode
import ru.raysmith.tgbot.model.network.payment.LabeledPrice
import ru.raysmith.tgbot.network.TelegramApi
import ru.raysmith.tgbot.utils.*
import ru.raysmith.tgbot.utils.datepicker.AdditionalRowsPosition
import ru.raysmith.tgbot.utils.datepicker.DatePicker
import ru.raysmith.tgbot.utils.datepicker.DatePickerState
import ru.raysmith.tgbot.utils.message.MessageAction
import ru.raysmith.tgbot.utils.message.message
import java.io.File
import java.util.*
import kotlin.properties.Delegates

val datePicker = DatePicker("sys").apply {
    locale = Locale.forLanguageTag("us")
//    monthLimitBack = 5
//    monthLimitForward = 3
//    allowFutureDays = false
//    allowPastDays = false
//    years = 2000..2022

//    initDate = LocalDate.of(2000, 1, 1)
//    dates = { data ->
//        println(data)
//        LocalDate.of(2021, 3, 1)..LocalDate.of(2022, 11, 20)
//    }

    yearsColumns = 6
    yearsRows = 4

    startWithState = DatePickerState.YEAR

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
                row("unhandled", "unhandled")
                row("hello", "hello")
            }
        }

    }

    fun CallbackQueryHandler.sendHelloMessage(messageText: String, isEdit: Boolean = false) {

        val keyboard = buildInlineKeyboard {
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
    fun run() {

//        buildHTMLMessageText {
//            text("Test").italic(" text")
//        }

        runBlocking {
            val logger = LoggerFactory.getLogger("bot")
            val notifierBot = Bot(token = "5031990924:AAG-7F1QzGqybvYW1CJdhvFlFUR0s5Icw6Y")
            var bot: Bot? = null

    //        GlobalScope.launch {
    //            delay(5000)
    //            bot!!.restart()
    ////            bot!!.stop()
    //        }

            bot = Bot(token = System.getenv("BOT_TOKEN"), lastUpdateId = 391851722)
                .onError { e -> logger.error(e.message, e) }
                .onUpdate { updates ->
//                    updates.forEach { println(it.callbackQuery?.data ?: it.message?.text) }
                    println(updates.firstOrNull())
                }
                .registerDatePicker(datePicker)
    //            .registerPagination(pagination)
                .enableBlocking { u -> u.findFrom() }
                .shutdownCommand("/shutdown")

            bot.start {
                handleMyChatMember {
                    if (newChatMember is ChatMember.ChatMemberMember) {
                        send("Я родился")
                    }
                }

                handlePreCheckoutQuery {
                    answerPreCheckoutQuery(true)
                }

                handleMessage {
                    Delegates.notNull<Int>()
                    if (message.photo != null) {
                        TelegramApi.service.getFile(message.photo!!.last().fileId).execute().body()?.let { fr ->
                            TelegramApi.fileService.downLoad(fr.file.path!!).execute().body()?.byteStream()!!.also {
                                println(it.readBytes().size)
                            }
                        }
                    } else if (message.text == "send image") {
                        sendPhoto {
                            photo("AgACAgIAAxkDAAPTYLzxe542hHekjNmlcA3vMMw7XVgAAlqyMRvfveFJJlmwc6u88jc3Bo-hLgADAQADAgADdwADuUkDAAEfBA")
                            caption = "Test image"
                        }
                        return@handleMessage
                    } else if (message.text == "send image2") {
                        sendPhoto {
                            photo(File("C:\\Users\\Ray\\Desktop\\image1.png")/*.readBytes(), "test", "image/jpg"*/)
                            caption = "Test image2"
                        }
                        return@handleMessage
                    } else if (message.text == "send other id") {
                        send("1") {
                            text = "this is not sent"
                        }
                    } else if (message.text == "text") {
                        send {
                            parseMode = ParseMode.HTML
                            text = "<b>bold</b>"
                        }
                    } else {
                        send {
                            disableWebPagePreview = true
                            textWithEntities {
                                underline("Hello").text("\n")
                                bold("Phone: ").phoneNumber("+7 950 555-55-55")
                                text("\n")
                                italic("Lik: ").textLink("vk", "vk.com")
                                text("\n")
                                text("Mixed: ").strikethrough("qwe")
                                    .mix("rty", MessageEntityType.STRIKETHROUGH, MessageEntityType.UNDERLINE)
                                    .strikethrough("uio")
                                text("\n")
                                text("Mention: ").mention("@raysmithdev")
                                text("\n")
                                text("Text mention: ").textMention("ray", message.from!!)
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
    //                        Thread.sleep(2000)
                    }
                }

                handleCallbackQuery(alwaysAnswer = true) {
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

                        }
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
//                            providerToken = "999999999:TEST:55555"
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

                    isDataEqual("no_answer") { sendPagination2(1, "no_answer_p_"); answer() }
                    isDataEqual("answer") { sendPagination2(1, "answer_p_"); answer() }
                    isDataStartWith("no_answer_p_") { page ->
                        sendPagination2(page.toLong(), "no_answer_p_")
                    }
                    isDataStartWith("answer_p_") { page ->
                        sendPagination2(page.toLong(), "answer_p_")
                        answer()
                    }

                    isDataEqual("answertest") {
                        sendAnswerVariants(MessageAction.EDIT)
                        answer()
                    }

                    isUnhandled {
                        logger.warn("Unhandled query (${query.data}): $query")
                    }
                }

                handleCommand {
                    if (command.mentionIsNotCurrentBot()) return@handleCommand

                    fun getChatIdFromCommandsCommand(args: String?) =
                        args?.split("\n")?.find { it.startsWith("chat_id") }?.split("=")?.last() ?: message.chat.id.toString()

                    fun collectCommandsFromArgs(args: String) = args
                        .split("\n")
                        .filterNot { it.startsWith("chat_id") }
                        .filter { it.matches("^[a-z0-9_]+\\s-\\s.+$".toRegex()) }
                        .associate { arg -> arg.split(" - ").let { it.first() to it.last() } }
                        .map { BotCommand(it.key, it.value) }

                    fun updateCommands(commands: List<BotCommand>, chatId: String?) {
                        setMyCommands(commands, chatId?.let { BotCommandScopeChat(it) } ?: BotCommandScopeDefault())
                        send(buildString {
                            if (chatId != null) {
                                append("Команды для чата #${chatId} обновлены:\n")
                            } else {
                                append("Команды по умолчанию обновлены:\n")
                            }
                            commands.forEach {
                                append("${it.command} - ${it.description}\n")
                            }
                        })
                    }

                    isCommand("provide_commands_control") { args ->
                        val chatId = message.chat.id.toString()
                        val commands = getMyCommands(BotCommandScopeChat(chatId)).toMutableList().apply {
                            addAll(listOf(
                                BotCommand("set_commands", "Установить новый список команд"),
                                BotCommand("add_commands", "Добавить команду"),
                                BotCommand("delete_commands", "Удалить команды"),
                                BotCommand("get_commands", "получить текущий список команд"),
                            ))
                        }

                        updateCommands(commands, chatId)
                    }

                    isCommand("set_commands") { args ->
                        args ?: return@isCommand
                        val newCommands = collectCommandsFromArgs(args)

                        if (newCommands.isEmpty()) {
                            send {
                                textWithEntities {
                                    text("Неверный синтаксис. Использование команды:")
                                    pre("""
                                        /set_commands
                                        [chat_id = <chat_id>]
                                        <command1> - <description1>
                                        [<command2> - <description2>]
                                        ...
                                    """.trimIndent())
                                }
                            }
                            return@isCommand
                        }

                        updateCommands(newCommands, getChatIdFromCommandsCommand(args))
                    }

                    isCommand("add_commands") { args ->
                        args ?: return@isCommand
                        val chatId = getChatIdFromCommandsCommand(args)
                        val newCommands = collectCommandsFromArgs(args)

                        if (newCommands.isEmpty()) {
                            send {
                                textWithEntities {
                                    text("Неверный синтаксис. Использование команды:")
                                    pre("""
                                        /add_commands
                                        [chat_id = <chat_id>]
                                        <command1> - <description1>
                                        [<command2> - <description2>]
                                        ...
                                    """.trimIndent())
                                }
                            }
                            return@isCommand
                        }

                        val commands = getMyCommands(BotCommandScopeChat(chatId)).toMutableList().apply {
                            addAll(newCommands)
                        }

                        updateCommands(commands, chatId)
                    }

                    isCommand("delete_commands") { args ->
                        val chatId = getChatIdFromCommandsCommand(args)

                        val commandsToDelete = args?.split("\n")
                            ?.filterNot { it.startsWith("chat_id") }
                            ?.filter { it.matches("^[a-z0-9_]+$".toRegex()) }
                            ?: emptyList()

                        if (commandsToDelete.isNotEmpty()) {
                            updateCommands(
                                getMyCommands(BotCommandScopeChat(chatId)).toMutableList()
                                    .filterNot { it.command in commandsToDelete },
                                chatId
                            )
                        } else {
                            send {
                                textWithEntities {
                                    text("Неверный синтаксис. Использование команды:")
                                    pre("""
                                        /delete_commands
                                        [chat_id = <chat_id>]
                                        <command1> - <description1>
                                        [<command2> - <description2>]
                                        ...
                                    """.trimIndent())
                                }
                            }
                        }
                    }

                    isCommand("get_commands") { args ->
                        val chatId = getChatIdFromCommandsCommand(args)
                        getMyCommands(BotCommandScopeChat(chatId)).also { commands ->
                            send(buildString {
                                append("Команды для чата #${chatId}:\n")

                                commands.forEach {
                                    append("${it.command} - ${it.description}\n")
                                }
                            })
                        }
                    }

                    isCommand("start") { args ->
                        if (args == "answertest") sendAnswerVariants(MessageAction.SEND)
                        else sendMain()
                    }

                    isCommand("keybloard") { args ->
                        send {
                            text = "test"
                            keyboardMarkup = MessageInlineKeyboard(buildInlineKeyboard {
                                row("1", "1")
                                row {
                                    button("2", "2")
                                    button("3", "3")
                                }
                            }.filter { it.getRow().size == 1 }.toMutableList())
                        }
                    }

                    isCommand("pages") {
                        sendPagination(Pagination.PAGE_FIRST, MessageAction.SEND)
                    }

                    isCommand("long") {
                        Thread.sleep(5000)
                        send("success")
                    }

                    isCommand("mrkd") {
                        send {
                            parseMode = ParseMode.MARKDOWNV2
                            text = buildMarkdownV2String {
                                text("2+2=4\n")
                                bold("2*2=4").text("\n")
                                strikethrough("2+2=4").text("\n")
                                italic("snake_case").text("\n")
                                text("c: ").code("code`\\").text("\n")
                                text("c: ").pre("code`\\", "unk").text("\n")
                                text("te[xt").text("\n")
                                spoiler("|||||").text("\n")
                                text("_*[]???()~`>#+-=|{}.!\n")
//                                italic("ital").mix("ik under", MessageEntityType.ITALIC, MessageEntityType.UNDERLINE).underline("line\n")
                                mix("italic underline", MessageEntityType.ITALIC, MessageEntityType.UNDERLINE)
//                                underline("1").underline("2")
//                                italic("1").italic("2")
//                                underline("1").italic("2").underline("3")
//                                italic("1").underline("2").italic("3")
                            }
                        }
                    }

                    isCommand("serialize") {
                        val string = buildInlineKeyboard {
                            row("title", "query")
                        }.let { TelegramApi.json.encodeToString(it) }

                        send {
                            text = string
                            keyboardMarkup = TelegramApi.json.decodeFromString(string)
                        }
                    }

                    isCommand("safe") {
                        val allowed = "1234567890qwertyuiopasdfghjklzxcvbnm"
                        val string = generateSequence(allowed.random()) { allowed.random() }
                        val entityTypes = listOf(MessageEntityType.BOLD, MessageEntityType.ITALIC, MessageEntityType.UNDERLINE, MessageEntityType.STRIKETHROUGH)
                        send {
                            textWithEntities {
                                repeat(100) {
                                    appendEntity(entityTypes.random(), text = string.take(50).joinToString(""))
                                    text("\n")
                                }
                            }
                        }
                    }

                    isCommand("dp") {
                        send {
                            text = "Date Picker"
                            inlineKeyboard {
                                createDatePicker(datePicker)
                            }
                        }
                    }

                    isCommand("dp5") {
                        send {
                            text = "Date Picker"
                            inlineKeyboard {
                                createDatePicker(datePicker, "qwertyuiopasdfghjklzxccvbnm1234567890")
                            }
                        }
                    }

                    isCommand("answertest") {
                        sendAnswerVariants(MessageAction.SEND)
                    }

                    isCommand("service") {
                       //                            withService(TelegramApi.service) {
//                                send("main")
//                                withService(telegramServiceOfNotifierBot) {
//                                    send("second")
//                                }
//                            }
//                            withService(telegramServiceOfNotifierBot) {
//                                send("second")
//                            }
                       withBot(notifierBot) {
                           send("second")
                       }

                       send("main")
                    }
                }
            }

    //        Thread.sleep(100000000)
        }
    }
}

fun EventHandler.sendAnswerVariants(action: MessageAction) = message(action) {
    text = "Choose variant"
    inlineKeyboard {
        row("No answer", "no_answer")
        row("answer", "answer")
    }
}

fun EventHandler.sendPagination(page: Long, action: MessageAction = MessageAction.EDIT) = message(action) {
    if (action == MessageAction.SEND) {
        text = "Choose item"
    }
    inlineKeyboard {
        pagination(pagesData, "pagination", page, setup = {
            columns = 2
        }) {
            button(it.toString(), "pitem$it")
        }
        row("some btn", CallbackQuery.EMPTY_CALLBACK_DATA)
    }
}

fun EventHandler.sendPagination2(page: Long, prefixCallback: String, action: MessageAction = MessageAction.EDIT) = message(action) {
    text = "Data: ${System.currentTimeMillis()}"
    inlineKeyboard {
        when(page) {
            1L -> row("»", prefixCallback + 2)
            5L -> row("«", prefixCallback + 4)
            else -> {
                row {
                    button("«", prefixCallback + (page - 1))
                    button("»", prefixCallback + (page + 1))
                }
            }
        }
        row("« Back", "answertest")
    }
}

fun String.asResources(): File {
    return ClassLoader.getSystemClassLoader().getResource(this)?.let {
        File(it.toURI())
    } ?: throw IllegalStateException("resource file '$this' not found")
}
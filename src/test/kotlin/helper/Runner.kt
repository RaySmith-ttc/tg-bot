package helper

import io.ktor.client.*
import kotlinx.coroutines.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.ISender
import ru.raysmith.tgbot.core.handler.EventHandler
import ru.raysmith.tgbot.core.handler.base.CallbackQueryHandler
import ru.raysmith.tgbot.core.handler.base.isCommand
import ru.raysmith.tgbot.core.send
import ru.raysmith.tgbot.model.Currency
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.bot.asChatId
import ru.raysmith.tgbot.model.bot.message.MessageText
import ru.raysmith.tgbot.model.bot.message.keyboard.MessageInlineKeyboard
import ru.raysmith.tgbot.model.bot.message.keyboard.buildInlineKeyboard
import ru.raysmith.tgbot.model.network.CallbackQuery
import ru.raysmith.tgbot.model.network.chat.ChatAdministratorRights
import ru.raysmith.tgbot.model.network.chat.forum.IconColor
import ru.raysmith.tgbot.model.network.chat.member.ChatMemberMember
import ru.raysmith.tgbot.model.network.command.BotCommand
import ru.raysmith.tgbot.model.network.command.BotCommandScopeChat
import ru.raysmith.tgbot.model.network.command.BotCommandScopeDefault
import ru.raysmith.tgbot.model.network.inline.content.InputTextMessageContent
import ru.raysmith.tgbot.model.network.inline.result.InlineQueryResultArticle
import ru.raysmith.tgbot.model.network.keyboard.KeyboardButtonPollType
import ru.raysmith.tgbot.model.network.keyboard.KeyboardButtonRequestChat
import ru.raysmith.tgbot.model.network.keyboard.KeyboardButtonRequestUser
import ru.raysmith.tgbot.model.network.media.input.InputFile
import ru.raysmith.tgbot.model.network.media.input.InputMediaPhoto
import ru.raysmith.tgbot.model.network.media.input.asTgFile
import ru.raysmith.tgbot.model.network.menubutton.MenuButtonCommands
import ru.raysmith.tgbot.model.network.menubutton.MenuButtonDefault
import ru.raysmith.tgbot.model.network.menubutton.MenuButtonWebApp
import ru.raysmith.tgbot.model.network.menubutton.WebAppInfo
import ru.raysmith.tgbot.model.network.message.Message
import ru.raysmith.tgbot.model.network.message.MessageEntityType
import ru.raysmith.tgbot.model.network.message.ParseMode
import ru.raysmith.tgbot.model.network.message.PollType
import ru.raysmith.tgbot.model.network.payment.LabeledPrice
import ru.raysmith.tgbot.model.network.sticker.InputSticker
import ru.raysmith.tgbot.model.network.sticker.StickerFormat
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.network.TelegramApi2
import ru.raysmith.tgbot.network.TelegramService2
import ru.raysmith.tgbot.utils.*
import ru.raysmith.tgbot.utils.datepicker.AdditionalRowsPosition
import ru.raysmith.tgbot.utils.datepicker.DatePicker
import ru.raysmith.tgbot.utils.datepicker.DatePickerState
import ru.raysmith.tgbot.utils.locations.LocationConfig
import ru.raysmith.tgbot.utils.message.MessageAction
import ru.raysmith.tgbot.utils.message.message
import ru.raysmith.utils.takeOrCut
import java.io.File
import java.io.IOException
import java.time.LocalDate
import kotlin.time.Duration.Companion.minutes

val locations = true
val prettyPrintJson = Json(TelegramApi2.json) {
    prettyPrint = true
    prettyPrintIndent = " "
}

inline fun <reified T> T.toJson() = prettyPrintJson.encodeToString(this)

suspend inline fun <reified T> ISender.sendAsJson(value: T) = send {
    textWithEntities {
        println(value)
        println(value.toJson())
        pre(value.toJson(), "json")
    }
}

var loc: String = "menu"

val newApi = object : TelegramService2 {
    override val client: HttpClient = TelegramApi2.defaultClient()
}

var lookPollAnswers = false

val datePicker = DatePicker("sys").apply {
//    locale = Locale.forLanguageTag("us")
    messageText = { data, state ->
        bold("Title").n()
        n()
        text("Data: $data").n()
        text("State: $state").n()
    }
    dates = {
        LocalDate.now()..LocalDate.now().plusYears(3)
    }
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

    startWithState = DatePickerState.DAY

    additionalRowsPosition = AdditionalRowsPosition.TOP
    additionalRows = {
        row("some btn", CallbackQuery.EMPTY_CALLBACK_DATA)
    }
    additionalRowsVisibleOnStates = setOf(DatePickerState.DAY, DatePickerState.YEAR)
}

fun MessageText.setupTestMessage(message: Message) {
    text("text").n()
    underline("Hello").n()
    bold("Phone: ").phoneNumber("+79876543210").n()
    bold("Phone: ").phoneNumber("+79876543210").n()
    italic("Lik: ").textLink("vk", "vk.com").n()
    text("Mixed: ").strikethrough("qwe")
        .mix("rty", MessageEntityType.STRIKETHROUGH, MessageEntityType.UNDERLINE)
        .strikethrough("uio").n()
    text("Mention: ").mention("@raysmithdev").n()
    text("Text mention: ").textMention("ray", message.from!!).n()
    spoiler("Spoiler").n()
//    text("emoji: ").emoji("\uD83D\uDC4D")
    text("custom emoji: ").emoji("\uD83D\uDC4D", "5368324170671202286")

    entity(MessageEntityType.STRIKETHROUGH) {
        offset = 2
        length = 30
    }
}

val pagesData = 1..120

class Runner {

    val CALLBACK_PREFIX = "cb_"

    suspend fun ISender.sendMain() {
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

    suspend fun CallbackQueryHandler.sendHelloMessage(messageText: String, isEdit: Boolean = false) {

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
        val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
//        scope.launch { startWebAppServer() }
        runBlocking {
//            startWebAppServer()
            val logger = LoggerFactory.getLogger("bot")
            val notifierBot = Bot(token = "5031990924:AAG-7F1QzGqybvYW1CJdhvFlFUR0s5Icw6Y")

            val bot = Bot(token = System.getenv("BOT_TOKEN")/*, lastUpdateId = 391851722*/)
                .onError { e -> logger.error(e.message, e) }
                .onUpdate { updates ->
//                    updates.forEach { println(it.callbackQuery?.data ?: it.message?.text) }
                    updates.forEach {
                        println("${it.type}: ${it.toJson()}")
                    }
                }.config {
//                    this.locale = Locale.JAPAN
                }
                .registerDatePicker(datePicker)
//                .registerPagination(pagination)
//                .enableBlocking { u -> u.findFrom() }
                .enableBlocking { u -> u.findChatId() }
                .shutdownCommand("/shutdown")
                .onStop {
                    println("Bot stopped by command: ${it?.command}")
                }

//            launch {
//                delay(5000)
//                bot.restart()
//            }

            class LocationConfigImpl(override val update: Update) : LocationConfig {
                val userId by lazy { update.findFrom()?.id?.value ?: -1 }
                var foo = "bar"
            }
            
            if (locations) {
                bot.locations<LocationConfigImpl> {
                    getLocation {
                        userId
                        loc
                    }
                    config {
                        LocationConfigImpl(it)
                    }
                    updateLocation {
                        loc = it.name
                    }

                    filter { true }

                    global {
                        handleMyChatMember {
                            send(update.findChatId()!!) {
                                text = update.findChatId()!!.toString()
                            }
                        }
                        handleCommand {
                            isCommand(ru.raysmith.tgbot.model.bot.BotCommand.START) {
                                send {
                                    text = "test"
                                    inlineKeyboard {
                                        row("asd", "asd")
                                    }
                                }
                            }
                            isCommand("menu") {
                                toLocation("menu")
                            }
                            isCommand("other") {
                                toLocation("other")
                            }
                            isCommand("poll") {
                                toLocation("poll")
                            }

                            val me = withBot(bot) {
                                getMe()
                            }
                        }

                        handleEditedMessage {
                            println("Message was edit")
                        }

                        handleChannelPost {
                            println(channelPost)
                        }

                        handleEditedChannelPost {
                            println(channelPost)
                        }

                        handleCallbackQuery(alwaysAnswer = true) {
                            println("handleCallbackQuery [global]")
                        }

                        handleMessage {
                            send {
                                text = "test"
                                inlineKeyboard {
                                    row("asd", "asd")
                                }
                            }
                        }
                    }
//
                    location("menu") {
                        onEnter {
//                            send {
//                                text = "in menu"
//                                inlineKeyboard {
//                                    row("dont do anything", "fv932mdcl")
//                                }
//                            }
                            println("on enter menu")
                        }

                        handle {
                            handleCommand {
                                isCommand("menu") {
                                    send("menu")
                                }

                                isCommand("toother") {
                                    foo = "changed"
                                    toLocation("other")
                                }
                            }

                            handleMessage {
                                send("handled in $name")
                            }

                            handleCallbackQuery(alwaysAnswer = false) {
                                println("handleCallbackQuery [menu]")
                            }

                            handleUnknown {
                                send("unknown")
                            }
                        }

                    }

                    location("other") {
                        onEnter {
//                            send("in other\nfoo:${foo}")
                            println("on enter other")
                        }

                        handle {
                            handleCommand {
                                isCommand("repeat") {
                                    toLocation("other")
                                }
                                isCommand("other") {
                                    send("other")
                                }
                                isCommand("tomenu") {
                                    toLocation("menu")
                                }
                                isCommand("dp") {
                                    send {
                                        text = "dp"
                                        inlineKeyboard {
                                            createDatePicker(datePicker)
                                        }
                                    }
                                }
                            }

                            handleEditedMessage {

                                println("Message was edit in other")
                            }

                            handleMessage {
                                send("handled in $name")

                                messageText()
                                    .verify { it.contains(".") }
                                    .convert { it.toFloatOrNull() }
                                    .verify { it > 0.0 }
                                    .onResult {
                                        send("value: $it")
                                    } ?: send("incorrect")
                            }

//                        handleCallbackQuery {
//                            datePickerResult(datePicker) {
//                                send("result: $it")
//                            }
//                        }
                        }
                    }

                    location("poll") {
                        onEnter {
                            val poll = sendPoll("Question", listOf("1", "2", "3")) {
                                isAnonymous = false
                                type = PollType.QUIZ
                                correctOptionId = 0
                                explanationWithEntities {
                                    bold("Expl: ").text("Wrong answer")
                                }
                                openPeriod = 10
                                inlineKeyboard {
                                    row("Button", "btn")
                                }
                            }

                            runBlocking { delay(5000) }
                            stopPoll(poll.messageId) {
                                row("New button", "btn")
                            }
                        }

                        handle {
                            handlePoll {
                                println("Receive poll #${poll.id}")
                            }

                            handlePollAnswer {
                                println("Receive poll answer: ${pollAnswer.optionIds}")
                            }
                        }
                    }
                }
            }
            
            bot.start { bot ->
                handleEditedMessage {
                    send(buildString {
                        append("Detect editing: \n")
                        append("New text: ${message.text?.takeOrCut(256)}\n")
                        append("Date: $editDate")
                    })
                }

                if (lookPollAnswers) {
                    handlePollAnswer {
                        println("answer")
                    }
                }

                handleChatJoinRequest {
                    println(chatJoinRequest)
                    approve()
                }

                handleInlineQuery {
                    val res = buildList {
                        repeat(1000) {
                            add(
                                InlineQueryResultArticle(
                                    "iq_photo_$it", "test $it", InputTextMessageContent("Message text $it")
                                )
                            )
                        }
                    }

                    answer(
                        id = inlineQuery.id,
                        results = res.filter { it.title.contains(inlineQuery.query) }.drop(inlineQuery.offset.toIntOrNull() ?: 0).take(50),
                        nextOffset = ((inlineQuery.offset.toIntOrNull() ?: 0) + 50).toString()
                    )
                }

                handleChosenInlineQuery { 
                    println("handleChosenInlineQuery: ${inlineResult.resultId}")
                }

                handleMyChatMember {
                    if (newChatMember is ChatMemberMember) {
                        send("Я родился")
                    }
                }

                handlePreCheckoutQuery {
                    answerPreCheckoutQuery(true)
                }

                handleMessage {
                    messageContact()
                        .verify { (it.userId?.value ?: 0) > 0 }
                        .convert { it.firstName + " " + it.lastName }
                        .onResult {
                            send(it)
                        }
                    
                    messageContact(/*verification = { it.phoneNumber != "" }*/) {
                        send(it.toString())
//                    } ?: messagePhoto {
//                        it.inputStream(this).readBytes().size.let {
//                            send("Bytes: $it")
//                        }
                    } ?: messagePhoto()
                        .verify { it.height != 9999 }
                        .convert { it.fileName ?: "unknown" }
                        .onResult {
//                            it.inputStream(this).readBytes().size.let {
//                                send("Bytes: $it")
//                            }
                            send("Filename: $it")
                    } ?: messageAudio {
                        send(it.fileId)
                    } ?: run {
                        if (message.text == "send image") {
                            sendPhoto {
                                photo = "AgACAgIAAxkDAAPTYLzxe542hHekjNmlcA3vMMw7XVgAAlqyMRvfveFJJlmwc6u88jc3Bo-hLgADAQADAgADdwADuUkDAAEfBA".asTgFile()
                                caption = "Test image"
                            }
                            return@handleMessage
                        } else if (message.text == "send image2") {
                            sendPhoto {
                                photo = File("C:\\Users\\Ray\\Desktop\\image1.png").asTgFile()
                                caption = "Test image2"
                            }
                            return@handleMessage
                        } else if (message.text == "send other id") {
                            send(1.asChatId()) {
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
                                    setupTestMessage(message)
                                }

                                inlineKeyboard {
                                    row {
                                        button("hello", "hello")
                                        button("world", "${CALLBACK_PREFIX}world")
                                        row {  }
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
                }

                handleCommand {
                    if (!command.mentionIsCurrentBot(bot.me)) return@handleCommand

                    fun getChatIdFromCommandsCommand(args: String?) =
                        (args?.split("\n")?.find { it.startsWith("chat_id") }?.split("=")?.last()?.toLongOrNull() ?: message.chat.id.value).toChatId()

                    fun collectCommandsFromArgs(args: String) = args
                        .split("\n")
                        .filterNot { it.startsWith("chat_id") }
                        .filter { it.matches("^[a-z0-9_]+\\s-\\s.+$".toRegex()) }
                        .associate { arg -> arg.split(" - ").let { it.first() to it.last() } }
                        .map { BotCommand(it.key, it.value) }

                    suspend fun updateCommands(commands: List<BotCommand>, chatId: ChatId?) {
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

                    isCommand("me") {
                        send {
                            textWithEntities {
                                text("userId: ").code(message.from?.id?.value).n()
                                text("chatId: ").code(message.chat.id.value).n()
                                text("botId: ").code(bot.me.id.value).n()
                            }
                        }
                    }

                    isCommand("lookPollAnswers") {
                        lookPollAnswers = true
                    }

                    isCommand("reloadConfig") {
                        bot.reloadConfig()
                    }

                    isCommand("exportChatInviteLink") {
                        sendAsJson(exportChatInviteLink((-4073352030).toChatId()))
                    }

                    isCommand("createChatInviteLink") {
                        sendAsJson(createChatInviteLink((-4073352030).toChatId()))
                    }

                    isCommand("setChatPhoto1") {
                        setChatPhoto("files/image1.png".asResources().asTgFile())
                    }

                    isCommand("deleteChatPhoto") {
                        deleteChatPhoto()
                    }

                    isCommand("setChatStickerSet") {
                        setChatStickerSet("thinking")
                    }

                    isCommand("getChatAdministrators") {
                        sendAsJson(getChatAdministrators())
                    }

                    isCommand("exception") {
                        throw IOException("test")
                    }

                    isCommand("provide_commands_control") {
                        val chatId = message.chat.id
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

                    isCommand("send_username") {
                        send(ChatId.of("@dskfijfwndnslllaa1")) {
                            text = "send_username"
                        }
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

                    isCommand("html") {
                        send {
                            parseMode = ParseMode.HTML
                            text = buildHTMLString {
                                setupTestMessage(message)
                            }
                        }
                    }

                    isCommand("setChatMenuButtonWeb") {
                        setChatMenuButton(MenuButtonWebApp("Test", WebAppInfo("https://192.168.0.104:8890")))
                        send("success")
                    }

                    isCommand("setChatMenuButtonDefault") {
                        setChatMenuButton(MenuButtonDefault)
                        send("success")
                    }

                    isCommand("setChatMenuButtonCommands") {
                        setChatMenuButton(MenuButtonCommands)
                        send("success")
                    }

                    isCommand("getChatMenuButton") {
                        send(getChatMenuButton().toString())
                    }

                    isCommand("setMyDefaultAdministratorRights") {
                        setMyDefaultAdministratorRights(ChatAdministratorRights(isAnonymous = true))
                        send("success")
                    }

                    isCommand("getMyDefaultAdministratorRights") {
                        send(getMyDefaultAdministratorRights().toString())
                    }

                    isCommand("stopPoll") {
                        val poll = sendPoll("Question", listOf("1", "2", "3")) {
                            type = PollType.QUIZ
                            correctOptionId = 0
                            explanationWithEntities {
                                bold("Expl: ").text("Wrong answer")
                            }
                            openPeriod = 5
                            inlineKeyboard {
                                row("Button", "btn")
                            }
                        }

                        runBlocking { delay(2000) }
                        stopPoll(poll.messageId) {
                            row("New button", "btn")
                        }
                    }

                    isCommand("editCaption") {
                        val message = sendPhoto {
                            caption = "Test"
                            photo = "AgACAgIAAxkDAAPTYLzxe542hHekjNmlcA3vMMw7XVgAAlqyMRvfveFJJlmwc6u88jc3Bo-hLgADAQADAgADdwADuUkDAAEfBA".asTgFile()
                        }
                        runBlocking { delay(2000) }
                        val editedMessage = editCaption(messageId = message.messageId) {
                            italic("edited")
                        }
                        runBlocking { delay(2000) }
                        editMedia<InputMediaPhoto>(messageId = message.messageId, media = InputMediaPhoto(
                            media = "AgACAgIAAxkBAAI9AWOCdwVufweWJV-HNe5pkexZa83GAAKRwzEbKkIQSHYZWoXjyhFNAQADAgADcwADKwQ",
                            caption = editedMessage.caption,
                            parseMode = null,
                            captionEntities = editedMessage.captionEntities
                        )) {
                            row("Button", "btn")
                        }
                        runBlocking { delay(2000) }
                        editReplyMarkup(messageId = message.messageId) {
                            row("New button", "btn")
                        }
                    }

                    isCommand("mrkd2") {
                        send {
                            parseMode = ParseMode.MARKDOWNV2
                            text = buildMarkdownV2String {
                                setupTestMessage(message)
                            }
                        }
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

                    isCommand("wh_create") {
                        send(setWebhook("https://google.com").toString())
                    }

                    isCommand("wh_info") {
                        send(getWebhookInfo().toString())
                    }

                    isCommand("wh_delete") {
                        send(deleteWebhook().toString())
                    }

                    isCommand("get_commands") { args ->
                        val chatId = getChatIdFromCommandsCommand(args)
                        println(chatId)
                        getMyCommands(BotCommandScopeChat(chatId)).also { commands ->
                            send(buildString {
                                append("Команды для чата #${chatId}:\n")

                                commands.forEach {
                                    append("${it.command} - ${it.description}\n")
                                }
                            })
                        }
                    }

                    isCommand("paylink") {
                        send(createInvoiceLink {
                            title = "test"
                            description = "Some desc"
                            payload = "payload"
                            currency = Currency.RUB
                            prices = listOf(
                                LabeledPrice("Item #1", 10000),
                                LabeledPrice("Item #2", 15000),
                                LabeledPrice("Item #3", 20000),
                            )
                            providerToken = "381764678:TEST:29287"
                        })
                    }

                    isCommand("pay") {
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
                            providerToken = "381764678:TEST:29287"
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

                    isCommand("start") { args ->
                        if (args == "answertest") sendAnswerVariants(MessageAction.SEND)
                        else if (args == "webApp") {
                            send {
                                text = "Web"
                                inlineKeyboard {
                                    row {
                                        button {
                                            text = "Open"
                                            webApp = WebAppInfo("https://sandbox.raysmith.ru:8890/payment")
                                        }

                                    }
                                }
                            }
                        }
                        else sendMain()
                    }

                    isCommand("keybloard") {
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

                    // TODO failed
                    isCommand("serialize") {
                        val string = buildInlineKeyboard {
                            row("title", "query")
                        }.let { TelegramApi2.json.encodeToString(it) }

                        send {
                            text = string
                            keyboardMarkup = TelegramApi2.json.decodeFromString(string)
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
                            datePicker(datePicker, "somedata")
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

                    isCommand("photo") {
                        sendPhoto {
                            photo = "files/image1.png".asResources().asTgFile()
                            captionWithEntities {
                                bold("Title").n()
                                n()
                                text("Description")
                            }
                        }
                    }

                    isCommand("document") {
                        sendDocument {
                            document = "files/sample.pdf".asResources().asTgFile()
                            thumbnail = "files/image2.jpg".asResources().asTgFile()
                            captionWithEntities {
                                bold("Title").n()
                                n()
                                text("Description")
                            }
                        }
                    }

                    isCommand("document_group") {
                        sendMediaGroup {
                            document("files/sample.pdf".asResources().asTgFile(), "files/image1.png".asResources().asTgFile()) {
                                text("1")
                            }
                            document("BQACAgIAAxkDAAJNKmUZPvampKPYTSeooUMPeofckLWfAAJlMQACbm3ISGVRMziukbaTMAQ".asTgFile(), "files/image2.jpg".asResources().asTgFile()) {
                                text("2")
                            }
                        }
                    }

                    isCommand("audio") {
                        sendAudio {
                            audio = "audio2.mp3".asResources().asTgFile()
                            thumbnail = "files/size.small.jpg".asResources().asTgFile()
                        }
                    }

                    isCommand("send_audio") {
                        sendMediaGroup {
                            audio("audio2.mp3".asResources().asTgFile(), "files/size.small.jpg".asResources().asTgFile()) {
                                bold("Test ").n().italic("message")
                            }
                            audio(
                                InputFile.ByteArray("audio1.mp3".asResources().readBytes(), "audio1", "audio/mp3"),
                                "files/size.small.jpg".asResources().asTgFile()
                            )

                            audio("audio2.mp3".asResources().asTgFile(), "files/size.small.jpg".asResources().asTgFile()) {
                                bold("Test ").n().italic("message 2")
                            }
                        }
                    }

                    isCommand("forwardMe") {
                        forwardMessage(
                            fromChatId = getChatIdOrThrow(),
                            messageId = message.messageId
                        )
                    }

                    isCommand("copyMe") {
                        copyMessage(getChatIdOrThrow(), messageId!!)
                    }

                    isCommand("answertest") {
                        sendAnswerVariants(MessageAction.SEND)
                    }

                    isCommand("webApp") {
                        send {
                            text = "Web"
                            inlineKeyboard {
                                row {
                                    button {
                                        text = "Open"
                                        webApp = WebAppInfo("https://sandbox.raysmith.ru:8890/payment")
                                    }

                                }
                            }
                        }
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

                    isCommand("safe2") {
                        send {
                            textWithEntities {
                                text("test")
                            }
                        }

                        send {
                            textWithEntities {
                                safeTextLength = true
                                text(generateString(5000))
                            }
                        }

                        try {
                            send {
                                textWithEntities {
                                    safeTextLength = false
                                    text("test")
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                        send {
                            textWithEntities {
                                safeTextLength = true
                                repeat(50) {
                                    when((1..5).random()) {
                                        1 -> text(generateString(100))
                                        2 -> bold(generateString(100))
                                        3 -> italic(generateString(100))
                                        4 -> strikethrough(generateString(100))
                                        5 -> code(generateString(100))
                                    }
                                }
                            }
                        }

                        try {
                            send {
                                textWithEntities {
                                    safeTextLength = false
                                    repeat(50) {
                                        when((1..5).random()) {
                                            1 -> text(generateString(100))
                                            2 -> bold(generateString(100))
                                            3 -> italic(generateString(100))
                                            4 -> strikethrough(generateString(100))
                                            5 -> code(generateString(100))
                                        }
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }

                    isCommand("safe3") {
                        send {
                            text = generateString(5000)
                            safeTextLength = true
                        }
                    }

                    isCommand("video") {
                        sendVideo {
                            captionWithEntities {
                                bold("Title").n()
                                n()
                                text("Body")
                            }
                            hasSpoiler = true
                            video = "files/video1.mp4".asResources().asTgFile()
                            duration = 30
                            width = 640
                            height = 360
                            supportsStreaming = false
                        }
                    }

                    isCommand("video_group") {
                        sendMediaGroup {
                            video("files/video1.mp4".asResources().asTgFile()) {
                                bold("Title").n()
                                n()
                                text("Body")
                            }
                            video("files/video1.mp4".asResources().asTgFile(), hasSpoiler = true)
                        }
                    }

                    isCommand("animation") {
                        sendAnimation {
                            captionWithEntities {
                                bold("Title").n()
                                n()
                                text("Body")
                            }
                            animation = "files/anim1.gif".asResources().asTgFile()
                            hasSpoiler = true
                        }
                    }

                    isCommand("location") {
                        sendLocation(56.843139, 60.596065) {
                            inlineKeyboard {
                                row("Test", " ")
                            }
                        }
                    }

                    isCommand("editMessageLiveLocation") {
                        var lat = 56.843139
                        var heading = 0
                        val message = sendLocation(lat, 60.596065) {
                            this.heading = heading
                            this.livePeriod = 1.minutes
                            inlineKeyboard {
                                row("Test", " ")
                            }
                        }

                        repeat(25) {
                            lat += 0.001
                            heading += 5
                            editMessageLiveLocation(lat, 60.596065, messageId = message.messageId) {
                                this.heading = heading
                                inlineKeyboard {
                                    row("Test", CallbackQuery.EMPTY_CALLBACK_DATA)
                                }
                            }
                            Thread.sleep(5000)
                        }
                    }

                    isCommand("venue") {
                        sendVenue(-33.8670522,151.1957362, "Test", "ул. Пушкина, д. Колотушкина") {
                            googlePlaceId = "ChIJrTLr-GyuEmsRBfy61i59si0"
                            googlePlaceType = "restaurant"
                        }
                    }

                    isCommand("contact") {
                        sendContact("=79827304599", "Ray") {
                            lastName = "Smith"
                            inlineKeyboard {
                                row("Test", " ")
                            }
                        }
                    }

                    isCommand("poll") {
                        sendPoll("Question", listOf("1", "2", "3")) {
                            type = PollType.QUIZ
                            correctOptionId = 0
                            explanationWithEntities {
                                bold("Expl: ").text("Wrong answer")
                            }
                            openPeriod = 5
                        }
                    }

                    isCommand("pollForAnswer") {
                        sendPoll("Question", listOf("1", "2", "3")) {
                            type = PollType.REGULAR
                            isAnonymous = false
                        }
                    }

                    isCommand("sendstiker") {
                        sendSticker {
                            sticker = "BQACAgIAAxUHZRldlNqmhN8w1gHFbVBmGQupp7MAAs8yAAJubchIuSn03p5b2EQwBA".asTgFile()
//                            sticker = "https://www.gstatic.com/webp/gallery/1.webp".asTgFile()
//                            sticker = "files/image1.png".asResources().asTgFile()
                        }
                    }

                    isCommand("uploadStickerFile") {
//                            val file1 = uploadStickerFile(getChatIdOrThrow(), "https://png.pngtree.com/element_our/png/20180928/beautiful-hologram-water-color-frame-png_119551.jpg".asTgFile())
//                            println(file1)
                        val file2 = uploadStickerFile(getChatIdOrThrow(), "files/logo2.png".asResources().asTgFile(), StickerFormat.STATIC)
                        println(file2)
                    }

                    isCommand("createNewStickerSet") { args ->

                        if (args == null || args.count { it == ' ' } != 1) {
                            send("Bad syntax: /createNewStickerSet <name> <title>")
                            return@isCommand
                        }
                        val (name, title) = args.split(" ")
//                        createNewStickerSet(getChatIdOrThrow(), stickerSetName(name), title, StickerFormat.STATIC) {
//                            stickerType = StickerType.REGULAR
//                            sticker("files/logo.png".asResources().asTgFile(), listOf("\uD83D\uDE0E"))
//                        }
                    }

                    isCommand("getStickerSet") {
                        val stickerSet = getStickerSet(stickerSetName("name"))
                        println(stickerSet.toJson())
                    }
//
                    isCommand("addStickerToSet") { args ->
                        val sticker = InputSticker(
                            (args ?: "CAACAgIAAxkBAAJK3GTziBDbwOYDCcleop0DHBMk9CdYAAITIgAChpJJS6Z_IJsc_IGpMAQ").asTgFile(),
                            listOf("\uD83D\uDE08"),
                            keywords = listOf("asd")
                        )
                        addStickerToSet(getChatIdOrThrow(), stickerSetName("name"), sticker)
                    }

                    isCommand("setStickerSetThumbnail") {
                        setStickerSetThumbnail(
                            stickerSetName("name"), getChatIdOrThrow(),
                            InputFile.FileIdOrUrl("https://i.ibb.co/ZS7TT09/image.png")
                        )
                    }

                    isCommand("getForumTopicIconStickers") {
                        println(getForumTopicIconStickers())
                    }

                    isCommand("createForumTopic") { args ->
                        if (args == null || args.split(" ").size > 4) {
                            send("Bad syntax: /createForumTopic <chatId> <name> [<iconColor>] [<iconCustomEmojiId>]")
                            return@isCommand
                        }

                        val paths = args.split(" ")
                        createForumTopic(
                            paths[1],
                            paths.getOrNull(2)?.toInt()?.let { v -> IconColor.entries.first { it.value == v } },
                            paths.getOrNull(3),
                            paths[0].toLong().toChatId()
                        )
                        send("Success")
                    }

                    isCommand("reply") {
                        send {
                            text = "text"
                            replyKeyboard {
                                row("Btn")
                            }
                        }
                    }

                    isCommand("reply_persistent") {
                        send {
                            text = "text"
                            replyKeyboard {
                                isPersistent = true
                                row("Btn")
                            }
                        }
                    }

                    isCommand("reply_remove") {
                        send {
                            text = "removed"
                            removeKeyboard {  }
                        }
                    }

                    isCommand("sendMediaGroup") {
                        sendMediaGroup {
                            sendAction = true
                            video("files/video1.mp4".asResources().asTgFile()) {
                                bold("Title").n()
                                n()
                                text("Body")
                            }
                            photo("AgACAgIAAxkDAAIIm2HCc8-GBzuHeX2wSbK25Pk_RK5bAAJLtjEbhJARSkZuPsUIDkxZAQADAgADcwADIwQ".asTgFile(), hasSpoiler = true)
//                            document("files/image2.jpg".asResources().asTgFile(), "files/image2.jpg".asResources().asTgFile())
//                            audio("audio2.mp3".asResources().asTgFile(), "files/size.small.jpg".asResources().asTgFile())
                        }
                    }

                    isCommand("request_user") {
                        send {
                            text = "text"
                            replyKeyboard {
                                row {
                                    button {
                                        text = "user"
                                        requestUser = KeyboardButtonRequestUser(1)
                                    }
                                }
                            }
                        }
                    }

                    isCommand("request_chat") {
                        send {
                            text = "text"
                            replyKeyboard {
                                row {
                                    button {
                                        text = "chat"
                                        requestChat = KeyboardButtonRequestChat(1)
                                    }
                                }
                            }
                        }
                    }

                    isCommand("request_poll") {
                        send {
                            text = "text"
                            replyKeyboard {
                                row {
                                    button {
                                        text = "poll"
                                        requestPoll = KeyboardButtonPollType(PollType.REGULAR)
                                    }
                                }
                            }
                        }
                    }

                    isCommand("newApi") {
                        println(getChatIdOrThrow())
//                        newApi.sendPhoto(
//                            getChatIdOrThrow(),
//                            photo = "files/image1.png".asResources().asTgFile()
//                        )
//                        newApi.sendPhoto(
//                            getChatIdOrThrow(),
//                            photo = "files/image1.png".asResources().readBytes().asTgFile("image1.png", "image/png")
//                        )
//                        newApi.sendPhoto(
//                            getChatIdOrThrow(),
//                            photo = "AgACAgIAAxkDAAIInGHCc89QKcGelysXyncJDzAZWaKNAAJMtjEbhJARSv14GxGJpnGuAQADAgADcwADIwQ".asTgFile()
//                        )
//                        newApi.sendPhoto(
//                            getChatIdOrThrow(),
//                            photo = "https://i.ibb.co/ZS7TT09/image.png".asTgFile()
//                        )
                    }
                }

                handleCallbackQuery(alwaysAnswer = true) {
                    isDataEqual("media_group_file", "") {
                        sendMediaGroup {
                            val caption = generateSequence("") { "1234567890".random().toString() }

                            photo("files/image1.png".asResources().asTgFile()) {
                                bold("Test").n()
                                italic("Message")
                            }
                            photo("files/image1.png".asResources().asTgFile(), caption.take(1024).joinToString(""))
                            photo("files/image1.png".asResources().asTgFile(), caption.take(2000).joinToString(""))
                            photo("files/image2.jpg".asResources().asTgFile(), "<b>${caption.take(1024).joinToString("")}</b>", parseMode = ParseMode.HTML)
//                                photo("files/image2.jpg".asResources().asTgFile(), "<b>${caption.take(2000).joinToString("")}</b>", parseMode = ParseMode.HTML) // error
                            photo("files/image2.jpg".asResources().asTgFile()) {
                                text(caption.take(2000).joinToString(""))
                            }
                        }
                    }

                    isDataEqual("media_group_id") {
                        sendMediaGroup {
                            photo("AgACAgIAAxkDAAIInGHCc89QKcGelysXyncJDzAZWaKNAAJMtjEbhJARSv14GxGJpnGuAQADAgADcwADIwQ".asTgFile()) {

                                text("test")
                            }
                            photo("AgACAgIAAxkDAAIIm2HCc8-GBzuHeX2wSbK25Pk_RK5bAAJLtjEbhJARSkZuPsUIDkxZAQADAgADcwADIwQ".asTgFile())
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
            }

            delay(Long.MAX_VALUE)
        }
    }
}

fun generateString(length: Int): String {
    val symbols = listOf('a'..'z', 'A'..'Z', '0'..'9').flatten().toMutableList().apply {
        add(' ')
    }
    return generateSequence("") { symbols.random().toString() }.take(length).joinToString("")
}

suspend fun EventHandler.sendAnswerVariants(action: MessageAction) = message(action) {
    text = "Choose variant"
    inlineKeyboard {
        row("No answer", "no_answer")
        row("answer", "answer")
    }
}

suspend fun EventHandler.sendPagination(page: Long, action: MessageAction = MessageAction.EDIT) = message(action) {
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

suspend fun EventHandler.sendPagination2(page: Long, prefixCallback: String, action: MessageAction = MessageAction.EDIT) = message(action) {
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
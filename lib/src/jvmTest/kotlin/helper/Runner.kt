package helper

import io.ktor.client.plugins.logging.*
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
import ru.raysmith.tgbot.model.bot.message.LivePeriod
import ru.raysmith.tgbot.model.bot.message.MessageText
import ru.raysmith.tgbot.model.bot.message.MessageTextType
import ru.raysmith.tgbot.model.bot.message.keyboard.buildInlineKeyboard
import ru.raysmith.tgbot.model.bot.message.keyboard.buildReplyKeyboard
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
import ru.raysmith.tgbot.model.network.keyboard.KeyboardButtonRequestUsers
import ru.raysmith.tgbot.model.network.media.input.InputFile
import ru.raysmith.tgbot.model.network.media.input.InputMediaPhoto
import ru.raysmith.tgbot.model.network.media.input.asTgFile
import ru.raysmith.tgbot.model.network.menubutton.MenuButtonCommands
import ru.raysmith.tgbot.model.network.menubutton.MenuButtonDefault
import ru.raysmith.tgbot.model.network.menubutton.MenuButtonWebApp
import ru.raysmith.tgbot.model.network.menubutton.WebAppInfo
import ru.raysmith.tgbot.model.network.message.*
import ru.raysmith.tgbot.model.network.message.reaction.ReactionTypeEmoji
import ru.raysmith.tgbot.model.network.payment.LabeledPrice
import ru.raysmith.tgbot.model.network.response.BooleanResponse
import ru.raysmith.tgbot.model.network.response.MessageResponse
import ru.raysmith.tgbot.model.network.sticker.InputSticker
import ru.raysmith.tgbot.model.network.sticker.StickerFormat
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.network.TelegramApi
import ru.raysmith.tgbot.utils.*
import ru.raysmith.tgbot.utils.datepicker.AdditionalRowsPosition
import ru.raysmith.tgbot.utils.datepicker.DatePickerState
import ru.raysmith.tgbot.utils.datepicker.createDatePicker
import ru.raysmith.tgbot.utils.locations.LocationConfig
import ru.raysmith.tgbot.utils.message.MessageAction
import ru.raysmith.tgbot.utils.message.message
import ru.raysmith.tgbot.utils.pagination.Pagination
import ru.raysmith.utils.takeOrCut
import java.io.File
import java.io.IOException
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.*

val locations = false
val prettyPrintJson = Json(TelegramApi.json) {
    prettyPrint = true
    prettyPrintIndent = " "
}

val UnhandledFeature = BotFeature { handler, handled ->
    if (!handled && handler is CallbackQueryHandler) {
        println("unhandled catch in feature")
        handler.handled = true
    }
}

val GlobalFeature = BotFeature { handler, handled ->
    if (handler is CallbackQueryHandler) {
        println("global feature...")
    }
}

val OverridedGlobalFeature = BotFeature { handler, handled ->
    if (handler is CallbackQueryHandler) {
        println("overrided global feature...")
    }
}

inline fun <reified T> T.toJson() = prettyPrintJson.encodeToString(this)

suspend inline fun <reified T> ISender.sendAsJson(value: T) = send {
    textWithEntities {
        pre(value.toJson(), "json")
    }
}

var loc: String = "menu"

var lookPollAnswers = false

val datePicker = createDatePicker("sys") {
    locale { _, _ ->
        Locale.forLanguageTag("ru-RU")
    }
    firstDayOfWeek { _, _ ->
        DayOfWeek.SATURDAY
    }
    messageText { _, data, state ->
        bold("Title").n()
        n()
        text("Data: $data").n()
        text("State: $state").n()
    }
    dates(LocalDate.now(), LocalDate.now().plusYears(3))

    yearsColumns(6)
    yearsRows(4)

    startWithState { _, _ ->
        DatePickerState.DAY
    }

    additionalRowsPosition(AdditionalRowsPosition.TOP)
    additionalRows { _, _, state ->
        if (state in listOf(DatePickerState.DAY, DatePickerState.YEAR)) {
            row("some btn", "some_btn")
            row("should be unhandled", "sbunhandled")
        }
    }
}

fun MessageText.setupTestMessage(message: Message) {
    bold("bold *text").n()
    italic("italic *text").n()
    underline("underline").n()
    strikethrough("strikethrough").n()
    spoiler("spoiler").n()

    bold("bold ")
        .mix("italic bold ", MessageEntityType.ITALIC, MessageEntityType.BOLD)
        .mix("italic bold strikethrough ", MessageEntityType.ITALIC, MessageEntityType.BOLD, MessageEntityType.STRIKETHROUGH)
        .mix("italic bold strikethrough spoiler ", MessageEntityType.ITALIC, MessageEntityType.BOLD, MessageEntityType.STRIKETHROUGH, MessageEntityType.SPOILER)
        .mix("underline italic bold", MessageEntityType.UNDERLINE, MessageEntityType.ITALIC, MessageEntityType.BOLD)
        .bold("bold").n()

    textLink("inline URL", "https://www.example.com/").n()
    mention("@raysmith").n()
    textMention("inline mention of a user", message.from!!).n()
    emoji("\uD83E\uDD26\uD83C\uDFFC\u200D♂\uFE0F", "1").n()
    code("inline fixed-width code").n()
    pre("pre-formatted fixed-width code block").n()
    pre("pre-formatted fixed-width code block written in the Kotlin programming language", "kotlin").n()

    blockquote("""
        Block quotation started
        Block quotation continued
        Block quotation continued
        Block quotation continued
        The last line of the block quotation
    """.trimIndent()).n()

    expandableBlockquote("""
        The expandable block quotation started right after the previous block quotation
        It is separated from the previous block quotation by an empty bold entity
        Expandable block quotation continued
        Hidden by default part of the expandable block quotation started
        Expandable block quotation continued
        The last line of the expandable block quotation with the expandability mark
    """.trimIndent()).n()

    text("end line").n()

    mix("italic_underline", MessageEntityType.ITALIC, MessageEntityType.UNDERLINE).italic("italic").n()
    mix("italic_underline", MessageEntityType.ITALIC, MessageEntityType.UNDERLINE).underline("underline").n()
    mix("italic_underline", MessageEntityType.ITALIC, MessageEntityType.UNDERLINE)
        .mix("italic_underline", MessageEntityType.ITALIC, MessageEntityType.UNDERLINE)


//    text("text").n()
//    underline("Hello").n()
//    bold("Phone: ").phoneNumber("+79876543210").n()
//    bold("Phone: ").phoneNumber("+79876543210").n()
//    italic("Lik: ").textLink("vk", "vk.com").n()
//    text("Mixed: ").strikethrough("qwe")
//        .mix("rty", MessageEntityType.STRIKETHROUGH, MessageEntityType.UNDERLINE)
//        .strikethrough("uio")
//        .mix("fgb", MessageEntityType.BOLD, MessageEntityType.UNDERLINE)
//        .n()
//    text("Mention: ").mention("@raysmithdev").n()
//    text("Text mention: ").textMention("ray", message.from!!).n()
//    spoiler("Spoiler").n()
////    text("emoji: ").emoji("\uD83D\uDC4D")
//    text("custom emoji: ").emoji("\uD83D\uDC4D", "5368324170671202286").n()
//    blockquote("blockquote").n()
//    expandableBlockquote("expandableBlockquote").n()
//
//


//    entity(MessageEntityType.STRIKETHROUGH) {
//        offset = 2
//        length = currentTextLength - 2
//    }
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

            val bot = Bot(token = System.getenv("BOT_TOKEN")/*, lastUpdateId = -2*/)
                .client {
                    install(Logging) {
                        this.level = LogLevel.INFO
                        this.logger = object : Logger {
                            override fun log(message: String) {
                                TelegramApi.logger.debug(message)
                            }
                        }
                    }
                }
                .onError { e -> logger.error(e.message, e) }
                .onUpdate { updates ->
//                    updates.forEach { println(it.callbackQuery?.data ?: it.message?.text) }
                    updates.forEach {
                        println("${it.type}: ${it.toJson()}")
                    }
                }.config {
//                    this.locale = Locale.JAPAN
                    defaultCallbackQueryHandlerFeatures = listOf(GlobalFeature)
//                    paginationFetcherFactory = object : PaginationFetcherFactory {
//                        override fun <T> getFetcher(): PaginationFetcher<T> {
//                            return object : PaginationFetcher<T> {
//                                override fun getCount(data: Iterable<T>): Int {
//                                    return if (data is SizedCollection<T>) data.count().toInt() else data.count()
//                                }
//                                override fun fetchData(data: Iterable<T>, page: Int, offset: Int, count: Int, rows: Int, columns: Int): Iterable<T> {
//                                    return if (data is SizedCollection<T>) data.limit(count, offset.toLong()) else data.toList().subList((columns * rows) * (page - 1), ((columns * rows) * (page - 1) + (columns * rows)).coerceAtMost(data.count()))
//                                }
//                            }
//                        }
//                    }
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
                            send(chatId = update.findChatId()!!) {
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
//                            isCommand("menu") {
//                                toLocation("menu")
//                            }
//                            isCommand("other") {
//                                toLocation("other")
//                            }
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

//                        handleMessage {
//                            send {
//                                text = "test"
//                                inlineKeyboard {
//                                    row("asd", "asd")
//                                }
//                            }
//                        }
                    }
//
                    location("menu") {
                        onEnter {
                            send {
                                text = "in menu"
                                inlineKeyboard {
                                    row("dont do anything", "fv932mdcl")
                                }
                            }
                            println("on enter menu")
                        }

                        handleCommand {
                            isCommand("dp") {
                                send {
                                    datePicker(datePicker)
                                }
                            }

                            isCommand("toother") {
                                foo = "changed"
                                toLocation("other")
                            }
                        }
                        handleMessage {
                            send("handled in ${this@location.name}")
                        }
                        handleCallbackQuery(
                            alwaysAnswer = false,
//                            features = listOf(OverridedGlobalFeature)
                        ) {
                            setupFeatures(OverridedGlobalFeature, UnhandledFeature)

                            isDataEqual("some_btn") {
                                println("handle some_btn")
                            }

                            isDataEqual("some_btn") {
                                println("handle some_btn [second]")
                            }
                        }
                        handleUnknown {
                            send("unknown")
                        }

                    }

                    location("other") {
                        onEnter {
                            send("in other\nfoo:${foo}")
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
                                send("handled in ${this@location.name}")

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
                            val poll = sendPoll {

                                question {
                                    text = ""
                                    textWithEntities {
                                        text("Some ").bold("Question")
                                    }
                                }

                                explanation {
                                    text = ""
                                    textWithEntities {
                                        bold("Expl: ").text("Wrong answer")
                                    }
                                }
                                option { text = "1" }
                                option { text = "2" }
                                option { text = "3" }

                                isAnonymous = false
                                type = PollType.QUIZ
                                correctOptionId = 0
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

                handleChannelPost {
                    if (channelPost.text == "/paid") {
                        sendPaidMedia {
                            caption = "test"
                            starCount = 1
                            photo("files/image1.png".asResources().asTgFile())
                        }
                    }
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
//                        send("Я родился")
                        send(chat.id.value.toString())
                    }
                }

                handlePreCheckoutQuery {
                    answerPreCheckoutQuery(true)
                }

                handleMessageReaction {
                    println(messageReaction)
                }

                handleMessageReactionCount {
                    println(messageReactionCount)
                }

                handleMessage {
                    messageUsersShared()
                        .onResult {
                            println(it)
                        } ?:

                    messageContact()
                        .verify { (it.userId?.value ?: 0) > 0 }
                        .convert { it.firstName + " " + it.lastName }
                        .onResult {
                            send(it)
                        } ?:
                    
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
                            send(chatId = 1.asChatId()) {
                                text = "this is not sent"
                            }
                        } else if (message.text == "text") {
                            send {
                                parseMode = ParseMode.HTML
                                text = "<b>bold</b>"
                            }
                        } else {
                            send {
                                linkPreviewOptions = LinkPreviewOptions.DISABLED
                                messageEffectId = Message.Effect.POO
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
                                    row("forceReply", "forceReply")
                                }
                            }
                        }
                    }
                }

                handleCommand {
                    if (command.mention != null && command.mention != bot.me.username) return@handleCommand

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

                    isCommand("userShared") {
                        send {
                            text = "Send users"
                            replyKeyboard {
                                row {
                                    button {
                                        text = "Select users"
                                        requestUser = KeyboardButtonRequestUsers(1, maxQuantity = 5)
                                    }
                                }
                            }
                        }
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
                        send(chatId = ChatId.of("@dskfijfwndnslllaa1")) {
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
//                        setChatMenuButton(MenuButtonWebApp("Test", WebAppInfo("https://192.168.0.104:8890")))
                        setChatMenuButton(MenuButtonWebApp("Test", WebAppInfo("https://expodelivery.ru")))
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
                        val poll = sendPoll {
                            type = PollType.QUIZ
                            correctOptionId = 0
                            openPeriod = 5

                            question {
                                text = ""
                                textWithEntities {
                                    text("Some ").bold("Question")
                                }
                            }

                            explanation {
                                text = ""
                                textWithEntities {
                                    bold("Expl: ").text("Wrong answer")
                                }
                            }

                            option { text = "1" }
                            option { text = "2" }
                            option { text = "3" }

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
                        editMedia(InputMediaPhoto(
                            media = "AgACAgIAAxkBAAI9AWOCdwVufweWJV-HNe5pkexZa83GAAKRwzEbKkIQSHYZWoXjyhFNAQADAgADcwADKwQ",
                            caption = editedMessage.caption,
                            parseMode = null,
                            captionEntities = editedMessage.captionEntities
                        ), message.messageId) {
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
//                                            webApp = WebAppInfo("https://sandbox.raysmith.ru:8890/payment")
                                            webApp = WebAppInfo("https://expodelivery.ru")
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
                            keyboardMarkup = buildInlineKeyboard {
                                row("1", "1")
                                row {
                                    button("2", "2")
                                    button("3", "3")
                                }
                            }
                        }
                    }

                    isCommand("pages") {
                        sendPagination(Pagination.PAGE_FIRST, MessageAction.SEND)
                    }

                    isCommand("long") {
                        Thread.sleep(5000)
                        send("success")
                    }

                    isCommand("longtext") {
                        send(generateString(4096 * 3))
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

                        println(string)

                        send {
                            text = string
                            keyboardMarkup = TelegramApi.json.decodeFromString(string)
                        }
                    }
                    isCommand("serialize2") {
                        val string = buildReplyKeyboard {
                            row("title")
                        }.let { TelegramApi.json.encodeToString(it) }

                        println(string)

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
                            showCaptionAboveMedia = true
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

                    isCommand("voice") {
                        sendVoice {
                            voice = "audio2.mp3".asResources().asTgFile()
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

//                            animation = "files/anim1.gif".asResources().asTgFile()
                            animation = "files/video2.webm".asResources().asTgFile() // with thumb

                            thumbnail = "files/size.small.jpg".asResources().asTgFile()
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
//                            this.livePeriod = LivePeriod.Duration(1.minutes)
                            this.livePeriod = LivePeriod.Indefinitely
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
                            }.let {
                                when(it) {
                                    is MessageResponse -> it.result
                                    is BooleanResponse -> it.result
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
                        sendPoll {
                            type = PollType.QUIZ
                            correctOptionId = 0

                            question {
                                textWithEntities {
                                    text("Some ").bold("Question")
                                }
                            }

                            explanation {
                                textWithEntities {
                                    bold("Expl: ").text("Wrong answer")
                                }
                            }

                            option { text = "1" }

                            option {
                                textWithEntities {
                                    text("2")
                                }
                            }

                            option {
                                textWithEntities {
                                    text("3").emoji("\uD83E\uDD26\uD83C\uDFFC\u200D♂\uFE0F", "1")
                                }
                            }

                            openPeriod = 5
                        }
                    }

                    isCommand("pollForAnswer") {
                        sendPoll {
                            question {
                                text = "Question"
                            }

                            option { text = "1" }
                            option { text = "2" }

                            type = PollType.REGULAR
                            isAnonymous = false
                        }
                    }

                    isCommand("sendSticker") {
                        sendSticker {
                            sticker = "CAACAgIAAxUAAWZvRYBa3vXOJahFYNgH5mmGcCd4AALZUAACca54S9ZsAAH860QmBjUE".asTgFile()
//                            sticker = "CAACAgQAAxkDAAJcSmZvOk-vK_uIEi-2CVCO_x0FWkEQAAKYBQAC8NIlUftR5YrfrWgZNQQ".asTgFile()
//                            sticker = "https://www.gstatic.com/webp/gallery/1.webp".asTgFile()
//                            sticker = "files/image1.png".asResources().asTgFile()
                        }.also {
                            println(it)
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

                        val stickers = listOf(
                            InputSticker(
                                "files/logo.png".asResources().asTgFile(),
                                StickerFormat.STATIC,
                                listOf("\uD83D\uDE0E"),
                                keywords = listOf("logo")
                            )
                        )

                        createNewStickerSet(getChatIdOrThrow(), stickerSetName(name), title, stickers)

                        send(stickerSetShareLink(name))
                    }

                    isCommand("getStickerSet") { args ->
                        if (args == null) {
                            send("Bad syntax: /getStickerSet <name>")
                            return@isCommand
                        }

                        val stickerSet = getStickerSet(stickerSetName(args))
                        println(stickerSet.toJson())
//                        send(stickerSet)
                    }
//
                    isCommand("addStickerToSet") { args ->
                        if (args == null || args.count { it == ' ' } > 1) {
                            send("Bad syntax: /getStickerSet <name> [file]")
                            return@isCommand
                        }

                        val spaceIndex = args.lastIndexOf(' ').let { if (it == -1) null else it }
                        val name = args.substring(0, spaceIndex ?: args.length)
                        val file = if (spaceIndex == null) "CAACAgIAAxkBAAJK3GTziBDbwOYDCcleop0DHBMk9CdYAAITIgAChpJJS6Z_IJsc_IGpMAQ" else args.substring(spaceIndex + 1)

                        val sticker = InputSticker(
                            file.asTgFile(),
                            StickerFormat.STATIC,
                            listOf("\uD83D\uDE08"),
                            keywords = listOf("asd")
                        )
                        addStickerToSet(getChatIdOrThrow(), stickerSetName(name), sticker)
                    }

                    isCommand("setStickerSetThumbnail") {
                        setStickerSetThumbnail(
                            stickerSetName("name"), getChatIdOrThrow(), StickerFormat.STATIC,
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

                    isCommand("forceReply") {
                        send {
                            text = "text"
                            forceReplyKeyboard {
                                inputFieldPlaceholder = "placeholder"
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
//                                bold("Title").n()
//                                n()
//                                text("Body")

                                text(generateString(5000))
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
                                        requestUser = KeyboardButtonRequestUsers(1)
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

                    isCommand("setMessageReaction") {
                        setMessageReaction(messageId!!, listOf(
                            ReactionTypeEmoji("🤡")
                        ), isBig = true)

                        unpinChatMessage()
                    }

                    isCommand("getChat") {
                        send {
                            textWithEntities {
                                pre(prettyPrintJson.encodeToString(getChat()), "json")
                            }
                        }
                    }
                }

                handleCallbackQuery(alwaysAnswer = true) {
                    isDataEqual("media_group_file", "") {
                        sendMediaGroup {
                            val caption = generateSequence("") { "1234567890".random().toString() }

                            photo("files/image1.png".asResources().asTgFile(), caption = "<b>Test</b>\n<i>Message</i>", parseMode = ParseMode.HTML)

//                            photo("files/image1.png".asResources().asTgFile()) {
//                                bold("Test").n()
//                                italic("Message")
//                            }
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
                            val caption = buildMarkdownV2String(MessageTextType.CAPTION) {
                                repeat(10) {
                                    bold(generateString(500))
                                }
                            }
                            photo("AgACAgIAAxkDAAIInGHCc89QKcGelysXyncJDzAZWaKNAAJMtjEbhJARSv14GxGJpnGuAQADAgADcwADIwQ".asTgFile(), caption, ParseMode.MARKDOWNV2)
//                            photo("AgACAgIAAxkDAAIInGHCc89QKcGelysXyncJDzAZWaKNAAJMtjEbhJARSv14GxGJpnGuAQADAgADcwADIwQ".asTgFile()) {
//                                text("test")
//                            }
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

                    isDataEqual("forceReply") {
                        send {
                            text = "forceReply"
                            forceReplyKeyboard {
                                inputFieldPlaceholder = "placeholder"
                            }
                        }
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

suspend fun EventHandler.sendPagination(page: Int, action: MessageAction = MessageAction.EDIT) = message(action) {
    text = "Choose item"
    inlineKeyboard {
        pagination(pagesData, "pagination", page, setup = {
            columns = 2
            rows = 5
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
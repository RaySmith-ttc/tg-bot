package ru.raysmith.tgbot.core

import ru.raysmith.tgbot.core.handler.CallbackQueryHandler
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.bot.TextMessage
import ru.raysmith.tgbot.model.network.keyboard.InlineKeyboardButton
import ru.raysmith.tgbot.model.network.message.Message
import ru.raysmith.tgbot.network.TelegramFileService
import ru.raysmith.tgbot.network.TelegramService
import ru.raysmith.tgbot.utils.Pagination

/** Represent an object that can edit messages */
interface IEditor : ChatIdHolder, ApiCaller {

    /** Identifier of the message to be edited */
    var messageId: Int?

    /** Identifier of the inline message to be edited */
    var inlineMessageId: String?

    override val service: TelegramService
    override val fileService: TelegramFileService

    fun edit(
        chatId: ChatId = getChatIdOrThrow(),
        messageId: Int? = this.messageId,
        inlineMessageId: String? = this.inlineMessageId,
        message: TextMessage.() -> Unit
    ): Message {
        return TextMessage(service, fileService).apply(message)
            .edit(chatId, messageId, inlineMessageId)
            .result
    }

    /**
     * Return the callback query with previous page or first page if page not found
     * @param pagePrefix callback query prefix that used for create [pagination][Pagination]
     *
     * @throws IllegalStateException if this isn't a CallbackQueryHandler
     * */
    fun IEditor.getPreviousPageCallback(pagePrefix: String): String {
        if (this !is CallbackQueryHandler) return "${pagePrefix}p1"

        return "${pagePrefix}p${getPreviousPage(pagePrefix)}"
    }

    fun CallbackQueryHandler.getPreviousPageButton(pagePrefix: String? = null): InlineKeyboardButton? {
        var res: InlineKeyboardButton? = null
        if (query.message?.replyMarkup?.keyboard != null) {
            keyboard@ for (row in query.message.replyMarkup.keyboard) {
                row@ for (button in row) {
//                    if (pagePrefix != null && button.callbackData != null && !button.callbackData.startsWith(pagePrefix)) break@row
                    if (pagePrefix != null && row.none { it.callbackData?.startsWith(pagePrefix) == true }) break@row
                    if (button.text.startsWith(Pagination.SYMBOL_CURRENT_PAGE) && button.text.endsWith(Pagination.SYMBOL_CURRENT_PAGE)) {
                        res = button
                        break@keyboard
                    }
                }
            }
        }

        return res
    }

    /**
     * Return the previous page or first page if page not found
     * @param pagePrefix callback query prefix that used for create [pagination][Pagination]
     * */
    fun IEditor.getPreviousPage(pagePrefix: String? = null): Long {
        return if (this is CallbackQueryHandler) getPreviousPage(pagePrefix)
        else Pagination.PAGE_FIRST
    }

    private fun CallbackQueryHandler.getPreviousPage(pagePrefix: String? = null): Long  {
        return getPreviousPageButton(pagePrefix).getPage()
    }

    private fun InlineKeyboardButton?.getPage(): Long =
        this?.text?.filterNot { it == Pagination.SYMBOL_CURRENT_PAGE }?.toLongOrNull() ?: Pagination.PAGE_FIRST
}
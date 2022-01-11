package ru.raysmith.tgbot.core

import ru.raysmith.tgbot.core.handler.CallbackQueryHandler
import ru.raysmith.tgbot.model.bot.TextMessage
import ru.raysmith.tgbot.model.network.keyboard.InlineKeyboardButton
import ru.raysmith.tgbot.model.network.message.Message
import ru.raysmith.tgbot.utils.Pagination
import java.lang.IllegalStateException

interface IEditor : ChatID {
    var messageId: Long?
    var inlineMessageId: String?

    fun edit(
        chatId: String = this.chatId!!,
        messageId: Long? = this.messageId,
        inlineMessageId: String? = this.inlineMessageId,
        message: TextMessage.() -> Unit
    ): Message {
        return (TextMessage().apply(message).edit(chatId, messageId, inlineMessageId)
            .body()?.result ?: throw NullPointerException("Edit message has no response body"))
            .also {
                if (this is CallbackQueryHandler) {
                    isAnswered = true
                }
            }
    }

    /**
     * Return the callback query with previous page or first page if page not found
     * @param pagePrefix callback query prefix that used for create [pagination][Pagination]
     *
     * @throws IllegalStateException if this isn't a CallbackQueryHandler
     * */
    fun IEditor.getPreviousPageCallback(pagePrefix: String): String {
        if (this !is CallbackQueryHandler)
            throw IllegalStateException("Previous page can be obtained from CallbackQueryHandler only")

        return  getPreviousPageButton(pagePrefix)?.callbackData ?: "${pagePrefix}p${Pagination.PAGE_FIRST}"
    }

    private fun CallbackQueryHandler.getPreviousPageButton(pagePrefix: String? = null): InlineKeyboardButton? {
        var res: InlineKeyboardButton? = null
        if (query.message?.replyMarkup?.keyboard != null) {
            keyboard@ for (row in query.message.replyMarkup.keyboard) {
                row@ for (button in row) {
                    if (pagePrefix != null && button.callbackData != null && !button.callbackData.startsWith(pagePrefix)) break@row
                    if (button.text.startsWith("·") && button.text.endsWith("·")) {
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
        this?.text?.replace("·", "")?.toLongOrNull() ?: Pagination.PAGE_FIRST
}
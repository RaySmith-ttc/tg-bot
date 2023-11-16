package ru.raysmith.tgbot.core

import ru.raysmith.tgbot.core.handler.base.CallbackQueryHandler
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.bot.message.MessageText
import ru.raysmith.tgbot.model.bot.message.MessageWithReplyMarkup
import ru.raysmith.tgbot.model.bot.message.TextMessage
import ru.raysmith.tgbot.model.bot.message.keyboard.MessageInlineKeyboard
import ru.raysmith.tgbot.model.bot.message.media.CaptionableMediaMessage
import ru.raysmith.tgbot.model.network.keyboard.InlineKeyboardButton
import ru.raysmith.tgbot.model.network.media.input.InputMedia
import ru.raysmith.tgbot.model.network.message.Message
import ru.raysmith.tgbot.network.TelegramService2
import ru.raysmith.tgbot.utils.Pagination

/** Represent an object that can edit messages */
interface IEditor : ChatIdHolder, TelegramService2 {

    /** Identifier of the message to be edited */
    var messageId: Int?

    /** Identifier of the inline message to be edited */
    var inlineMessageId: String?

    suspend fun editCaption(
        chatId: ChatId = getChatIdOrThrow(),
        messageId: Int? = this.messageId,
        inlineMessageId: String? = this.inlineMessageId,
        caption: MessageText.() -> Unit
    ): Message {
        return CaptionableMediaMessage.instance(this)
            .apply { captionWithEntities(caption) }
            .edit(chatId, messageId, inlineMessageId)
    }

    suspend fun editReplyMarkup(
        chatId: ChatId = getChatIdOrThrow(),
        messageId: Int? = this.messageId,
        inlineMessageId: String? = this.inlineMessageId,
        keyboard: MessageInlineKeyboard.() -> Unit
    ): Message {
        return MessageWithReplyMarkup.instance(this)
            .apply { inlineKeyboard(keyboard) }
            .editReplyMarkup(chatId, messageId, inlineMessageId)
    }

    suspend fun <T : InputMedia> editMedia(
        chatId: ChatId = getChatIdOrThrow(),
        messageId: Int? = this.messageId,
        inlineMessageId: String? = this.inlineMessageId,
        media: InputMedia,
        keyboard: MessageInlineKeyboard.() -> Unit
    ): Message {
        return CaptionableMediaMessage.instance(this)
            .apply { inlineKeyboard(keyboard) }
            .editMedia<T>(chatId, messageId, inlineMessageId, media)
    }

    suspend fun edit(
        chatId: ChatId = getChatIdOrThrow(),
        messageId: Int? = this.messageId,
        inlineMessageId: String? = this.inlineMessageId,
        message: TextMessage.() -> Unit
    ): Message {
        return TextMessage(client).apply { message() }
            .edit(chatId, messageId, inlineMessageId)
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

    private fun CallbackQueryHandler.getPreviousPageButton(pagePrefix: String? = null): InlineKeyboardButton? {
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

    /**
     * Return the previous page or null if page not found
     * @param pagePrefix callback query prefix that used for create [pagination][Pagination]
     * */
    fun IEditor.getPreviousPageOrNull(pagePrefix: String? = null): Long? {
        return if (this is CallbackQueryHandler) getPreviousPageOrNull(pagePrefix)
        else null
    }

    private fun CallbackQueryHandler.getPreviousPage(pagePrefix: String? = null): Long  {
        return getPreviousPageButton(pagePrefix)?.getPageOrNull() ?: Pagination.PAGE_FIRST
    }

    private fun CallbackQueryHandler.getPreviousPageOrNull(pagePrefix: String? = null): Long? {
        return getPreviousPageButton(pagePrefix)?.getPageOrNull()
    }

    private fun InlineKeyboardButton.getPageOrNull(): Long? =
        this.text.filterNot { it == Pagination.SYMBOL_CURRENT_PAGE }.toLongOrNull()
}
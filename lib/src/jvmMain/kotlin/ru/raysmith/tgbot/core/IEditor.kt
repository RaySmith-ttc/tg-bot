package ru.raysmith.tgbot.core

import ru.raysmith.tgbot.core.handler.base.CallbackQueryHandler
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.bot.message.MessageText
import ru.raysmith.tgbot.model.bot.message.MessageWithReplyMarkup
import ru.raysmith.tgbot.model.bot.message.TextMessage
import ru.raysmith.tgbot.model.bot.message.keyboard.MessageInlineKeyboard
import ru.raysmith.tgbot.model.bot.message.media.CaptionableMediaMessage
import ru.raysmith.tgbot.model.bot.message.media.ExtendedCaptionableMediaMessage
import ru.raysmith.tgbot.model.network.keyboard.InlineKeyboardButton
import ru.raysmith.tgbot.model.network.media.input.InputMedia
import ru.raysmith.tgbot.model.network.message.InaccessibleMessage
import ru.raysmith.tgbot.model.network.message.Message
import ru.raysmith.tgbot.network.API
import ru.raysmith.tgbot.utils.pagination.Pagination

/** Represent an object that can edit messages */
interface IEditor : ChatIdHolder, API, BotHolder {

    /** Identifier of the message to be edited */
    var messageId: Int?

    /** Identifier of the inline message to be edited */
    var inlineMessageId: String?

    /** Unique identifier of the business connection on behalf of which the message will be sent */
    var businessConnectionId: String? // TODO bad design: currently propagate to events handlers where can't be obtained

    // TODO messageId not optional ?

    suspend fun editCaption(
        chatId: ChatId = getChatIdOrThrow(),
        messageId: Int? = this.messageId,
        inlineMessageId: String? = this.inlineMessageId,
        businessConnectionId: String? = this.businessConnectionId,
        caption: suspend MessageText.() -> Unit
    ): Message {
        return ExtendedCaptionableMediaMessage.instance(bot)
            .apply {
                captionWithEntities(caption)
                this.businessConnectionId = businessConnectionId
            }
            .edit(chatId, messageId, inlineMessageId)
    }

    suspend fun editReplyMarkup(
        chatId: ChatId = getChatIdOrThrow(),
        messageId: Int? = this.messageId,
        inlineMessageId: String? = this.inlineMessageId,
        businessConnectionId: String? = this.businessConnectionId,
        keyboard: suspend MessageInlineKeyboard.() -> Unit
    ): Message {
        return MessageWithReplyMarkup.instance(bot)
            .apply {
                inlineKeyboard(keyboard)
                this.businessConnectionId = businessConnectionId
            }
            .editReplyMarkup(chatId, messageId, inlineMessageId)
    }

    suspend fun editMedia(
        media: InputMedia,
        messageId: Int? = this.messageId,
        inlineMessageId: String? = this.inlineMessageId,
        businessConnectionId: String? = this.businessConnectionId,
        chatId: ChatId = getChatIdOrThrow(),
        keyboard: suspend MessageInlineKeyboard.() -> Unit
    ): Message {
        return ExtendedCaptionableMediaMessage.instance(bot)
            .apply {
                inlineKeyboard(keyboard)
                this.businessConnectionId = businessConnectionId
            }
            .editMedia(media, chatId, messageId, inlineMessageId)
    }

    suspend fun edit(
        chatId: ChatId = getChatIdOrThrow(),
        messageId: Int? = this.messageId,
        inlineMessageId: String? = this.inlineMessageId,
        businessConnectionId: String? = this.businessConnectionId,
        message: suspend TextMessage.() -> Unit
    ): Message {
        return TextMessage(bot)
            .apply {
                message()
                this.businessConnectionId = businessConnectionId
            }
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
        when(query.message) {
            is InaccessibleMessage, null -> {}
            is Message -> {
                if (query.message.replyMarkup?.keyboard != null) {
                    keyboard@ for (row in query.message.replyMarkup.keyboard) {
                        row@ for (button in row) {
                            if (pagePrefix != null && row.none { it.callbackData?.startsWith(pagePrefix) == true }) break@row
                            if (button.text.startsWith(Pagination.SYMBOL_CURRENT_PAGE) && button.text.endsWith(Pagination.SYMBOL_CURRENT_PAGE)) {
                                res = button
                                break@keyboard
                            }
                        }
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
    fun IEditor.getPreviousPage(pagePrefix: String? = null): Int {
        return if (this is CallbackQueryHandler) getPreviousPage(pagePrefix)
        else Pagination.PAGE_FIRST
    }

    /**
     * Return the previous page or null if page not found
     * @param pagePrefix callback query prefix that used for create [pagination][Pagination]
     * */
    fun IEditor.getPreviousPageOrNull(pagePrefix: String? = null): Int? {
        return if (this is CallbackQueryHandler) getPreviousPageOrNull(pagePrefix)
        else null
    }

    private fun CallbackQueryHandler.getPreviousPage(pagePrefix: String? = null): Int  {
        return getPreviousPageButton(pagePrefix)?.getPageOrNull() ?: Pagination.PAGE_FIRST
    }

    private fun CallbackQueryHandler.getPreviousPageOrNull(pagePrefix: String? = null): Int? {
        return getPreviousPageButton(pagePrefix)?.getPageOrNull()
    }

    private fun InlineKeyboardButton.getPageOrNull(): Int? =
        this.text.filterNot { it == Pagination.SYMBOL_CURRENT_PAGE }.toIntOrNull()
}
package ru.raysmith.tgbot.core

import io.ktor.client.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import ru.raysmith.tgbot.model.Currency
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.bot.message.keyboard.InlineKeyboardCreator
import ru.raysmith.tgbot.model.bot.message.keyboard.MessageKeyboard
import ru.raysmith.tgbot.model.network.keyboard.InlineKeyboardMarkup
import ru.raysmith.tgbot.model.network.payment.LabeledPrice
import ru.raysmith.tgbot.network.TelegramService2

/** Builder of [sendInvoice][BotContext.sendInvoice] request */
class InvoiceSender(override val client: HttpClient) : InlineKeyboardCreator, TelegramService2 {

    var messageThreadId: Int? = null
    var title: String? = null
    var description: String? = null
    var payload: String? = null
    var providerToken: String? = Bot.config.defaultProviderToken
    var currency: Currency? = null
    var prices: List<LabeledPrice>? = null
    var maxTipAmount: Int? = null
    var suggestedTipAmounts: List<Int>? = null
    var startParameter: String? = null
    var providerData: JsonElement? = null
    var photoUrl: String? = null
    var photoSize: Int? = null
    var photoWidth: Int? = null
    var photoHeight: Int? = null
    var needName: Boolean? = null
    var needPhoneNumber: Boolean? = null
    var needEmail: Boolean? = null
    var needShippingAddress: Boolean? = null
    var sendPhoneNumberToProvider: Boolean? = null
    var sendEmailToProvider: Boolean? = null
    var isFlexible: Boolean? = null
    var disableNotification: Boolean? = null
    var replyToMessageId: Long? = null
    var allowSendingWithoutReply: Boolean? = null
    override var keyboardMarkup: MessageKeyboard? = null

    suspend fun send(chatId: ChatId) = sendInvoice(
        chatId = chatId,
        messageThreadId = messageThreadId,
        title = title ?: "",
        description = description ?: "",
        payload = payload ?: "",
        providerToken = providerToken ?: "",
        currency = currency?.code ?: "",
        prices = prices?.let { Json.encodeToString(it) } ?: "",
        maxTipAmount = maxTipAmount,
        suggestedTipAmounts = suggestedTipAmounts?.let { Json.encodeToString(it) },
        startParameter = startParameter,
        providerData = providerData?.let { Json.encodeToString(it) },
        photoUrl = photoUrl,
        photoSize = photoSize,
        photoWidth = photoWidth,
        photoHeight = photoHeight,
        needName = needName,
        needPhoneNumber = needPhoneNumber,
        needEmail = needEmail,
        needShippingAddress = needShippingAddress,
        sendPhoneNumberToProvider = sendPhoneNumberToProvider,
        sendEmailToProvider = sendEmailToProvider,
        isFlexible = isFlexible,
        disableNotification = disableNotification,
        replyToMessageId = replyToMessageId,
        allowSendingWithoutReply = allowSendingWithoutReply,
        replyMarkup = keyboardMarkup?.toMarkup() as InlineKeyboardMarkup?
    )
}

/** Builder of [sendInvoice][BotContext.createInvoiceLink] request */
class InvoiceCreateLinkSender(override val client: HttpClient) : TelegramService2 {

    var title: String? = null
    var description: String? = null
    var payload: String? = null
    var providerToken: String? = Bot.config.defaultProviderToken
    var currency: Currency? = null
    var prices: List<LabeledPrice>? = null
    var maxTipAmount: Int? = null
    var suggestedTipAmounts: List<Int>? = null
    var providerData: JsonElement? = null
    var photoUrl: String? = null
    var photoSize: Int? = null
    var photoWidth: Int? = null
    var photoHeight: Int? = null
    var needName: Boolean? = null
    var needPhoneNumber: Boolean? = null
    var needEmail: Boolean? = null
    var needShippingAddress: Boolean? = null
    var sendPhoneNumberToProvider: Boolean? = null
    var sendEmailToProvider: Boolean? = null
    var isFlexible: Boolean? = null

    suspend fun send() = createInvoiceLink(
        title = title ?: "",
        description = description ?: "",
        payload = payload ?: "",
        providerToken = providerToken ?: "",
        currency = currency?.code ?: "",
        prices = prices?.let { Json.encodeToString(it) } ?: "",
        maxTipAmount = maxTipAmount,
        suggestedTipAmounts = suggestedTipAmounts?.let { Json.encodeToString(it) },
        providerData = providerData?.let { Json.encodeToString(it) },
        photoUrl = photoUrl,
        photoSize = photoSize,
        photoWidth = photoWidth,
        photoHeight = photoHeight,
        needName = needName,
        needPhoneNumber = needPhoneNumber,
        needEmail = needEmail,
        needShippingAddress = needShippingAddress,
        sendPhoneNumberToProvider = sendPhoneNumberToProvider,
        sendEmailToProvider = sendEmailToProvider,
        isFlexible = isFlexible
    )
}
package ru.raysmith.tgbot.core

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import org.slf4j.LoggerFactory
import retrofit2.Response
import ru.raysmith.tgbot.model.Currency
import ru.raysmith.tgbot.model.bot.InlineKeyboardCreator
import ru.raysmith.tgbot.model.bot.MessageKeyboard
import ru.raysmith.tgbot.model.network.keyboard.InlineKeyboardMarkup
import ru.raysmith.tgbot.model.network.message.MessageResponse
import ru.raysmith.tgbot.model.network.payment.LabeledPrice
import ru.raysmith.tgbot.network.TelegramService
import ru.raysmith.utils.properties.PropertiesFactory
import ru.raysmith.utils.properties.getOrNull

class InvoiceSender(override var service: TelegramService) : InlineKeyboardCreator, ApiCaller {

    companion object {
        private val logger = LoggerFactory.getLogger("invoice")
        private val defaultProviderToken = PropertiesFactory.from("bot.properties").getOrNull("providerToken")
    }

    var title: String? = null
    var description: String? = null
    var payload: String? = null
    var providerToken: String? = defaultProviderToken
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

    fun send(chatId: String): Response<MessageResponse> {
        return service.sendInvoice(
            chatId = chatId,
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
        ).execute()
    }

}
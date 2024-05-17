package ru.raysmith.tgbot.core

import io.ktor.client.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import ru.raysmith.tgbot.model.Currency
import ru.raysmith.tgbot.model.network.payment.LabeledPrice
import ru.raysmith.tgbot.network.API

/** Builder of [sendInvoice][API.createInvoiceLink] request */
class CreateInvoiceLinkRequestBuilder(override val bot: Bot) : API, BotHolder {
    override val client: HttpClient = bot.client
    override val botConfig: BotConfig = bot.botConfig

    /** Product name, 1-32 characters */
    var title: String? = null

    /** Product description, 1-255 characters */
    var description: String? = null

    /**
     * Bot-defined invoice payload, 1-128 bytes.
     * This will not be displayed to the user, use for your internal processes.
     * */
    var payload: String? = null

    /** Payments provider token, obtained via [@Botfather](tg://BotFather) */
    var providerToken: String? = bot.botConfig.defaultProviderToken

    /**
     * Three-letter ISO 4217 currency code, see
     * [more on currencies](https://core.telegram.org/bots/payments#supported-currencies)
     * */
    var currency: Currency? = null
    /**
     * Price breakdown, a JSON-serialized list of components (e.g. product price, tax, discount,
     * delivery cost, delivery tax, bonus, etc.)
     * */
    var prices: List<LabeledPrice>? = null

    /**
     * The maximum accepted amount for tips in the *smallest units* of the currency
     * (integer, **not** float/double). For example, for a maximum tip of `US$ 1.45` pass `max_tip_amount = 145`.
     * See the *exp* parameter in [currencies.json](https://core.telegram.org/bots/payments/currencies.json),
     * it shows the number of digits past the decimal point for each currency (2 for the majority of currencies).
     * Defaults to 0
     * */
    var maxTipAmount: Int? = null

    /**
     * A JSON-serialized array of suggested amounts of tips in the *smallest units*
     * of the currency (integer, **not** float/double). At most 4 suggested tip amounts can be specified.
     * The suggested tip amounts must be positive, passed in a strictly increased order and must
     * not exceed *max_tip_amount*.
     * */
    var suggestedTipAmounts: List<Int>? = null

    /**
     * A JSON-serialized data about the invoice, which will be shared with the payment provider.
     * A detailed description of required fields should be provided by the payment provider.
     * */
    var providerData: JsonElement? = null

    /**
     * URL of the product photo for the invoice. Can be a photo of the goods or a marketing
     * image for a service. People like it better when they see what they are paying for.
     * */
    var photoUrl: String? = null

    /** Photo size in bytes */
    var photoSize: Int? = null

    /** Photo width */
    var photoWidth: Int? = null

    /** Photo height */
    var photoHeight: Int? = null

    /** Pass *True*, if you require the user's full name to complete the order */
    var needName: Boolean? = null

    /** Pass *True*, if you require the user's phone number to complete the order */
    var needPhoneNumber: Boolean? = null

    /** Pass *True*, if you require the user's email address to complete the order */
    var needEmail: Boolean? = null

    /** Pass *True*, if you require the user's shipping address to complete the order */
    var needShippingAddress: Boolean? = null

    /** Pass *True*, if user's phone number should be sent to provider */
    var sendPhoneNumberToProvider: Boolean? = null

    /** Pass *True*, if user's email address should be sent to provider */
    var sendEmailToProvider: Boolean? = null

    /** Pass *True*, if the final price depends on the shipping method */
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
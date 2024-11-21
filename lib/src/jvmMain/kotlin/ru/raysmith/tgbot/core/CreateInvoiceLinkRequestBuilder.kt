package ru.raysmith.tgbot.core

import io.ktor.client.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import ru.raysmith.tgbot.model.Currency
import ru.raysmith.tgbot.model.network.payment.LabeledPrice
import ru.raysmith.tgbot.network.API
import kotlin.time.Duration

// TODO move required fields to constructor

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

    /**
     * Payments provider token, obtained via [@Botfather](tg://BotFather).
     * Pass an empty string for payments in Telegram Stars.
     * */
    var providerToken: String? = bot.botConfig.defaultProviderToken

    /**
     * Three-letter ISO 4217 currency code, see
     * [more on currencies](https://core.telegram.org/bots/payments#supported-currencies)
     * */
    var currency: Currency? = null
    /**
     * Price breakdown, a list of components (e.g. product price, tax, discount,
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
     * An array of suggested amounts of tips in the *smallest units*
     * of the currency (integer, **not** float/double). At most 4 suggested tip amounts can be specified.
     * The suggested tip amounts must be positive, passed in a strictly increased order and must
     * not exceed *max_tip_amount*.
     * */
    var suggestedTipAmounts: List<Int>? = null

    private var _providerData: String? = null

    /**
     * A data about the invoice, which will be shared with the payment provider.
     * A detailed description of required fields should be provided by the payment provider.
     *
     * *This is an alias with the [JsonElement] type for [providerData] field*
     * */
    var providerDataJson: JsonElement? = null
        set(value) {
            _providerData = value?.let { Json.encodeToString(it) }
            field = value
        }

    /**
     * A JSON-serialized data about the invoice, which will be shared with the payment provider.
     * A detailed description of required fields should be provided by the payment provider.
     * */
    var providerData: String? = null
        set(value) {
            _providerData = value?.let { Json.encodeToString(it) }
            field = value
        }

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

    /**
     * Unique identifier of the business connection on behalf of which the link will be created.
     * For payments in [Telegram Stars](https://t.me/BotNews/90) only.
     * */
    var businessConnectionId: String? = null

    /**
     * The number of seconds the subscription will be active for before the next payment.
     * The currency must be set to [Currency.XTR] (Telegram Stars) if the parameter is used. Currently,
     * it must always be 2592000 (30 days) if specified. Any number of subscriptions can be active for a given bot at
     * the same time, including multiple concurrent subscriptions from the same user.
     * */
    var subscriptionPeriod: Duration? = null

    suspend fun send() = createInvoiceLink(
        businessConnectionId = businessConnectionId,
        title = title ?: "",
        description = description ?: "",
        payload = payload ?: "",
        providerToken = providerToken,
        currency = currency?.code ?: "",
        prices = prices,
        subscriptionPeriod = subscriptionPeriod?.inWholeSeconds?.toInt(),
        maxTipAmount = maxTipAmount,
        suggestedTipAmounts = suggestedTipAmounts,
        providerData = _providerData,
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
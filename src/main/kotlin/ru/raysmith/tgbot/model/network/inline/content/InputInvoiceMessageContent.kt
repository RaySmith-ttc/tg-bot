package ru.raysmith.tgbot.model.network.inline.content

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import ru.raysmith.tgbot.model.network.payment.LabeledPrice

/** Represents the [content][InputMessageContent] of an invoice message to be sent as the result of an inline query. */
@Serializable
data class InputInvoiceMessageContent(

    /** Product name, 1-32 characters */
    @SerialName("title") val title: String,

    /** Product description, 1-255 characters */
    @SerialName("description") val description: String,

    /**
     * Bot-defined invoice payload, 1-128 bytes.
     * This will not be displayed to the user, use for your internal processes.
     * */
    @SerialName("payload") val payload: String,

    /** Payment provider token, obtained via [@BotFather](https://t.me/botfather) */
    @SerialName("provider_token") val providerToken: String,

    /**
     * Three-letter ISO 4217 currency code,
     * [see more on currencies](https://core.telegram.org/bots/payments#supported-currencies)
     * */
    @SerialName("currency") val currency: String,

    /**
     * Price breakdown, list of components (e.g. product price, tax, discount, delivery cost, delivery tax, bonus, etc.)
     * */
    @SerialName("prices") val prices: List<LabeledPrice>,

    /**
     * Total price in the *smallest units* of the currency (integer, **not** float/double). For example,
     * for a price of `US$ 1.45` pass `amount = 145`. See the exp parameter in
     * [currencies.json](https://core.telegram.org/bots/payments/currencies.json),
     * it shows the number of digits past the decimal point for each currency (2 for the majority of currencies).
     * */
    @SerialName("max_tip_amount") val maxTipAmount: Int? = null,

    /**
     * List of suggested amounts of tip in the *smallest units* of the currency (integer, **not** float/double).
     * At most 4 suggested tip amounts can be specified. The suggested tip amounts must be positive,
     * passed in a strictly increased order and must not exceed [maxTipAmount].
     * */
    @SerialName("suggested_tip_amounts") val suggestedTipAmounts: List<Int>? = null,

    /**
     * A JSON object for data about the invoice, which will be shared with the payment provider.
     * A detailed description of the required fields should be provided by the payment provider.
     * */
    @SerialName("provider_data") val providerData: JsonElement? = null,

    /** URL of the product photo for the invoice. Can be a photo of the goods or a marketing image for a service. */
    @SerialName("photo_url") val photoUrl: String? = null,

    /** Photo size in bytes */
    @SerialName("photo_size") val photoSize: Int? = null,

    /** Photo width */
    @SerialName("photo_width") val photoWidth: Int? = null,

    /** Photo height */
    @SerialName("photo_height") val photoHeight: Int? = null,

    /** Pass *True* if you require the user's full name to complete the order */
    @SerialName("need_name") val needName: Boolean? = null,

    /** Pass *True* if you require the user's phone number to complete the order */
    @SerialName("need_phone_number") val needPhoneNumber: Boolean? = null,

    /** Pass *True* if you require the user's email address to complete the order */
    @SerialName("need_email") val needEmail: Boolean? = null,

    /** Pass *True* if you require the user's shipping address to complete the order */
    @SerialName("need_shipping_address") val needShippingAddress: Boolean? = null,

    /** Pass *True* if the user's phone number should be sent to provider */
    @SerialName("send_phone_number_to_provider") val sendPhoneNumberToProvider: Boolean? = null,

    /** Pass *True* if the user's email address should be sent to provider */
    @SerialName("send_email_to_provider") val sendEmailToProvider: Boolean? = null,

    /** Pass *True* if the final price depends on the shipping method */
    @SerialName("is_flexible") val isFlexible: Boolean? = null,
) : InputMessageContent()
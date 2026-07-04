package ru.raysmith.tgbot.model.api.message.rich.text

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** A text with a bank card number. */
@Serializable
data class RichTextBankCardNumber(

    /** The text */
    @SerialName("text") val text: RichText,

    /** The bank card number */
    @SerialName("bank_card_number") val bankCardNumber: String,
) : RichText {

    /** Type of the rich text, always “bank_card_number” */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "bank_card_number"
}

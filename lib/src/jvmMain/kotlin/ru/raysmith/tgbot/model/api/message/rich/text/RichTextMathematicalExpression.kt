package ru.raysmith.tgbot.model.api.message.rich.text

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** A mathematical expression. */
@Serializable
data class RichTextMathematicalExpression(

    /** The expression in LaTeX format */
    @SerialName("expression") val expression: String,
) : RichText {

    /** Type of the rich text, always “mathematical_expression” */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "mathematical_expression"
}

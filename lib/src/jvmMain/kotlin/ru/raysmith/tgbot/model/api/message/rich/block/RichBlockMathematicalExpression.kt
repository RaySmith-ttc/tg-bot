package ru.raysmith.tgbot.model.api.message.rich.block

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** A block with a mathematical expression in LaTeX format, corresponding to the custom HTML tag <tg-math-block>. */
@Serializable
data class RichBlockMathematicalExpression(

    /** The mathematical expression in LaTeX format */
    @SerialName("expression") val expression: String,
) : RichBlock {

    /** Type of the block, always “mathematical_expression” */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "mathematical_expression"
}

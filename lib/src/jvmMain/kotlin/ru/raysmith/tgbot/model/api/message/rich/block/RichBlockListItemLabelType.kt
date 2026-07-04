package ru.raysmith.tgbot.model.api.message.rich.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The type of the item label for ordered list
 *
 * - [LOWERCASE_LETTERS] - Lowercase letters
 * - [UPPERCASE_LETTERS] - Uppercase letters
 * - [LOWERCASE_NUMERALS] - Lowercase Roman numerals
 * - [UPPERCASE_NUMERALS] - Uppercase Roman numerals
 * - [DECIMAL_NUMBERS] - Decimal numbers
 * */
@Serializable
enum class RichBlockListItemLabelType {

    /** Lowercase letters */
    @SerialName("a") LOWERCASE_LETTERS,

    /** Uppercase letters */
    @SerialName("A") UPPERCASE_LETTERS,

    /** Lowercase Roman numerals */
    @SerialName("i") LOWERCASE_NUMERALS,

    /** Uppercase Roman numerals */
    @SerialName("I") UPPERCASE_NUMERALS,

    /** Decimal numbers */
    @SerialName("1") DECIMAL_NUMBERS,
}

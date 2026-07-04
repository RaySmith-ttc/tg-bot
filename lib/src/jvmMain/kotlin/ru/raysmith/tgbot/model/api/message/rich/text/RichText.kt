package ru.raysmith.tgbot.model.api.message.rich.text

import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.serializer.RichTextSerializer

/**
 * This object represents a rich formatted text. Currently, it can be either a String for plain text, an Array of
 * [RichText], or any of the following types:
 *
 * - [RichTextBold]
 * - [RichTextItalic]
 * - [RichTextUnderline]
 * - [RichTextStrikethrough]
 * - [RichTextSpoiler]
 * - [RichTextDateTime]
 * - [RichTextTextMention]
 * - [RichTextSubscript]
 * - [RichTextSuperscript]
 * - [RichTextMarked]
 * - [RichTextCode]
 * - [RichTextCustomEmoji]
 * - [RichTextMathematicalExpression]
 * - [RichTextUrl]
 * - [RichTextEmailAddress]
 * - [RichTextPhoneNumber]
 * - [RichTextBankCardNumber]
 * - [RichTextMention]
 * - [RichTextHashtag]
 * - [RichTextCashtag]
 * - [RichTextBotCommand]
 * - [RichTextAnchor]
 * - [RichTextAnchorLink]
 * - [RichTextReference]
 * - [RichTextReferenceLink]
 * */
@Serializable(RichTextSerializer::class)
sealed interface RichText {

    /** Type of the rich text */
    val type: String
}

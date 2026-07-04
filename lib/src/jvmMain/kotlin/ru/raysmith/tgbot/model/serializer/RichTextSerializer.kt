package ru.raysmith.tgbot.model.serializer

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive
import ru.raysmith.tgbot.exceptions.NewApiException
import ru.raysmith.tgbot.model.api.message.rich.text.*

/**
 * Deserializes a [RichText] value which, depending on the JSON representation, can be:
 * - a plain JSON string -> [RichTextPlain]
 * - a JSON array of [RichText] -> [RichTextList]
 * - a JSON object with a 'type' discriminator -> the corresponding [RichText] implementation
 * */
internal object RichTextSerializer : JsonContentPolymorphicSerializer<RichText>(RichText::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<RichText> = when (element) {
        is JsonPrimitive -> RichTextPlain.serializer()
        is JsonArray -> RichTextList.serializer()
        is JsonObject -> selectDeserializerByType(element)
    }

    private fun selectDeserializerByType(element: JsonObject): KSerializer<out RichText> {
        val typeObject = element["type"]
            ?: error("The RichText json object must contains 'type' to deserialize")
        require(typeObject is JsonPrimitive) { "The RichText json field 'type' must be primitive to deserialize" }
        require(typeObject.isString) { "The RichText json field 'type' must be string to deserialize" }

        return when (val type = typeObject.jsonPrimitive.content) {
            "bold" -> RichTextBold.serializer()
            "italic" -> RichTextItalic.serializer()
            "underline" -> RichTextUnderline.serializer()
            "strikethrough" -> RichTextStrikethrough.serializer()
            "spoiler" -> RichTextSpoiler.serializer()
            "date_time" -> RichTextDateTime.serializer()
            "text_mention" -> RichTextTextMention.serializer()
            "subscript" -> RichTextSubscript.serializer()
            "superscript" -> RichTextSuperscript.serializer()
            "marked" -> RichTextMarked.serializer()
            "code" -> RichTextCode.serializer()
            "custom_emoji" -> RichTextCustomEmoji.serializer()
            "mathematical_expression" -> RichTextMathematicalExpression.serializer()
            "url" -> RichTextUrl.serializer()
            "email_address" -> RichTextEmailAddress.serializer()
            "phone_number" -> RichTextPhoneNumber.serializer()
            "bank_card_number" -> RichTextBankCardNumber.serializer()
            "mention" -> RichTextMention.serializer()
            "hashtag" -> RichTextHashtag.serializer()
            "cashtag" -> RichTextCashtag.serializer()
            "bot_command" -> RichTextBotCommand.serializer()
            "anchor" -> RichTextAnchor.serializer()
            "anchor_link" -> RichTextAnchorLink.serializer()
            "reference" -> RichTextReference.serializer()
            "reference_link" -> RichTextReferenceLink.serializer()
            else -> throw NewApiException("Unknown RichText type: '$type'")
        }
    }
}

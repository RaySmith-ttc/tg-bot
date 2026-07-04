package ru.raysmith.tgbot.model.serializer

import ru.raysmith.tgbot.model.api.message.rich.block.RichBlock
import ru.raysmith.tgbot.model.api.message.rich.block.RichBlockAnchor
import ru.raysmith.tgbot.model.api.message.rich.block.RichBlockAnimation
import ru.raysmith.tgbot.model.api.message.rich.block.RichBlockAudio
import ru.raysmith.tgbot.model.api.message.rich.block.RichBlockBlockQuotation
import ru.raysmith.tgbot.model.api.message.rich.block.RichBlockCollage
import ru.raysmith.tgbot.model.api.message.rich.block.RichBlockDetails
import ru.raysmith.tgbot.model.api.message.rich.block.RichBlockDivider
import ru.raysmith.tgbot.model.api.message.rich.block.RichBlockFooter
import ru.raysmith.tgbot.model.api.message.rich.block.RichBlockList
import ru.raysmith.tgbot.model.api.message.rich.block.RichBlockMap
import ru.raysmith.tgbot.model.api.message.rich.block.RichBlockMathematicalExpression
import ru.raysmith.tgbot.model.api.message.rich.block.RichBlockParagraph
import ru.raysmith.tgbot.model.api.message.rich.block.RichBlockPhoto
import ru.raysmith.tgbot.model.api.message.rich.block.RichBlockPreformatted
import ru.raysmith.tgbot.model.api.message.rich.block.RichBlockPullQuotation
import ru.raysmith.tgbot.model.api.message.rich.block.RichBlockSectionHeading
import ru.raysmith.tgbot.model.api.message.rich.block.RichBlockSlideshow
import ru.raysmith.tgbot.model.api.message.rich.block.RichBlockTable
import ru.raysmith.tgbot.model.api.message.rich.block.RichBlockThinking
import ru.raysmith.tgbot.model.api.message.rich.block.RichBlockVideo
import ru.raysmith.tgbot.model.api.message.rich.block.RichBlockVoiceNote
import ru.raysmith.tgbot.model.api.message.rich.text.RichTextAnchor
import ru.raysmith.tgbot.model.api.message.rich.text.RichTextAnchorLink
import ru.raysmith.tgbot.model.api.message.rich.text.RichTextBankCardNumber
import ru.raysmith.tgbot.model.api.message.rich.text.RichTextBold
import ru.raysmith.tgbot.model.api.message.rich.text.RichTextBotCommand
import ru.raysmith.tgbot.model.api.message.rich.text.RichTextCashtag
import ru.raysmith.tgbot.model.api.message.rich.text.RichTextCode
import ru.raysmith.tgbot.model.api.message.rich.text.RichTextCustomEmoji
import ru.raysmith.tgbot.model.api.message.rich.text.RichTextDateTime
import ru.raysmith.tgbot.model.api.message.rich.text.RichTextEmailAddress
import ru.raysmith.tgbot.model.api.message.rich.text.RichTextHashtag
import ru.raysmith.tgbot.model.api.message.rich.text.RichTextItalic
import ru.raysmith.tgbot.model.api.message.rich.text.RichTextMarked
import ru.raysmith.tgbot.model.api.message.rich.text.RichTextMathematicalExpression
import ru.raysmith.tgbot.model.api.message.rich.text.RichTextMention
import ru.raysmith.tgbot.model.api.message.rich.text.RichTextPhoneNumber
import ru.raysmith.tgbot.model.api.message.rich.text.RichTextReference
import ru.raysmith.tgbot.model.api.message.rich.text.RichTextReferenceLink
import ru.raysmith.tgbot.model.api.message.rich.text.RichTextSpoiler
import ru.raysmith.tgbot.model.api.message.rich.text.RichTextStrikethrough
import ru.raysmith.tgbot.model.api.message.rich.text.RichTextSubscript
import ru.raysmith.tgbot.model.api.message.rich.text.RichTextSuperscript
import ru.raysmith.tgbot.model.api.message.rich.text.RichTextTextMention
import ru.raysmith.tgbot.model.api.message.rich.text.RichTextUnderline
import ru.raysmith.tgbot.model.api.message.rich.text.RichTextUrl

internal object RichBlockSerializer : BaseJsonContentPolymorphicSerializer<RichBlock>(RichBlock::class) {
    override fun selectDeserializer(type: String) = when(type) {
        "paragraph" -> RichBlockParagraph.serializer()
        "heading" -> RichBlockSectionHeading.serializer()
        "pre" -> RichBlockPreformatted.serializer()
        "footer" -> RichBlockFooter.serializer()
        "divider" -> RichBlockDivider.serializer()
        "mathematical_expression" -> RichBlockMathematicalExpression.serializer()
        "anchor" -> RichBlockAnchor.serializer()
        "list" -> RichBlockList.serializer()
        "blockquote" -> RichBlockBlockQuotation.serializer()
        "pullquote" -> RichBlockPullQuotation.serializer()
        "collage" -> RichBlockCollage.serializer()
        "slideshow" -> RichBlockSlideshow.serializer()
        "table" -> RichBlockTable.serializer()
        "details" -> RichBlockDetails.serializer()
        "map" -> RichBlockMap.serializer()
        "animation" -> RichBlockAnimation.serializer()
        "audio" -> RichBlockAudio.serializer()
        "photo" -> RichBlockPhoto.serializer()
        "video" -> RichBlockVideo.serializer()
        "voice_note" -> RichBlockVoiceNote.serializer()
        "thinking" -> RichBlockThinking.serializer()
        else -> null
    }
}

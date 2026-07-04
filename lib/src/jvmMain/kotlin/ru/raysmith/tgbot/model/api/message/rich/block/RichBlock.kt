package ru.raysmith.tgbot.model.api.message.rich.block

import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.serializer.RichBlockSerializer

/**
 * This object represents a block in a rich formatted message. Currently, it can be any of the following types:
 *
 * - [RichBlockParagraph]
 * - [RichBlockSectionHeading]
 * - [RichBlockPreformatted]
 * - [RichBlockFooter]
 * - [RichBlockDivider]
 * - [RichBlockMathematicalExpression]
 * - [RichBlockAnchor]
 * - [RichBlockList]
 * - [RichBlockBlockQuotation]
 * - [RichBlockPullQuotation]
 * - [RichBlockCollage]
 * - [RichBlockSlideshow]
 * - [RichBlockTable]
 * - [RichBlockDetails]
 * - [RichBlockMap]
 * - [RichBlockAnimation]
 * - [RichBlockAudio]
 * - [RichBlockPhoto]
 * - [RichBlockVideo]
 * - [RichBlockVoiceNote]
 * - [RichBlockThinking]
 * */
@Serializable(RichBlockSerializer::class)
sealed interface RichBlock {

    /** Type of the block */
    val type: String
}

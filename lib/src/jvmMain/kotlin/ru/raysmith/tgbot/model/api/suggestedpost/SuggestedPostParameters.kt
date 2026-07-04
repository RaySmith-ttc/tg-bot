package ru.raysmith.tgbot.model.api.suggestedpost

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.serializer.ZonedDateTimeAsUnixSerializer
import java.time.ZonedDateTime
import kotlin.time.Duration

/**
 * Contains parameters of a post that is being suggested by the bot.
 *
 * @property price Proposed price for the post. If the field is omitted, then the post is unpaid.
 * @property sendDate Proposed send date of the post. If specified, then the date must be between 300 second and 2678400
 *  seconds (30 days) in the future. If the field is omitted, then the post can be published at any time within 30 days
 *  at the sole discretion of the user who approves it.
 * */
@Serializable
data class SuggestedPostParameters(
    @SerialName("price") val price: SuggestedPostPrice? = null,
    @SerialName("send_date") @Serializable(ZonedDateTimeAsUnixSerializer::class) val sendDate: ZonedDateTime? = null,
)

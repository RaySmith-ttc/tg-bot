package ru.raysmith.tgbot.model.network.payment.stars

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.User

/** Describes the affiliate program that issued the affiliate commission received via this transaction. */
@Serializable
data class TransactionPartnerAffiliateProgram(

    /** Information about the bot that sponsored the affiliate program */
    @SerialName("sponsor_user")
    val sponsorUser: User? = null,

    /**
     * The number of Telegram Stars received by the bot for each 1000 Telegram Stars received by the affiliate program
     * sponsor from referred users
     * */
    @SerialName("commission_per_mille")
    val commissionPerMille: Int
) : TransactionPartner() {

    /** Type of the transaction partner, always “affiliate_program” */
    override val type: String = "affiliate_program"

}

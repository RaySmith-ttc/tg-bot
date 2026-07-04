package ru.raysmith.tgbot.model.api.chat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Colors [Red], [Orange], [Purple], [Green], [Cyan], [Blue] and [Pink] can be customized by app themes.
 * Additionally, the others colors are currently in use.
 * */
@Serializable
enum class AccentColor(val lightColors: List<String>?, val darkColors: List<String>?) {
    @SerialName("0") Red(null, null),
    @SerialName("1") Orange(null, null),
    @SerialName("2") Purple(null, null),
    @SerialName("3") Green(null, null),
    @SerialName("4") Cyan(null, null),
    @SerialName("5") Blue(null, null),
    @SerialName("6") Pink(null, null),

    @SerialName("7") RedGradient(listOf("E15052", "F9AE63"), listOf("FF9380", "992F37")),
    @SerialName("8") OrangeGradient(listOf("E0802B", "FAC534"), listOf("ECB04E", "C35714")),
    @SerialName("9") PurpleGradient(listOf("A05FF3", "F48FFF"), listOf("C697FF", "5E31C8")),
    @SerialName("10") GreenGradient(listOf("27A910", "A7DC57"), listOf("A7EB6E", "167E2D")),
    @SerialName("11") CyanGradient(listOf("27ACCE", "82E8D6"), listOf("40D8D0", "045C7F")),
    @SerialName("12") BlueGradient(listOf("3391D4", "7DD3F0"), listOf("52BFFF", "0B5494")),
    @SerialName("13") PinkGradient(listOf("DD4371", "FFBE9F"), listOf("FF86A6", "8E366E")),

    @SerialName("14") BlueRedWhite(listOf("247BED", "F04856", "FFFFFF"), listOf("3FA2FE", "E5424F", "FFFFFF")),
    @SerialName("15") OrangeGreenWhite(listOf("D67722", "1EA011", "FFFFFF"), listOf("FF905E", "32A527", "FFFFFF")),
    @SerialName("16") GreenRedWhite(listOf("179E42", "E84A3F", "FFFFFF"), listOf("66D364", "D5444F", "FFFFFF")),
    @SerialName("17") CyanGreenWhite(listOf("2894AF", "6FC456", "FFFFFF"), listOf("22BCE2", "3DA240", "FFFFFF")),
    @SerialName("18") CyanPeachYellow(listOf("0C9AB3", "FFAD95", "FFE6B5"), listOf("22BCE2", "FF9778", "FFDA6B")),
    @SerialName("19") PurpleOrangeYellow(listOf("7757D6", "F79610", "FFDE8E"), listOf("9791FF", "F2731D", "FFDB59")),
    @SerialName("20") BlueYellowWhite(listOf("1585CF", "F2AB1D", "FFFFFF"), listOf("3DA6EB", "EEA51D", "FFFFFF"));
}

package ru.raysmith.tgbot.model.api.chat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ProfileAccentColor(val lightColors: List<String>, val darkColors: List<String>) {
    @SerialName("0") Red(listOf("BA5650"), listOf("9C4540")),
    @SerialName("1") Orange(listOf("C27C3E"), listOf("945E2C")),
    @SerialName("2") Purple(listOf("956AC8"), listOf("715099")),
    @SerialName("3") Green(listOf("49A355"), listOf("33713B")),
    @SerialName("4") Cyan(listOf("3E97AD"), listOf("387E87")),
    @SerialName("5") Blue(listOf("5A8FBB"), listOf("477194")),
    @SerialName("6") Pink(listOf("B85378"), listOf("944763")),
    @SerialName("7") Grey(listOf("7F8B95"), listOf("435261")),

    @SerialName("8") RedGradient(listOf("C9565D", "D97C57"), listOf("994343", "AC583E")),
    @SerialName("9") OrangeGradient(listOf("CF7244", "CC9433"), listOf("8F552F", "A17232")),
    @SerialName("10") PurpleGradient(listOf("9662D4", "B966B6"), listOf("634691", "9250A2")),
    @SerialName("11") GreenGradient(listOf("3D9755", "89A650"), listOf("296A43", "5F8F44")),
    @SerialName("12") CyanGradient(listOf("3D95BA", "50AD98"), listOf("306C7C", "3E987E")),
    @SerialName("13") BlueGradient(listOf("538BC2", "4DA8BD"), listOf("38618C", "458BA1")),
    @SerialName("14") PinkGradient(listOf("B04F74", "D1666D"), listOf("884160", "A65259")),
    @SerialName("15") GreyGradient(listOf("637482", "7B8A97"), listOf("53606E", "384654"));
}

package ru.raysmith.tgbot.webappapp.pages.backbutton

import react.Props

external interface BBProps : Props {
    var setIsBackButtonDefaultOnClickEnabled: (value: Boolean) -> Unit
}
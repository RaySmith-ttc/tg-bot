package ru.raysmith.tgbot.webappapp.pages

import mui.material.Typography
import mui.material.styles.TypographyVariant
import mui.system.sx
import react.FC
import react.PropsWithChildren
import react.useEffectOnce
import ru.raysmith.tgbot.hooks.useBackButton
import ru.raysmith.tgbot.webappapp.wrappers.mb
import web.cssom.px

external interface BaseSubPageLayoutProps : PropsWithChildren {
    var title: String
}

val BaseSubPageLayout = FC<BaseSubPageLayoutProps> { props ->
    val backButton = useBackButton()

    useEffectOnce {
        backButton.show()
    }

    Typography {
        variant = TypographyVariant.h5
        +props.title
        sx { mb = 16.px }
    }

    +props.children
}
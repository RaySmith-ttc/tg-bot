package ru.raysmith.tgbot.webappapp

import js.objects.jso
import js.process
import kotlinx.browser.window
import mui.material.Box
import mui.material.Button
import mui.material.LinkProps
import mui.material.Typography
import mui.material.styles.TypographyVariant
import mui.system.sx
import react.*
import react.dom.client.createRoot
import react.dom.html.ReactHTML
import react.dom.html.ReactHTML.br
import react.router.Outlet
import react.router.dom.Link
import ru.raysmith.tgbot.Telegram
import ru.raysmith.tgbot.verify
import ru.raysmith.tgbot.webApp
import web.cssom.GridArea
import web.cssom.NamedColor
import web.cssom.px
import web.dom.document

fun main() {
    val root = document.getElementById("root") ?: error("Couldn't find root container!")
    createRoot(root).render(App.create())
}

val App = FC<Props> {
    Router {

    }
}

val RootPage = FC<Props> {
    Box {
        sx {
            gridArea = "content".unsafeCast<GridArea>()
            padding = 30.px
        }

        component = ReactHTML.main

        Outlet {

        }
    }
}

val MainPage = FC<Props> {
    Header {

    }

    Typography {
        variant = TypographyVariant.body1
        sx { color = NamedColor.white }
        +"verified: ${window.Telegram.WebApp.verify(process.env.TG_BOT_TOKEN)}"
    }

    Button {
        component = RouterLink
        this.href = Paths.biometrical
        +"Biometrical"
    }
}

external interface RouterLinkProps : LinkProps /* Omit<LinkProps, 'to'> */ {

}

val RouterLink = ForwardRef<RouterLinkProps> { props ->
    Link {
        ref = props.ref
        to = props.href ?: error("href is required")
        +props
    }
}

val BiometricalPage = FC<Props> {

    var isInit by useState(false)
    var granted by useState<Boolean?>(null)
    var auth by useState<Boolean?>(null)
    var token by useState<String?>(null)

    useEffectOnce {
//        window.Telegram.WebApp.enableClosingConfirmation()
//        println("pr")
//        println(process.env.TG_BOT_TOKEN)
//        println(js("process"))
//        println(js("process.env"))
//        println(js("process.env.TG_BOT_TOKEN"))
//        println(process)

        webApp.BiometricManager.init {
            isInit = true
            console.log("BiometricManager inited")
        }
    }


    Header {

    }

    Typography {
        sx { color = NamedColor.white }
        +"isInit: $isInit"
        br()
        +"granted: $granted"
        br()
        +"auth: $auth"
        br()
        +"token: $token"
        br()
        +"BiometricManager.isBiometricAvailable: ${webApp.BiometricManager.isBiometricAvailable}"
        br()
        +"BiometricManager.isInited: ${webApp.BiometricManager.isInited}"
        br()
        +"BiometricManager.biometricType: ${webApp.BiometricManager.biometricType}"
        br()
        +"BiometricManager.isBiometricTokenSaved: ${webApp.BiometricManager.isBiometricTokenSaved}"
        br()
        +"BiometricManager.deviceId: ${webApp.BiometricManager.deviceId}"
        br()
        +"BiometricManager.isAccessGranted: ${webApp.BiometricManager.isAccessGranted}"
    }

    if (webApp.BiometricManager.isBiometricAvailable) {
            Button {
                +"Biometric Settings"
                onClick = {
                    webApp.BiometricManager.openSettings()
                }
            }
            Button {
                +"Request"
                onClick = {
                    webApp.BiometricManager.requestAccess(jso { reason = "Some reason" }) { res ->
                        granted = res
                    }
                }
            }
            Button {
                +"Authenticate"
                onClick = {
                    webApp.BiometricManager.authenticate(jso { reason = "Some reason" }) { res, t ->
                        auth = res
                        token = t
                    }
                }
            }
        }
}


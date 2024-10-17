package ru.raysmith.tgbot.webappapp.pages

import js.objects.jso
import mui.material.Button
import mui.material.Typography
import mui.system.sx
import react.FC
import react.Props
import react.dom.html.ReactHTML
import react.useEffectOnce
import react.useState
import ru.raysmith.tgbot.webApp
import ru.raysmith.tgbot.webappapp.components.Header
import web.cssom.NamedColor

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
        ReactHTML.br()
        +"granted: $granted"
        ReactHTML.br()
        +"auth: $auth"
        ReactHTML.br()
        +"token: $token"
        ReactHTML.br()
        +"BiometricManager.isBiometricAvailable: ${webApp.BiometricManager.isBiometricAvailable}"
        ReactHTML.br()
        +"BiometricManager.isInited: ${webApp.BiometricManager.isInited}"
        ReactHTML.br()
        +"BiometricManager.biometricType: ${webApp.BiometricManager.biometricType}"
        ReactHTML.br()
        +"BiometricManager.isBiometricTokenSaved: ${webApp.BiometricManager.isBiometricTokenSaved}"
        ReactHTML.br()
        +"BiometricManager.deviceId: ${webApp.BiometricManager.deviceId}"
        ReactHTML.br()
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
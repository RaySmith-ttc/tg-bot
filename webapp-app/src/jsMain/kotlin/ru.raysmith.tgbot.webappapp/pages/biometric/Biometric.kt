package ru.raysmith.tgbot.webappapp.pages.biometric

import js.objects.jso
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mui.icons.material.Settings
import mui.material.*
import mui.system.responsive
import mui.system.sx
import react.*
import ru.raysmith.tgbot.hooks.useBackButton
import ru.raysmith.tgbot.hooks.useBiometric
import ru.raysmith.tgbot.webApp
import ru.raysmith.tgbot.webappapp.OnClick
import ru.raysmith.tgbot.webappapp.components.FullPageLoading
import ru.raysmith.tgbot.webappapp.components.datadisplay.DataDisplayCheckbox
import ru.raysmith.tgbot.webappapp.components.datadisplay.DataDisplayTextField
import ru.raysmith.tgbot.webappapp.mainScope
import ru.raysmith.tgbot.webappapp.pages.BaseSubPageLayout
import ru.raysmith.tgbot.webappapp.wrappers.mt
import ru.raysmith.utils.uuid

val BiometricPage = FC<Props> {
    val biometric = useBiometric()
    val backButton = useBackButton()

    var inited by useState(biometric.isInited)
    var snackbar by useState(false)
    var snackbarMessage by useState("")

    var granted by useState<Boolean?>(null)
    var auth by useState<Boolean?>(null)
    var token by useState<String?>(null)

    useEffectOnce {
        if (!inited) {
            biometric.init {
                inited = true
            }

            // callback not called if open with WebApp version < 7.2 (e.g. desktop browser)
            mainScope.launch {
                delay(1000)
                inited = true
            }
        }
    }

    val onTokenSave = useCallback<OnClick>(token) {
        val tempToken = uuid()
        biometric.updateBiometricToken(tempToken) { res ->

            if (res) {
                token = tempToken
                snackbar = true
                snackbarMessage = if (token.isNullOrEmpty()) "New token saved" else "Token updated"
            }
        }
    }

    val onTokenReset: OnClick = {
        biometric.updateBiometricToken("") { res ->
            if (res) {
                token = ""
                snackbar = true
                snackbarMessage = "Token reset"
            }
        }
    }

    useEffectOnce {
        backButton.show()
    }

    if (!inited) {
        FullPageLoading {}
        return@FC
    }

    BaseSubPageLayout {
        title = "Biometric"
        if (!biometric.isBiometricAvailable) {
            Alert {
                +"BiometricManager not available on this device. To access the features on this page, use a device with a fingerprint scanner or face-base biometric"
                severity = AlertColor.warning.unsafeCast<String>()
            }
            return@BaseSubPageLayout
        }

        if (!biometric.isAccessGranted || granted == false) {
            if (!biometric.isAccessRequested) {
                Stack {
                    spacing = responsive(0.5)

                    Alert {
                        +"Please allow the bot to use biometry"
                        severity = AlertColor.warning.unsafeCast<String>()
                    }

                    Button {
                        +"Allow"
                        fullWidth = true
                        size = Size.large
                        startIcon = Settings.create()
                        onClick = {
                            biometric.requestAccess(jso { reason = "Some reason" }) { res ->
                                granted = res
                            }
                        }
                    }
                }
                return@BaseSubPageLayout
            }

            Stack {
                spacing = responsive(2)

                Alert {
                    +"The bot does not have permission for biometry. Go to the settings and grant permission to use it."
                    severity = AlertColor.warning.unsafeCast<String>()
                }

                Button {
                    +"Biometric Settings"
                    fullWidth = true
                    size = Size.large
                    startIcon = Settings.create()
                    onClick = {
                        biometric.openSettings()
                    }
                }
            }
            return@BaseSubPageLayout
        }

        Stack {
            DataDisplayCheckbox {
                label = "isInited"
                checked = biometric.isInited
            }

            Stack {
                spacing = responsive(2)
                sx { mt = 2 }

                DataDisplayTextField {
                    label = ReactNode("biometricType")
                    value = Typography.create { +biometric.biometricType.toString() }
                }

                DataDisplayTextField {
                    label = ReactNode("deviceId")
                    value = Typography.create { +biometric.deviceId }
                    copyToClipboard = true
                }
            }

            DataDisplayCheckbox {
                label = "isAccessRequested"
                checked = biometric.isAccessRequested
            }

            DataDisplayCheckbox {
                label = "isAccessGranted"
                checked = biometric.isAccessGranted
            }

            DataDisplayCheckbox {
                label = "isBiometricTokenSaved"
                checked = biometric.isBiometricTokenSaved
            }

            DataDisplayTextField {
                label = ReactNode("token")
                value = token ?: ""
            }

            Stack {
                spacing = responsive(0.5)
                direction = responsive(StackDirection.row)

                Button {
                    +"Save token"
                    onClick = onTokenSave
                }

                Button {
                    +"Reset token"
                    onClick = onTokenReset
                }
            }
        }

        if (biometric.isBiometricAvailable) {

            Stack {
                direction = responsive(StackDirection.row)

                DataDisplayCheckbox {
                    label = "Access granted"
                    checked = granted ?: false
                }

                Button {
                    +"Request"
                    onClick = {
                        biometric.requestAccess(jso { reason = "Some reason" }) { res ->
                            granted = res
                        }
                    }
                }
            }

            Stack {
                direction = responsive(StackDirection.row)
                DataDisplayCheckbox {
                    label = "Authenticated"
                    checked = auth ?: false
                }

                Button {
                    +"Authenticate"
                    onClick = {
                        biometric.authenticate(jso { reason = "Some reason" }) { res, t ->
                            auth = res
                            token = t
                        }
                    }
                }
            }
        }

        Snackbar {
            open = snackbar
            onClose = { _, _ -> snackbar = false }
            message = ReactNode(snackbarMessage)
            autoHideDuration = 3000
        }
    }
}
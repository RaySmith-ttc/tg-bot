package ru.raysmith.tgbot.webappapp.pages.biometric

import js.objects.jso
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mui.icons.material.Settings
import mui.material.*
import mui.system.sx
import react.*
import ru.raysmith.tgbot.hooks.useBackButton
import ru.raysmith.tgbot.hooks.useBiometric
import ru.raysmith.tgbot.webappapp.components.ButtonsGroupControl
import ru.raysmith.tgbot.webappapp.components.FullPageLoading
import ru.raysmith.tgbot.webappapp.components.applyControlButtonStyle
import ru.raysmith.tgbot.webappapp.components.datadisplay.DataDisplayCheckbox
import ru.raysmith.tgbot.webappapp.components.datadisplay.DataDisplayTableRow
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

    val onTokenSave = useCallback(token) {
        val tempToken = uuid()
        biometric.updateBiometricToken(tempToken) { res ->
            if (res) {
                token = tempToken
                snackbar = true
                snackbarMessage = if (token.isNullOrEmpty()) "New token saved" else "Token updated"
            }
        }
    }

    val onTokenReset = {
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
                severity = AlertColor.error.unsafeCast<String>()
            }
            return@BaseSubPageLayout
        }

        if (!biometric.isAccessGranted || granted == false) {
            if (!biometric.isAccessRequested) {
                Alert {
                    +"Please allow the bot to use biometry"
                    severity = AlertColor.warning.unsafeCast<String>()
                }

                Button {
                    +"Allow"
                    applyControlButtonStyle()
                    startIcon = Settings.create()
                    onClick = {
                        biometric.requestAccess(jso { reason = "Some reason" }) { res ->
                            granted = res
                        }
                    }
                }
                return@BaseSubPageLayout
            }

            Alert {
                +"The bot does not have permission for biometry. Go to the settings and grant permission to use it."
                severity = AlertColor.warning.unsafeCast<String>()
            }

            Button {
                +"Open settings"
                applyControlButtonStyle()
                startIcon = Settings.create()
                onClick = {
                    biometric.openSettings()
                }
            }

            return@BaseSubPageLayout
        }

        Table {
            TableBody {
                DataDisplayTableRow {
                    title = "isInited"
                    value = DataDisplayCheckbox.create { checked = biometric.isInited }
                }
                DataDisplayTableRow {
                    title = "biometricType"
                    value = Typography.create { +biometric.biometricType.toString() }
                }
                DataDisplayTableRow {
                    title = "deviceId"
                    value = Typography.create { +biometric.deviceId }
                }
                DataDisplayTableRow {
                    title = "isAccessRequested"
                    value = DataDisplayCheckbox.create { checked = biometric.isAccessRequested }
                }
                DataDisplayTableRow {
                    title = "isAccessGranted"
                    value = DataDisplayCheckbox.create { checked = biometric.isAccessGranted }
                }
                DataDisplayTableRow {
                    title = "isBiometricTokenSaved"
                    value = DataDisplayCheckbox.create { checked = biometric.isBiometricTokenSaved }

                    DataDisplayTextField {
                        label = ReactNode("token")
                        value = token ?: ""
                        copyToClipboard = true
                        fullWidth = true
                    }

                    ButtonsGroupControl {
                        sx { mt = 1 }
                        items = mapOf(
                            "Save token" to { onTokenSave() },
                            "Reset token" to onTokenReset
                        )
                    }
                }

                if (biometric.isBiometricAvailable) {
                    DataDisplayTableRow {
                        title = "Access granted"
                        value = DataDisplayCheckbox.create { checked = granted ?: false }
                        Button {
                            +"Request"
                            onClick = {
                                biometric.requestAccess(jso { reason = "Some reason" }) { res ->
                                    granted = res
                                }
                            }
                        }
                    }

                    DataDisplayTableRow {
                        title = "Authenticated"
                        value = DataDisplayCheckbox.create { checked = auth ?: false }
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
            }
        }

        Snackbar {
            open = snackbar
            onClick = { _ -> snackbar = false }
            onClose = { _, _ -> snackbar = false }
            message = ReactNode(snackbarMessage)
            autoHideDuration = 3000
        }
    }
}
package ru.raysmith.tgbot

import seskar.js.JsValue

@Suppress("NESTED_CLASS_IN_EXTERNAL_INTERFACE")
sealed external interface EventType {
    companion object {

        /**
         * Occurs when the Mini App becomes active (e.g., opened from minimized state or selected among tabs).
         *
         * *eventHandler* receives no parameters.
         *
         * @since Bot API 8.0
         */
        @JsValue("activated")
        val activated: EventType

        /**
         * Occurs when the Mini App becomes inactive (e.g., minimized or moved to an inactive tab).
         *
         * *eventHandler* receives no parameters.
         *
         * @since Bot API 8.0
         */
        @JsValue("deactivated")
        val deactivated: EventType

        /**
         * Occurs whenever theme settings are changed in the user's Telegram app (including switching to night mode).
         *
         * *eventHandler* receives no parameters, new theme settings and color scheme can be received via
         * `this.themeParams` and `this.colorScheme` respectively.
         * */
        @JsValue("themeChanged")
        val themeChanged: EventType

        /**
         * Occurs when the visible section of the Mini App is changed.
         *
         * *eventHandler* receives an object with the single field *isStateStable*. If *isStateStable* is true, the
         * resizing of the Mini App is finished. If it is false, the resizing is ongoing (the user is expanding or
         * collapsing the Mini App or an animated object is playing).
         * The current value of the visible section’s height is available in `this.viewportHeight`.
         * */
        @JsValue("viewportChanged")
        val viewportChanged: EventType

        /**
         * Occurs when the device's safe area insets change (e.g., due to orientation change or screen adjustments).
         *
         * *eventHandler* receives no parameters. The current inset values can be accessed via `this.safeAreaInset`.
         *
         * @since Bot API 8.0
         */
        @JsValue("safeAreaChanged")
        val safeAreaChanged: EventType

        /**
         * Occurs when the safe area for content changes (e.g., due to orientation change or screen adjustments).
         *
         * *eventHandler* receives no parameters. The current inset values can be accessed via
         * `this.contentSafeAreaInset`.
         *
         * @since Bot API 8.0
         */
        @JsValue("contentSafeAreaChanged")
        val contentSafeAreaChanged: EventType

        /**
         * Occurs when the [main button](https://core.telegram.org/bots/webapps#bottombutton) is pressed.
         *
         * *eventHandler* receives no parameters.
         * */
        @JsValue("mainButtonClicked")
        val mainButtonClicked: EventType

        /**
         * Occurs when the [secondary button](https://core.telegram.org/bots/webapps#bottombutton) is pressed.
         *
         * *eventHandler* receives no parameters.
         *
         * @since Bot API 7.10
         * */
        @JsValue("secondaryButtonClicked")
        val secondaryButtonClicked: EventType

        /**
         * Occurs when the back button is pressed.
         *
         * *eventHandler* receives no parameters.
         *
         * @since Bot API 6.1
         * */
        @JsValue("backButtonClicked")
        val backButtonClicked: EventType

        /**
         * Occurs when the Settings item in context menu is pressed.
         *
         * *eventHandler* receives no parameters.
         *
         * @since Bot API 6.1
         * */
        @JsValue("settingsButtonClicked")
        val settingsButtonClicked: EventType

        /**
         * Occurs when the opened invoice is closed.
         *
         * *eventHandler* receives an object with the two fields: url – invoice link provided and status –
         * one of the invoice statuses:
         * - `paid` – invoice was paid successfully,
         * - `cancelled` – user closed this invoice without paying,
         * - `failed` – user tried to pay, but the payment was failed,
         * - `pending` – the payment is still processing. The bot will receive a service message about a
         * [successful payment](https://core.telegram.org/bots/api#successfulpayment) when the payment is
         * successfully paid.
         *
         * @since Bot API 6.1
         * */
        @JsValue("invoiceClosed")
        val invoiceClosed: EventType

        /**
         * Occurs when the opened popup is closed.
         *
         * *eventHandler* receives an object with the single field `button_id` – the value of the field `id` of the
         * pressed button. If no buttons were pressed, the field `button_id` will be `null`.
         *
         * @since Bot API 6.2
         * */
        @JsValue("popupClosed")
        val popupClosed: EventType

        /**
         * Occurs when the QR code scanner catches a code with text data.
         *
         * *eventHandler* receives an object with the single field data containing text data from the QR code.
         *
         * @since Bot API 6.4
         * */
        @JsValue("qrTextReceived")
        val qrTextReceived: EventType

        /**
         * Occurs when the QR code scanner popup is closed by the user.
         *
         * *eventHandler* receives no parameters
         *
         * @since Bot API 7.7
         * */
        @JsValue("scanQrPopupClosed")
        val scanQrPopupClosed: EventType

        /**
         * Occurs when the [WebApp.readTextFromClipboard] method is called.
         *
         * *eventHandler* receives an object with the single field data containing text `data` from the clipboard.
         * If the clipboard contains non-text data, the field `data` will be an empty string.
         * If the Mini App has no access to the clipboard, the field `data` will be `null`.
         *
         * @since Bot API 6.4
         * */
        @JsValue("clipboardTextReceived")
        val clipboardTextReceived: EventType

        /**
         * Occurs when the write permission was requested.
         *
         * *eventHandler* receives an object with the single field `status` containing one of the statuses:
         * - `allowed` – user granted write permission to the bot,
         * - `cancelled` – user declined this request.
         *
         * @since Bot API 6.9
         * */
        @JsValue("writeAccessRequested")
        val writeAccessRequested: EventType

        /**
         * Occurs when the user's phone number was requested.
         *
         * *eventHandler* receives an object with the single field `status` containing one of the statuses:
         * - `sent` – user shared their phone number with the bot,
         * - `cancelled` – user declined this request.
         *
         * @since Bot API 6.9
         * */
        @JsValue("contactRequested")
        val contactRequested: EventType

        /**
         * Occurs whenever [BiometricManager] object is changed.
         *
         * *eventHandler* receives no parameters.
         *
         * @since Bot API 7.2
         * */
        @JsValue("biometricManagerUpdated")
        val biometricManagerUpdated: EventType

        /**
         * Occurs whenever biometric authentication was requested.
         *
         * *eventHandler* receives an object with the field `isAuthenticated` containing a boolean indicating whether
         * the user was authenticated successfully. If `isAuthenticated` is true, the field `biometricToken` will
         * contain the biometric token stored in secure storage on the device.
         *
         * @since Bot API 7.2
         * */
        @JsValue("biometricAuthRequested")
        val biometricAuthRequested: EventType

        /**
         * Occurs whenever the biometric token was updated.
         *
         * *eventHandler* receives an object with the single field `isUpdated`, containing a boolean indicating whether
         * the token was updated.
         *
         * @since Bot API 7.2
         * */
        @JsValue("biometricTokenUpdated")
        val biometricTokenUpdated: EventType


        /**
         * Occurs whenever the Mini App enters or exits fullscreen mode.
         *
         * *eventHandler* receives no parameters. The current fullscreen state can be checked via `this.isFullscreen`.
         *
         * @since Bot API 8.0
         */
        @JsValue("fullscreenChanged")
        val fullscreenChanged: EventType

        /**
         * Occurs if a request to enter fullscreen mode fails.
         *
         * *eventHandler* receives an object with the single field `error`, describing the reason for the failure.
         * Possible values for `error` are:
         * - `UNSUPPORTED` – Fullscreen mode is not supported on this device or platform.
         * - `ALREADY_FULLSCREEN` – The Mini App is already in fullscreen mode.
         *
         * @since Bot API 8.0
         */
        @JsValue("fullscreenFailed")
        val fullscreenFailed: EventType

        /**
         * Occurs when the Mini App is successfully added to the home screen.
         *
         * *eventHandler* receives no parameters.
         *
         * @since Bot API 8.0
         */
        @JsValue("homeScreenAdded")
        val homeScreenAdded: EventType

        /**
         * Occurs after checking the home screen status.
         *
         * *eventHandler* receives an object with the field `status`, which is a string indicating the current home
         * screen status.
         * Possible values for `status` are:
         * - `unsupported` – the feature is not supported, and it is not possible to add the icon to the home screen,
         * - `unknown` – the feature is supported, and the icon can be added, but it is not possible to determine if the
         * icon has already been added,
         * - `added` – the icon has already been added to the home screen,
         * - `missed` – the icon has not been added to the home screen.
         *
         * @since Bot API 8.0
         */
        @JsValue("homeScreenChecked")
        val homeScreenChecked: EventType


        /**
         * Occurs when accelerometer tracking has started successfully.
         *
         * *eventHandler* receives no parameters.
         *
         * @since Bot API 8.0
         */
        @JsValue("accelerometerStarted")
        val accelerometerStarted: EventType

        /**
         * Occurs when accelerometer tracking has stopped.
         *
         * *eventHandler* receives no parameters.
         *
         * @since Bot API 8.0
         */
        @JsValue("accelerometerStopped")
        val accelerometerStopped: EventType

        /**
         * Occurs with the specified frequency after calling the start method, sending the current accelerometer data.
         *
         * *eventHandler* receives no parameters. The current acceleration values can be received via `this.x`,
         * `this.y`, and `this.z` respectively.
         *
         * @since Bot API 8.0
         */
        @JsValue("accelerometerChanged")
        val accelerometerChanged: EventType

        /**
         * Occurs if a request to start accelerometer tracking fails.
         *
         * *eventHandler* receives an object with the single field `error`, describing the reason for the failure.
         * Possible values for `error` are:
         * - `UNSUPPORTED` – Accelerometer tracking is not supported on this device or platform.
         *
         * @since Bot API 8.0
         */
        @JsValue("accelerometerFailed")
        val accelerometerFailed: EventType

        /**
         * Occurs when device orientation tracking has started successfully.
         *
         * *eventHandler* receives no parameters.
         *
         * @since Bot API 8.0
         */
        @JsValue("deviceOrientationStarted")
        val deviceOrientationStarted: EventType

        /**
         * Occurs when device orientation tracking has stopped.
         *
         * *eventHandler* receives no parameters.
         *
         * @since Bot API 8.0
         */
        @JsValue("deviceOrientationStopped")
        val deviceOrientationStopped: EventType

        /**
         * Occurs with the specified frequency after calling the start method, sending the current orientation data.
         *
         * *eventHandler* receives no parameters. The current device orientation values can be received via
         * `this.alpha`, `this.beta`, and `this.gamma` respectively.
         *
         * @since Bot API 8.0
         */
        @JsValue("deviceOrientationChanged")
        val deviceOrientationChanged: EventType

        /**
         * Occurs if a request to start device orientation tracking fails.
         *
         * *eventHandler* receives an object with the single field `error`, describing the reason for the failure.
         * Possible values for `error` are:
         * - `UNSUPPORTED` – Device orientation tracking is not supported on this device or platform.
         *
         * @since Bot API 8.0
         */
        @JsValue("deviceOrientationFailed")
        val deviceOrientationFailed: EventType

        /**
         * Occurs when gyroscope tracking has started successfully.
         *
         * *eventHandler* receives no parameters.
         *
         * @since Bot API 8.0
         */
        @JsValue("gyroscopeStarted")
        val gyroscopeStarted: EventType

        /**
         * Occurs when gyroscope tracking has stopped.
         *
         * *eventHandler* receives no parameters.
         *
         * @since Bot API 8.0
         */
        @JsValue("gyroscopeStopped")
        val gyroscopeStopped: EventType

        /**
         * Occurs with the specified frequency after calling the start method, sending the current gyroscope data.
         *
         * *eventHandler* receives no parameters. The current rotation rates can be received via `this.x`, `this.y`, and
         * `this.z` respectively.
         *
         * @since Bot API 8.0
         */
        @JsValue("gyroscopeChanged")
        val gyroscopeChanged: EventType

        /**
         * Occurs if a request to start gyroscope tracking fails.
         *
         * *eventHandler* receives an object with the single field `error`, describing the reason for the failure.
         * Possible values for `error` are:
         * - `UNSUPPORTED` – Gyroscope tracking is not supported on this device or platform.
         *
         * @since Bot API 8.0
         */
        @JsValue("gyroscopeFailed")
        val gyroscopeFailed: EventType

        /**
         * Occurs whenever LocationManager object is changed.
         *
         * *eventHandler* receives no parameters.
         *
         * @since Bot API 8.0
         */
        @JsValue("locationManagerUpdated")
        val locationManagerUpdated: EventType

        /**
         * Occurs when location data is requested.
         *
         * *eventHandler* receives an object with the single field `locationData` of type [LocationData], containing the
         * current location information.
         *
         * @since Bot API 8.0
         */
        @JsValue("locationRequested")
        val locationRequested: EventType

        /**
         * Occurs when the message is successfully shared by the user.
         *
         * *eventHandler* receives no parameters.
         *
         * @since Bot API 8.0
         */
        @JsValue("shareMessageSent")
        val shareMessageSent: EventType

        /**
         * Occurs if sharing the message fails.
         *
         * *eventHandler* receives an object with the single field `error`, describing the reason for the failure.
         * Possible values for `error` are:
         * - `UNSUPPORTED` – The feature is not supported by the client.
         * - `MESSAGE_EXPIRED` – The message could not be retrieved because it has expired.
         * - `MESSAGE_SEND_FAILED` – An error occurred while attempting to send the message.
         * - `USER_DECLINED` – The user closed the dialog without sharing the message.
         * - `UNKNOWN_ERROR` – An unknown error occurred.
         *
         * @since Bot API 8.0
         */
        @JsValue("shareMessageFailed")
        val shareMessageFailed: EventType

        /**
         * Occurs when the emoji status is successfully set.
         *
         * *eventHandler* receives no parameters.
         *
         * @since Bot API 8.0
         */
        @JsValue("emojiStatusSet")
        val emojiStatusSet: EventType

        /**
         * Occurs if setting the emoji status fails.
         *
         * *eventHandler* receives an object with the single field `error`, describing the reason for the failure.
         * Possible values for `error` are:
         * - `UNSUPPORTED` – The feature is not supported by the client.
         * - `SUGGESTED_EMOJI_INVALID` – One or more emoji identifiers are invalid.
         * - `DURATION_INVALID` – The specified duration is invalid.
         * - `USER_DECLINED` – The user closed the dialog without setting a status.
         * - `SERVER_ERROR` – A server error occurred when attempting to set the status.
         * - `UNKNOWN_ERROR` – An unknown error occurred.
         *
         * @since Bot API 8.0
         */
        @JsValue("emojiStatusFailed")
        val emojiStatusFailed: EventType

        /**
         * Occurs when the write permission was requested.
         *
         * *eventHandler* receives an object with the single field `status` containing one of the statuses:
         * - `allowed` – user granted emoji status permission to the bot,
         * - `cancelled` – user declined this request.
         *
         * @since Bot API 8.0
         */
        @JsValue("emojiStatusAccessRequested")
        val emojiStatusAccessRequested: EventType

        /**
         * Occurs when the user responds to the file download request.
         *
         * *eventHandler* receives an object with the single field `status` containing one of the statuses:
         * - `downloading` – the file download has started,
         * - `cancelled` – user declined this request.
         *
         * @since Bot API 8.0
         */
        @JsValue("fileDownloadRequested")
        val fileDownloadRequested: EventType
    }
}
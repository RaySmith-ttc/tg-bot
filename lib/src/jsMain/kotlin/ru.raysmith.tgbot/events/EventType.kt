package ru.raysmith.tgbot.events

import ru.raysmith.tgbot.*
import ru.raysmith.tgbot.hooks.*
import seskar.js.JsValue
import web.scheduling.VoidFunction

@Suppress("NESTED_CLASS_IN_EXTERNAL_INTERFACE")
sealed external interface EventType<T> {
    companion object {

        /**
         * Occurs when the Mini App becomes active (e.g., opened from minimized state or selected among tabs).
         *
         * *eventHandler* receives no parameters.
         *
         * @since Bot API 8.0
         * @see useWebAppState
         */
        @JsValue("activated")
        val activated: EventType<VoidFunction>

        /**
         * Occurs when the Mini App becomes inactive (e.g., minimized or moved to an inactive tab).
         *
         * *eventHandler* receives no parameters.
         *
         * @since Bot API 8.0
         * @see useWebAppState
         */
        @JsValue("deactivated")
        val deactivated: EventType<VoidFunction>

        /**
         * Occurs whenever theme settings are changed in the user's Telegram app (including switching to night mode).
         *
         * *eventHandler* receives no parameters, new theme settings and color scheme can be received via
         * `this.themeParams` and `this.colorScheme` respectively.
         *
         * @see useThemeParams
         * */
        @JsValue("themeChanged")
        val themeChanged: EventType<VoidFunction>

        /**
         * Occurs when the visible section of the Mini App is changed.
         *
         * *eventHandler* receives an [ViewportChanged] object.
         * The current value of the visible section’s height is available in `this.viewportHeight`.
         *
         * @see useViewport
         * */
        @JsValue("viewportChanged")
        val viewportChanged: EventType<(ViewportChanged) -> Unit>

        /**
         * Occurs when the device's safe area insets change (e.g., due to orientation change or screen adjustments).
         *
         * *eventHandler* receives no parameters. The current inset values can be accessed via `this.safeAreaInset`.
         *
         * @since Bot API 8.0
         * @see useInsets
         */
        @JsValue("safeAreaChanged")
        val safeAreaChanged: EventType<VoidFunction>

        /**
         * Occurs when the safe area for content changes (e.g., due to orientation change or screen adjustments).
         *
         * *eventHandler* receives no parameters. The current inset values can be accessed via
         * `this.contentSafeAreaInset`.
         *
         * @since Bot API 8.0
         * @see useInsets
         */
        @JsValue("contentSafeAreaChanged")
        val contentSafeAreaChanged: EventType<VoidFunction>

        /**
         * Occurs when the [main button](https://core.telegram.org/bots/webapps#bottombutton) is pressed.
         *
         * *eventHandler* receives no parameters.
         *
         * @see useBottomButton
         * */
        @JsValue("mainButtonClicked")
        val mainButtonClicked: EventType<VoidFunction>

        /**
         * Occurs when the [secondary button](https://core.telegram.org/bots/webapps#bottombutton) is pressed.
         *
         * *eventHandler* receives no parameters.
         *
         * @since Bot API 7.10
         * @see useBottomButton
         * */
        @JsValue("secondaryButtonClicked")
        val secondaryButtonClicked: EventType<VoidFunction>

        /**
         * Occurs when the back button is pressed.
         *
         * *eventHandler* receives no parameters.
         *
         * @since Bot API 6.1
         * @see useBackButton
         * */
        @JsValue("backButtonClicked")
        val backButtonClicked: EventType<VoidFunction>

        /**
         * Occurs when the Settings item in context menu is pressed.
         *
         * *eventHandler* receives no parameters.
         *
         * @since Bot API 6.1
         * @see useSettingsButton
         * */
        @JsValue("settingsButtonClicked")
        val settingsButtonClicked: EventType<VoidFunction>

        /**
         * Occurs when the opened invoice is closed.
         *
         * *eventHandler* receives an [InvoiceClosed] object.
         *
         * @since Bot API 6.1
         * */
        @JsValue("invoiceClosed")
        val invoiceClosed: EventType<(InvoiceClosed) -> Unit>

        /**
         * Occurs when the opened popup is closed.
         *
         * *eventHandler* receives an [PopupClosed].
         * If no buttons were pressed, the field [PopupClosed.buttonId] will be `null`.
         *
         * @since Bot API 6.2
         * */
        @JsValue("popupClosed")
        val popupClosed: EventType<(PopupClosed) -> Unit>

        /**
         * Occurs when the QR code scanner catches a code with text data.
         *
         * *eventHandler* receives an [QrTextReceived] object.
         *
         * @since Bot API 6.4
         * */
        @JsValue("qrTextReceived")
        val qrTextReceived: EventType<(QrTextReceived) -> Unit>

        /**
         * Occurs when the QR code scanner popup is closed by the user.
         *
         * *eventHandler* receives no parameters
         *
         * @since Bot API 7.7
         * */
        @JsValue("scanQrPopupClosed")
        val scanQrPopupClosed: EventType<VoidFunction>

        /**
         * Occurs when the [WebApp.readTextFromClipboard] method is called.
         *
         * *eventHandler* receives an [ClipboardTextReceived] object.
         *
         * @since Bot API 6.4
         * */
        @JsValue("clipboardTextReceived")
        val clipboardTextReceived: EventType<(ClipboardTextReceived) -> Unit>

        /**
         * Occurs when the write permission was requested.
         *
         * *eventHandler* receives an [WriteAccessRequested] object.
         *
         * @since Bot API 6.9
         * */
        @JsValue("writeAccessRequested")
        val writeAccessRequested: EventType<(WriteAccessRequested) -> Unit>

        /**
         * Occurs when the user's phone number was requested.
         *
         * *eventHandler* receives an [ContactRequested] object.
         *
         * @since Bot API 6.9
         * */
        @JsValue("contactRequested")
        val contactRequested: EventType<(ContactRequested) -> Unit>

        /**
         * Occurs whenever [BiometricManager] object is changed.
         *
         * *eventHandler* receives no parameters.
         *
         * @since Bot API 7.2
         * @see useBiometric
         * */
        @JsValue("biometricManagerUpdated")
        val biometricManagerUpdated: EventType<VoidFunction>

        /**
         * Occurs whenever biometric authentication was requested.
         *
         * *eventHandler* receives an [BiometricAuthRequested] object.
         *
         * @since Bot API 7.2
         * @see useBiometric
         * */
        @JsValue("biometricAuthRequested")
        val biometricAuthRequested: EventType<(BiometricAuthRequested) -> Unit> // TODO test

        /**
         * Occurs whenever the biometric token was updated.
         *
         * *eventHandler* receives an [BiometricTokenUpdated] object.
         *
         * @since Bot API 7.2
         * @see useBiometric
         * */
        @JsValue("biometricTokenUpdated")
        val biometricTokenUpdated: EventType<(BiometricTokenUpdated) -> Unit>

        /**
         * Occurs whenever the Mini App enters or exits fullscreen mode.
         *
         * *eventHandler* receives no parameters. The current fullscreen state can be checked via [WebApp.isFullscreen].
         *
         * @since Bot API 8.0
         * @see useViewport
         */
        @JsValue("fullscreenChanged")
        val fullscreenChanged: EventType<VoidFunction>

            /**
         * Occurs if a request to enter fullscreen mode fails.
         *
         * *eventHandler* receives an [FullscreenFailed] object.
         *
         * @since Bot API 8.0
         * @see useViewport
         */
        @JsValue("fullscreenFailed")
        val fullscreenFailed: EventType<(FullscreenFailed) -> Unit>

        /**
         * Occurs when the Mini App is successfully added to the home screen.
         *
         * *eventHandler* receives no parameters.
         *
         * @since Bot API 8.0
         */
        @JsValue("homeScreenAdded")
        val homeScreenAdded: EventType<VoidFunction>

        /**
         * Occurs after checking the home screen status.
         *
         * *eventHandler* receives an [HomeScreenChecked] object.
         *
         * @since Bot API 8.0
         */
        @JsValue("homeScreenChecked")
        val homeScreenChecked: EventType<(HomeScreenChecked) -> Unit>

        /**
         * Occurs when accelerometer tracking has started successfully.
         *
         * *eventHandler* receives no parameters.
         *
         * @since Bot API 8.0
         */
        @JsValue("accelerometerStarted")
        val accelerometerStarted: EventType<VoidFunction>

        /**
         * Occurs when accelerometer tracking has stopped.
         *
         * *eventHandler* receives no parameters.
         *
         * @since Bot API 8.0
         * @see useAccelerometer
         */
        @JsValue("accelerometerStopped")
        val accelerometerStopped: EventType<VoidFunction>

        /**
         * Occurs with the specified frequency after calling the start method, sending the current accelerometer data.
         *
         * *eventHandler* receives no parameters. The current acceleration values can be received via
         * [WebApp.Accelerometer.x][Accelerometer.x], [WebApp.Accelerometer.y][Accelerometer.y], and
         * [WebApp.Accelerometer.z][Accelerometer.z] respectively.
         *
         * @since Bot API 8.0
         * @see useAccelerometer
         */
        @JsValue("accelerometerChanged")
        val accelerometerChanged: EventType<VoidFunction>

        /**
         * Occurs if a request to start accelerometer tracking fails.
         *
         * *eventHandler* receives an [AccelerometerFailed] object.
         *
         * @since Bot API 8.0
         * @see useAccelerometer
         */
        @JsValue("accelerometerFailed")
        val accelerometerFailed: EventType<(AccelerometerFailed) -> Unit>

        /**
         * Occurs when device orientation tracking has started successfully.
         *
         * *eventHandler* receives no parameters.
         *
         * @since Bot API 8.0
         * @see useDeviceOrientation
         */
        @JsValue("deviceOrientationStarted")
        val deviceOrientationStarted: EventType<VoidFunction>

        /**
         * Occurs when device orientation tracking has stopped.
         *
         * *eventHandler* receives no parameters.
         *
         * @since Bot API 8.0
         * @see useDeviceOrientation
         */
        @JsValue("deviceOrientationStopped")
        val deviceOrientationStopped: EventType<VoidFunction>

        /**
         * Occurs with the specified frequency after calling the start method, sending the current orientation data.
         *
         * *eventHandler* receives no parameters. The current device orientation values can be received via
         * [WebApp.DeviceOrientation.alpha][DeviceOrientation.alpha],
         * [WebApp.DeviceOrientation.beta][DeviceOrientation.beta], and
         * [WebApp.DeviceOrientation.gamma][DeviceOrientation.gamma] respectively.
         *
         * @since Bot API 8.0
         * @see useDeviceOrientation
         */
        @JsValue("deviceOrientationChanged")
        val deviceOrientationChanged: EventType<VoidFunction>

        /**
         * Occurs if a request to start device orientation tracking fails.
         *
         * *eventHandler* receives an [DeviceOrientationFailed] object.
         *
         * @since Bot API 8.0
         * @see useDeviceOrientation
         */
        @JsValue("deviceOrientationFailed")
        val deviceOrientationFailed: EventType<(DeviceOrientationFailed) -> Unit>

        /**
         * Occurs when gyroscope tracking has started successfully.
         *
         * *eventHandler* receives no parameters.
         *
         * @since Bot API 8.0
         * @see useGyroscope
         */
        @JsValue("gyroscopeStarted")
        val gyroscopeStarted: EventType<VoidFunction>

        /**
         * Occurs when gyroscope tracking has stopped.
         *
         * *eventHandler* receives no parameters.
         *
         * @since Bot API 8.0
         * @see useGyroscope
         */
        @JsValue("gyroscopeStopped")
        val gyroscopeStopped: EventType<VoidFunction>

        /**
         * Occurs with the specified frequency after calling the start method, sending the current gyroscope data.
         *
         * *eventHandler* receives no parameters. The current rotation rates can be received via
         * [WebApp.Gyroscope.x][Gyroscope.x], [WebApp.Gyroscope.y][Gyroscope.y], and [WebApp.Gyroscope.z][Gyroscope.z]
         * respectively.
         *
         * @since Bot API 8.0
         * @see useGyroscope
         */
        @JsValue("gyroscopeChanged")
        val gyroscopeChanged: EventType<VoidFunction>

        /**
         * Occurs if a request to start gyroscope tracking fails.
         *
         * *eventHandler* receives an [GyroscopeFailed] object.
         *
         * @since Bot API 8.0
         * @see useGyroscope
         */
        @JsValue("gyroscopeFailed")
        val gyroscopeFailed: EventType<(GyroscopeFailed) -> Unit>

        /**
         * Occurs whenever LocationManager object is changed.
         *
         * *eventHandler* receives no parameters.
         *
         * @since Bot API 8.0
         * @see useLocationManager
         */
        @JsValue("locationManagerUpdated")
        val locationManagerUpdated: EventType<VoidFunction>

        /**
         * Occurs when location data is requested.
         *
         * *eventHandler* receives an [LocationRequested] object.
         *
         * @since Bot API 8.0
         * @see useLocationManager
         */
        @JsValue("locationRequested")
        val locationRequested: EventType<(LocationRequested) -> Unit>

        /**
         * Occurs when the message is successfully shared by the user.
         *
         * *eventHandler* receives no parameters.
         *
         * @since Bot API 8.0
         */
        @JsValue("shareMessageSent")
        val shareMessageSent: EventType<VoidFunction>

        /**
         * Occurs if sharing the message fails.
         *
         * *eventHandler* receives an [ShareMessageFailed] object.
         *
         * @since Bot API 8.0
         */
        @JsValue("shareMessageFailed")
        val shareMessageFailed: EventType<VoidFunction>

        /**
         * Occurs when the emoji status is successfully set.
         *
         * *eventHandler* receives no parameters.
         *
         * @since Bot API 8.0
         */
        @JsValue("emojiStatusSet")
        val emojiStatusSet: EventType<VoidFunction>

        /**
         * Occurs if setting the emoji status fails.
         *
         * *eventHandler* receives an [EmojiStatusFailed] object.
         *
         * @since Bot API 8.0
         */
        @JsValue("emojiStatusFailed")
        val emojiStatusFailed: EventType<(EmojiStatusFailed) -> Unit>

        /**
         * Occurs when the write permission was requested.
         *
         * *eventHandler* receives an [EmojiStatusAccessRequested] object.
         *
         * @since Bot API 8.0
         */
        @JsValue("emojiStatusAccessRequested")
        val emojiStatusAccessRequested: EventType<(EmojiStatusAccessRequested) -> Unit>

        /**
         * Occurs when the user responds to the file download request.
         *
         * *eventHandler* receives an [FileDownloadRequested] object.
         *
         * @since Bot API 8.0
         */
        @JsValue("fileDownloadRequested")
        val fileDownloadRequested: EventType<VoidFunction>
    }
}
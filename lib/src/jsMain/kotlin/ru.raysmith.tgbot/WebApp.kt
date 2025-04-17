package ru.raysmith.tgbot

import ru.raysmith.tgbot.events.EventType
import ru.raysmith.tgbot.events.InvoiceClosedStatus
import ru.raysmith.tgbot.events.ViewportChanged
import web.cssom.Color
import web.scheduling.VoidFunction

external object WebApp {
    /**
     * A string with raw data transferred to the Mini App.
     *
     * WARNING: [Validate data](https://core.telegram.org/bots/webapps#validating-data-received-via-the-mini-app)
     * before using it on the bot's server.
     */
    val initData: String

    /**
     * An object with input data transferred to the Mini App.
     *
     * WARNING: Data from this field should not be trusted. You should only use data from [initData] on the bot's
     * server and only after it has been
     * [validated](https://core.telegram.org/bots/webapps#validating-data-received-via-the-mini-app).
     * */
    val initDataUnsafe: WebAppInitData

    /** The version of the Bot API available in the user's Telegram app. */
    val version: String

    /** The name of the platform of the user's Telegram app. */
    val platform: String

    /**
     * The color scheme currently used in the Telegram app. Also available as the CSS variable `var(--tg-color-scheme)`.
     * */
    val colorScheme: ColorScheme

    /** An object containing the current theme settings used in the Telegram app. */
    val themeParams: ThemeParams

    /**
     * *True*, if the Mini App is currently active. *False*, if the Mini App is minimized.
     * @since Bot API 8.0
     * */
    val isActive: Boolean

    /**
     * *True*, if the Mini App is expanded to the maximum available height. False, if the Mini App occupies part of
     * the screen and can be expanded to the full height using the [expand] method.
     * */
    val isExpanded: Boolean

    /**
     * The current height of the visible area of the Mini App. Also available in CSS as the variable
     * [CssVar.tgViewportHeight].
     *
     * The application can display just the top part of the Mini App, with its lower part remaining outside the screen
     * area. From this position, the user can “pull” the Mini App to its maximum height, while the bot can do the same
     * by calling the [expand] method. As the position of the Mini App changes, the current height value of the
     * visible area will be updated in real time.
     *
     * Please note that the refresh rate of this value is not sufficient to smoothly follow the lower border of the
     * window. It should not be used to pin interface elements to the bottom of the visible area. It's more appropriate
     * to use the value of the [viewportStableHeight] field for this purpose.
     * */
    val viewportHeight: Float

    /**
     * The height of the visible area of the Mini App in its last stable state. Also available in CSS as a variable
     * [CssVar.tgViewportStableHeight].
     *
     * The application can display just the top part of the Mini App, with its lower part remaining outside the screen
     * area. From this position, the user can “pull” the Mini App to its maximum height, while the bot can do the same
     * by calling the [expand] method. Unlike the value of [viewportHeight], the value of [viewportStableHeight]
     * does not change as the position of the Mini App changes with user gestures or during animations. The value of
     * [viewportStableHeight] will be updated after all gestures and animations are completed and the Mini App reaches
     * its final size.
     *
     * *Note the event [EventType.viewportChanged] with the passed parameter
     * [isStateStable][ViewportChanged.isStateStable]`=true`, which will allow you to track when the stable state of
     * the height of the visible area changes.
     * */
    val viewportStableHeight: Float

    /** Current header color in the `#RRGGBB` format. */
    val headerColor: Color

    /** Current background color in the `#RRGGBB` format. */
    val backgroundColor: Color

    /** Current bottom bar color in the `#RRGGBB` format. */
    val bottomBarColor: Color

    /**
     * *True*, if the confirmation dialog is enabled while the user is trying to close the Mini App.
     * *False*, if the confirmation dialog is disabled.
     * */
    val isClosingConfirmationEnabled: Boolean

    /**
     * *True*, if vertical swipes to close or minimize the Mini App are enabled. False, if vertical swipes to close or
     * minimize the Mini App are disabled. In any case, the user will still be able to minimize and close the
     * Mini App by swiping the Mini App's header.
     * */
    val isVerticalSwipesEnabled: Boolean

    /**
     * *True*, if the Mini App is currently being displayed in fullscreen mode.
     * */
    val isFullscreen: Boolean

    /**
     * *True*, if the Mini App’s orientation is currently locked.
     * *False*, if orientation changes freely based on the device’s rotation.
     * */
    val isOrientationLocked : Boolean

    /**
     * An object representing the device's safe area insets, accounting for system UI elements like notches or
     * navigation bars.
     * */
    val safeAreaInset: SafeAreaInset

    /**
     * An object representing the safe area for displaying content within the app, free from overlapping
     * Telegram UI elements.
     * */
    val contentSafeAreaInset: ContentSafeAreaInset

    /**
     * An object for controlling the back button which can be displayed in the header of the Mini App in the
     * Telegram interface.
     * */
    val BackButton: BackButton

    /**
     * An object for controlling the main button, which is displayed at the bottom of the Mini App in the
     * Telegram interface.
     * */
    val MainButton: BottomButton

    /**
     * An object for controlling the secondary button, which is displayed at the bottom of the Mini App in the
     * Telegram interface.
     * */
    val SecondaryButton: BottomButton

    /**
     * An object for controlling the Settings item in the context menu of the Mini App in the Telegram interface.
     * */
    val SettingsButton: SettingsButton

    /** An object for controlling haptic feedback. */
    val HapticFeedback: HapticFeedback

    /** An object for controlling cloud storage. */
    val CloudStorage: CloudStorage

    /** An object for controlling biometrics on the device. */
    val BiometricManager: BiometricManager

    /** An object for accessing accelerometer data on the device. */
    val Accelerometer: Accelerometer

    /** An object for accessing device orientation data on the device. */
    val DeviceOrientation: DeviceOrientation

    /** An object for accessing gyroscope data on the device. */
    val Gyroscope: Gyroscope

    /** An object for controlling location on the device. */    
    val LocationManager: LocationManager

    /**
     * Returns true if the user's app supports a version of the Bot API that is equal to or higher than the version
     * passed as the parameter.
     * */
    fun isVersionAtLeast(version: String): Boolean

    /**
     * A method that sets the app header color in the `#RRGGBB` format. You can also use keywords *bg_color* and
     * *secondary_bg_color*.
     *
     * Up to `Bot API 6.9` You can only pass [Telegram.WebApp.ThemeParams.bgColor][ThemeParams.bgColor] or
     * [Telegram.WebApp.ThemeParams.secondaryBgColor][ThemeParams.secondaryBgColor] as a color or *bg_color*,
     * *secondary_bg_color* keywords.
     *
     * @since Bot API 6.1
     * */
    fun setHeaderColor(color: Color)

    /**
     * A method that sets the app background color in the `#RRGGBB` format. You can also use keywords *bg_color* and
     * *secondary_bg_color*.
     *
     * @since Bot API 6.1
     * */
    fun setBackgroundColor(color: Color)

    /**
     * A method that sets the app's bottom bar color in the `#RRGGBB` format. You can also use the keywords *bg_color*,
     * *secondary_bg_color* and *bottom_bar_bg_color*. This color is also applied to the navigation bar on Android.
     *
     * @since Bot API 7.10
     * */
    fun setBottomBarColor(color: Color)

    /**
     * A method that enables a confirmation dialog while the user is trying to close the Mini App.
     *
     * @since Bot API 6.2
     * */
    fun enableClosingConfirmation()

    /**
     * A method that disables the confirmation dialog while the user is trying to close the Mini App.
     *
     * @since Bot API 6.2
     * */
    fun disableClosingConfirmation()

    /**
     * A method that enables vertical swipes to close or minimize the Mini App. For user convenience, it is recommended
     * to always enable swipes unless they conflict with the Mini App's own gestures.
     *
     * @since Bot API 7.7
     * */
    val enableVerticalSwipes: VoidFunction

    /**
     * A method that disables vertical swipes to close or minimize the Mini App. This method is useful if your Mini App
     * uses swipe gestures that may conflict with the gestures for minimizing and closing the app.
     *
     * @since Bot API 7.7
     * */
    val disableVerticalSwipes: VoidFunction

    /**
     * A method that requests opening the Mini App in fullscreen mode. Although the header is transparent in fullscreen
     * mode, it is recommended that the Mini App sets the header color using the setHeaderColor method.
     * This color helps determine a contrasting color for the status bar and other UI controls.
     *
     * @since Bot API 8.0
     * */
    val requestFullscreen: VoidFunction

    /**
     * A method that requests exiting fullscreen mode.
     *
     * @since Bot API 8.0
     * */
    val exitFullscreen: VoidFunction

    /**
     * A method that locks the Mini App’s orientation to its current mode (either portrait or landscape). Once locked,
     * the orientation remains fixed, regardless of device rotation. This is useful if a stable orientation is needed
     * during specific interactions.
     *
     * @since Bot API 8.0
     * */
    val lockOrientation: VoidFunction

    /**
     * A method that unlocks the Mini App’s orientation, allowing it to follow the device's rotation freely. Use this to
     * restore automatic orientation adjustments based on the device orientation.
     *
     * @since Bot API 8.0
     * */
    val unlockOrientation: VoidFunction

    /**
     * A method that prompts the user to add the Mini App to the home screen. After successfully adding the icon, the
     * [EventType.homeScreenAdded] event will be triggered if supported by the device. Note that if the device cannot
     * determine the installation status, the event may not be received even if the icon has been added.
     *
     * @since Bot API 8.0
     * */
    val addToHomeScreen: VoidFunction

    /**
     * A method that checks if adding to the home screen is supported and if the Mini App has already been added.
     * If an optional callback parameter is provided, the callback function will be called with a single argument
     * status, which is a string indicating the home screen status. Possible values for status are:
     * - [HomeScreenStatus.unsupported] – the feature is not supported, and it is not possible to add the icon to the
     * home screen,
     * - [HomeScreenStatus.unknown] – the feature is supported, and the icon can be added, but it is not possible to
     * determine if the icon has already been added,
     * - [HomeScreenStatus.added] – the icon has already been added to the home screen,
     * - [HomeScreenStatus.missed] – the icon has not been added to the home screen.
     *
     * @since Bot API 8.0
     * */
    fun checkHomeScreenStatus(callback: (status: HomeScreenStatus) -> Unit = definedExternally)

    /** A method that sets the app event handler. */
    fun <T> onEvent(eventType: EventType<T>, eventHandler: T)

    /** A method that deletes a previously set event handler. */
    fun <T> offEvent(eventType: EventType<T>, eventHandler: T)

    /**
     * A method used to send data to the bot. When this method is called, a service message is sent to the bot
     * containing the [data] of the length up to 4096 bytes, and the Mini App is closed.
     * See the field `webAppData` in the class [Message](https://core.telegram.org/bots/api#message).
     *
     * *This method is only available for Mini Apps launched via a
     * [Keyboard button](https://core.telegram.org/bots/webapps#keyboard-button-mini-apps).*
     * */
    fun sendData(data: String)

    /**
     * A method that inserts the bot's username and the specified inline [query] in the current chat's
     * input field. Query may be empty, in which case only the bot's username will be inserted.
     * If an optional [chooseChatTypes] parameter was passed, the client prompts the user to choose a specific chat,
     * then opens that chat and inserts the bot's username and the specified inline [query] in the input field.
     * You can specify which types of chats the user will be able to choose from. It can be one or more of the
     * following types: [InlineQueryChatType.users], [InlineQueryChatType.bots], [InlineQueryChatType.bots],
     * [InlineQueryChatType.channels].
     *
     * @since Bot API 6.7
     * */
    fun switchInlineQuery(query: String, chooseChatTypes: Array<InlineQueryChatType> = definedExternally)

    /**
     * A method that opens a link in an external browser. The Mini App will *not* be closed.
     *
     * `Bot API 6.4+` If the optional options parameter is passed with the field [OpenLinkOptions.tryInstantView]=true,
     * the link will be opened in [Instant View](https://instantview.telegram.org/) mode if possible.
     *
     * *Note that this method can be called only in response to user interaction with the Mini App interface
     * (e.g. a click inside the Mini App or on the main button)*
     * */
    fun openLink(url: String, options: OpenLinkOptions = definedExternally)

    /**
     * A method that opens a telegram link inside the Telegram app.
     * The Mini App will not be closed after this method is called.
     *
     * Up to `Bot API 7.0` The Mini App *will* be closed after this method is called.
     * */
    fun openTelegramLink(url: String)

    /**
     * A method that opens an invoice using the link url. The Mini App will receive the event [EventType.invoiceClosed]
     * when the invoice is closed. If an optional [callback] parameter was passed, the [callback] function will be
     * called and the invoice status will be passed as the first argument.
     *
     * @since Bot API 6.1
     * */
    fun openInvoice(url: String, callback: (status: InvoiceClosedStatus) -> Unit = definedExternally)

    /**
     * A method that opens the native story editor with the media specified in the mediaUrl parameter as an HTTPS URL.
     * An optional params argument of the type [StoryShareParams] describes additional sharing settings.
     *
     * @since Bot API 7.8
     * */
    fun shareToStory(mediaUrl: String, params: StoryShareParams)

    /**
     * A method that opens a dialog allowing the user to share a message provided by the bot.
     * If an optional [callback] parameter is provided, the [callback] function will be called with a boolean as the
     * first argument, indicating whether the message was successfully sent. The message id passed to this method must
     * belong to a [PreparedInlineMessage](https://core.telegram.org/bots/api#preparedinlinemessage) previously obtained
     * via the Bot API method [savePreparedInlineMessage](https://core.telegram.org/bots/api#savepreparedinlinemessage).
     *
     * @since Bot API 8.0
     * */
    fun shareMessage(msgId: String, callback: (sent: Boolean) -> Unit = definedExternally)

    /**
     * A method that opens a dialog allowing the user to set the specified custom emoji as their status.
     * An optional params argument of type EmojiStatusParams specifies additional settings, such as duration.
     * If an optional [callback] parameter is provided, the [callback] function will be called with a boolean as the
     * first argument, indicating whether the status was set.
     *
     * Note: this method opens a native dialog and cannot be used to set the emoji status without manual user
     * interaction. For fully programmatic changes, you should instead use the Bot API method setUserEmojiStatus after
     * obtaining authorization to do so via the Mini App method [requestEmojiStatusAccess].
     *
     * @since Bot API 8.0
     * */
    fun setEmojiStatus(
        customEmojiId: String, params: EmojiStatusParams, callback: (isStatusSet: Boolean) -> Unit = definedExternally
    )

    /**
     * A method that shows a native popup requesting permission for the bot to manage user's emoji status.
     * If an optional [callback] parameter was passed, the [callback] function will be called when the popup is closed
     * and the first argument will be a boolean indicating whether the user granted this access.
     *
     * @since Bot API 8.0
     * */
    fun requestEmojiStatusAccess(callback: (granted: Boolean) -> Unit = definedExternally)

    /**
     * A method that displays a native popup prompting the user to download a file specified by the params argument of
     * type [DownloadFileParams]. If an optional [callback] parameter is provided, the [callback] function will be
     * called when the popup is closed, with the first argument as a boolean indicating whether the user accepted the
     * download request.
     *
     * @since Bot API 8.0
     * */
    fun downloadFile(params: DownloadFileParams, callback: (granted: Boolean) -> Unit = definedExternally)

    /**
     * A method that shows a native popup described by the *params* argument of the type [PopupParams].
     * The Mini App will receive the event [EventType.popupClosed] when the popup is closed. If an optional [callback]
     * parameter was passed, the [callback] function will be called and the field id of the pressed button will be
     * passed as the first argument.
     *
     * @since Bot API 6.2
     * */
    fun showPopup(params: PopupParams, callback: (buttonId: String?) -> Unit = definedExternally)

    /**
     * A method that shows [message] in a simple alert with a 'Close' button. If an optional [callback] parameter was
     * passed, the [callback] function will be called when the popup is closed.
     *
     * @since Bot API 6.2
     * */
    fun showAlert(message: String, callback: VoidFunction = definedExternally)

    /**
     * A method that shows [message] in a simple confirmation window with 'OK' and 'Cancel' buttons. If an optional
     * [callback] parameter was passed, the [callback] function will be called when the popup is closed and the first
     * argument will be a boolean indicating whether the user pressed the 'OK' button.
     *
     * @since Bot API 6.2
     * */
    fun showConfirm(message: String, callback: (isOkPressed: Boolean) -> Unit = definedExternally)

    /**
     * A method that shows a native popup for scanning a QR code described by the `params` argument of the type
     * [ScanQrPopupParams]. The Mini App will receive the event [EventType.qrTextReceived] every time the scanner
     * catches a code with text data. If an optional [callback] parameter was passed, the [callback] function will be
     * called and the text from the QR code will be passed as the first argument. Returning *true* inside this
     * [callback] function causes the popup to be closed.
     *
     * Starting from `Bot API 7.7`, the Mini App will receive the [EventType.scanQrPopupClosed] event
     * if the user closes the native popup for scanning a QR code.
     *
     * @since Bot API 6.4
     * */
    fun showScanQrPopup(params: ScanQrPopupParams, callback: (String) -> Boolean = definedExternally)

    /**
     * A method that closes the native popup for scanning a QR code opened with the [showScanQrPopup] method.
     * Run it if you received valid data in the event [EventType.qrTextReceived].
     *
     * @since Bot API 6.4
     * */
    fun closeScanQrPopup()

    /**
     * A method that requests text from the clipboard. The Mini App will receive the event
     * [EventType.clipboardTextReceived]. If an optional [callback] parameter was passed, the [callback] function will
     * be called and the text from the clipboard will be passed as the first argument.
     *
     * *Note: this method can be called only for Mini Apps launched from the attachment menu and only in response to
     * a user interaction with the Mini App interface (e.g. a click inside the Mini App or on the main button).*
     * @since Bot API 6.4
     * */
    fun readTextFromClipboard(callback: (String) -> Unit = definedExternally)

    /**
     * A method that shows a native popup requesting permission for the bot to send messages to the user.
     * If an optional [callback] parameter was passed, the [callback] function will be called when the popup is closed
     * and the first argument will be a boolean indicating whether the user granted this access.
     *
     * @since Bot API 6.9
     * */
    fun requestWriteAccess(callback: (granted: Boolean) -> Unit = definedExternally)

    /**
     * A method that shows a native popup prompting the user for their phone number. If an optional [callback] parameter
     * was passed, the [callback] function will be called when the popup is closed and the first argument will be a
     * boolean indicating whether the user shared its phone number.
     *
     * @since Bot API 6.9
     * */
    fun requestContact(callback: (isUserSharedItsPhoneNumber: Boolean) -> Unit = definedExternally)

    /**
     * A method that informs the Telegram app that the Mini App is ready to be displayed.
     *
     * It is recommended to call this method as early as possible, as soon as all essential interface elements are
     * loaded. Once this method is called, the loading placeholder is hidden and the Mini App is shown.
     *
     * If the method is not called, the placeholder will be hidden only when the page is fully loaded.
     * */
    fun ready()

    /**
     * A method that expands the Mini App to the maximum available height. To find out if the Mini App is expanded to
     * the maximum height, refer to the value of the [Telegram.WebApp.isExpanded][WebApp.isExpanded] parameter
     * */
    fun expand()

    /** A method that closes the Mini App. */
    fun close()
}
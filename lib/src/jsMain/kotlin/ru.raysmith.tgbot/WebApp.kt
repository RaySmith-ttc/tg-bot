package ru.raysmith.tgbot

import js.objects.jso
import web.scheduling.VoidFunction

external interface WebAppProps {
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

//    /** An object containing the current theme settings used in the Telegram app. */
    val themeParams: ThemeParams

    // TODO link to expand
    /**
     * *True*, if the Mini App is expanded to the maximum available height. False, if the Mini App occupies part of
     * the screen and can be expanded to the full height using the **expand()** method.
     * */
    val isExpanded: Boolean

    // TODO link to expand
    /**
     * The current height of the visible area of the Mini App. Also available in CSS as the variable
     * [CssVar.tgViewportHeight].
     *
     * The application can display just the top part of the Mini App, with its lower part remaining outside the screen
     * area. From this position, the user can “pull” the Mini App to its maximum height, while the bot can do the same
     * by calling the **expand()** method. As the position of the Mini App changes, the current height value of the
     * visible area will be updated in real time.
     *
     * Please note that the refresh rate of this value is not sufficient to smoothly follow the lower border of the
     * window. It should not be used to pin interface elements to the bottom of the visible area. It's more appropriate
     * to use the value of the `viewportStableHeight` field for this purpose.
     * */
    val viewportHeight: Float

    // TODO link to expand, event
    /**
     * The height of the visible area of the Mini App in its last stable state. Also available in CSS as a variable
     * [CssVar.tgViewportStableHeight].
     *
     * The application can display just the top part of the Mini App, with its lower part remaining outside the screen
     * area. From this position, the user can “pull” the Mini App to its maximum height, while the bot can do the same
     * by calling the **expand()** method. Unlike the value of `viewportHeight`, the value of `viewportStableHeight`
     * does not change as the position of the Mini App changes with user gestures or during animations. The value of
     * `viewportStableHeight` will be updated after all gestures and animations are completed and the Mini App reaches
     * its final size.
     *
     * *Note the event `viewportChanged` with the passed parameter `isStateStable=true`, which will allow you to track
     * when the stable state of the height of the visible area changes.
     * */
    val viewportStableHeight: Float

    /** Current header color in the `#RRGGBB` format. */
    val headerColor: String

    /** Current background color in the `#RRGGBB` format. */
    val backgroundColor: String

    /** Current bottom bar color in the `#RRGGBB` format. */
    val bottomBarColor: String

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

    // TODO model
    /** An object for controlling biometrics on the device. */
    val BiometricManager: BiometricManager

    /**
     * Returns true if the user's app supports a version of the Bot API that is equal to or higher than the version
     * passed as the parameter.
     * */
    val isVersionAtLeast: (version: String) -> Boolean // TODO type version

    /**
     * A method that sets the app header color in the `#RRGGBB` format. You can also use keywords *bg_color* and
     * *secondary_bg_color*.
     *
     * Up to `Bot API 6.9` You can only pass *Telegram.WebApp.themeParams.bg_color* or
     * *Telegram.WebApp.themeParams.secondary_bg_color* as a color or *bg_color*, *secondary_bg_color* keywords.
     *
     * @since Bot API 6.1
     * */
    val setHeaderColor: (color: dynamic) -> Boolean // TODO type color ('bg_color' | 'secondary_bg_color' | Hex (#fff|#fffff|rgb(255,255,255)))

    /**
     * A method that sets the app background color in the `#RRGGBB` format. You can also use keywords *bg_color* and
     * *secondary_bg_color*.
     *
     * @since Bot API 6.1
     * */
    val setBackgroundColor: (color: dynamic) -> Boolean // TODO type color ('bg_color' | 'secondary_bg_color' | Hex (#fff|#fffff|rgb(255,255,255)))

    /**
     * A method that sets the app's bottom bar color in the `#RRGGBB` format. You can also use the keywords *bg_color*,
     * *secondary_bg_color* and *bottom_bar_bg_color*.
     *
     * @since Bot API 7.10
     * */
    val setBottomBarColor: (color: dynamic) -> Boolean // TODO type color ('bg_color' | 'secondary_bg_color' | 'bottom_bar_bg_color' | Hex (#fff|#fffff|rgb(255,255,255))) !

    /**
     * A method that enables a confirmation dialog while the user is trying to close the Mini App.
     *
     * @since Bot API 6.2
     * */
    val enableClosingConfirmation: VoidFunction

    /**
     * A method that disables the confirmation dialog while the user is trying to close the Mini App.
     *
     * @since Bot API 6.2
     * */
    val disableClosingConfirmation: VoidFunction

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

    /** A method that sets the app event handler. */
    val onEvent: (eventType: EventType, eventHandler: (dynamic) -> Unit) -> Unit

    /** A method that deletes a previously set event handler. */
    val offEvent: (eventType: EventType, eventHandler: (dynamic) -> Unit) -> Unit

    /**
     * A method used to send data to the bot. When this method is called, a service message is sent to the bot
     * containing the data *data* of the length up to 4096 bytes, and the Mini App is closed.
     * See the field *web_app_data* in the class [Message](https://core.telegram.org/bots/api#message).
     *
     * *This method is only available for Mini Apps launched via a
     * [Keyboard button](https://core.telegram.org/bots/webapps#keyboard-button-mini-apps).*
     * */
    val sendData: (data: String) -> Unit

    /**
     * A method that inserts the bot's username and the specified inline query in the current chat's
     * input field. Query may be empty, in which case only the bot's username will be inserted.
     * If an optional *chooseChatTypes* parameter was passed, the client prompts the user to choose a specific chat,
     * then opens that chat and inserts the bot's username and the specified inline query in the input field.
     * You can specify which types of chats the user will be able to choose from. It can be one or more of the
     * following types: *users*, *bots*, *groups*, *channels*.
     *
     * @since Bot API 6.7
     * */
    val switchInlineQuery: (query: String, chooseChatTypes: List<ChatType>) -> Unit

    /**
     * A method that opens a link in an external browser. The Mini App will *not* be closed.
     * `Bot API 6.4+` If the optional options parameter is passed with the field [OpenLinkOptions.tryInstantView]=true,
     * the link will be opened in [Instant View](https://instantview.telegram.org/) mode if possible.
     *
     * *Note that this method can be called only in response to user interaction with the Mini App interface
     * (e.g. a click inside the Mini App or on the main button)*
     * */
    val openLink: (url: String, options: OpenLinkOptions) -> Unit

    /**
     * A method that opens a telegram link inside the Telegram app.
     * The Mini App will not be closed after this method is called.
     *
     * Up to `Bot API 7.0` The Mini App *will* be closed after this method is called.
     * */
    val openTelegramLink: (url: String) -> Unit

    // TODO link to event
    /**
     * A method that opens an invoice using the link url. The Mini App will receive the event
     * *invoiceClosed* when the invoice is closed. If an optional *callback* parameter was passed, the *callback*
     * function will be called and the invoice status will be passed as the first argument.
     *
     * @since Bot API 6.1
     * */
    val openInvoice: (url: String, callback: VoidFunction) -> Unit

    /**
     * A method that opens the native story editor with the media specified in the mediaUrl parameter as an HTTPS URL.
     * An optional params argument of the type [StoryShareParams] describes additional sharing settings.
     *
     * @since Bot API 7.8
     * */
    val shareToStory: (mediaUrl: String, params: StoryShareParams) -> Unit

    // TODO link to event
    /**
     * A method that shows a native popup described by the *params* argument of the type [PopupParams].
     * The Mini App will receive the event *popupClosed* when the popup is closed. If an optional *callback* parameter
     * was passed, the *callback* function will be called and the field id of the pressed button will be passed as
     * the first argument.
     *
     * @since Bot API 6.2
     * */
    val showPopup: (params: PopupParams, callback: VoidFunction) -> Unit

    /**
     * A method that shows *message* in a simple alert with a 'Close' button. If an optional *callback* parameter was
     * passed, the *callback* function will be called when the popup is closed.
     *
     * @since Bot API 6.2
     * */
    val showAlert: (message: String, callback: VoidFunction) -> Unit

    /**
     * A method that shows *message* in a simple confirmation window with 'OK' and 'Cancel' buttons. If an optional
     * *callback* parameter was passed, the *callback* function will be called when the popup is closed and the first
     * argument will be a boolean indicating whether the user pressed the 'OK' button.
     *
     * @since Bot API 6.2
     * */
    val showConfirm: (message: String, callback: VoidFunction) -> Unit

    // TODO link to event
    /**
     * A method that shows a native popup for scanning a QR code described by the *params* argument of the type
     * [ScanQrPopupParams]. The Mini App will receive the event *qrTextReceived* every time the scanner catches a code
     * with text data. If an optional *callback* parameter was passed, the *callback* function will be called and the
     * text from the QR code will be passed as the first argument. Returning *true* inside this callback function
     * causes the popup to be closed.
     *
     * Starting from `Bot API 7.7`, the Mini App will receive the [scanQrPopupClosed][EventType.scanQrPopupClosed] event
     * if the user closes the native popup for scanning a QR code.
     *
     * @since Bot API 6.4
     * */
    val showScanQrPopup: (params: ScanQrPopupParams, callback: VoidFunction) -> Unit

    /**
     * A method that closes the native popup for scanning a QR code opened with the [showScanQrPopup] method.
     * Run it if you received valid data in the event *qrTextReceived*.
     *
     * @since Bot API 6.4
     * */
    val closeScanQrPopup: VoidFunction

    /**
     * A method that requests text from the clipboard. The Mini App will receive the event *clipboardTextReceived*.
     * If an optional *callback* parameter was passed, the *callback* function will be called and the text from the
     * clipboard will be passed as the first argument.
     *
     * *Note: this method can be called only for Mini Apps launched from the attachment menu and only in response to
     * a user interaction with the Mini App interface (e.g. a click inside the Mini App or on the main button).*
     * @since Bot API 6.4
     * */
    val readTextFromClipboard: (callback: VoidFunction) -> Unit

    /**
     * A method that shows a native popup requesting permission for the bot to send messages to the user.
     * If an optional *callback* parameter was passed, the *callback* function will be called when the popup is closed
     * and the first argument will be a boolean indicating whether the user granted this access.
     *
     * @since Bot API 6.9
     * */
    val requestWriteAccess: (callback: VoidFunction) -> Unit

    /**
     * A method that shows a native popup prompting the user for their phone number. If an optional *callback* parameter
     * was passed, the *callback* function will be called when the popup is closed and the first argument will be a
     * boolean indicating whether the user shared its phone number.
     *
     * @since Bot API 6.9
     * */
    val requestContact: (callback: VoidFunction) -> Unit

    /**
     * A method that informs the Telegram app that the Mini App is rea`dy to be displayed.
     *
     * It is recommended to call this method as early as possible, as soon as all essential interface elements are
     * loaded. Once this method is called, the loading placeholder is hidden and the Mini App is shown.
     *
     * If the method is not called, the placeholder will be hidden only when the page is fully loaded.
     * */
    val ready: VoidFunction

    /**
     * A method that expands the Mini App to the maximum available height. To find out if the Mini App is expanded to
     * the maximum height, refer to the value of the [Telegram.WebApp.isExpanded][WebApp.isExpanded] parameter
     * */
    val expand: VoidFunction

    /** A method that closes the Mini App. */
    val close: VoidFunction
}


external object WebApp : WebAppProps {
    override val initData: String
    override val initDataUnsafe: WebAppInitData
    override val version: String
    override val platform: String
    override val colorScheme: ColorScheme
    override val themeParams: ThemeParams
    override val isExpanded: Boolean
    override val viewportHeight: Float
    override val viewportStableHeight: Float
    override val headerColor: String
    override val backgroundColor: String
    override val bottomBarColor: String
    override val isClosingConfirmationEnabled: Boolean
    override val isVerticalSwipesEnabled: Boolean
    override val BackButton: BackButton
    override val MainButton: BottomButton
    override val SecondaryButton: BottomButton
    override val SettingsButton: SettingsButton
    override val HapticFeedback: HapticFeedback
    override val CloudStorage: CloudStorage
    override val BiometricManager: BiometricManager
    override val isVersionAtLeast: (version: String) -> Boolean
    override val setHeaderColor: (color: dynamic) -> Boolean
    override val setBackgroundColor: (color: dynamic) -> Boolean
    override val setBottomBarColor: (color: dynamic) -> Boolean
    override val enableClosingConfirmation: VoidFunction
    override val disableClosingConfirmation: VoidFunction
    override val enableVerticalSwipes: VoidFunction
    override val disableVerticalSwipes: VoidFunction
    override val onEvent: (eventType: EventType, eventHandler: (dynamic) -> Unit) -> Unit
    override val offEvent: (eventType: EventType, eventHandler: (dynamic) -> Unit) -> Unit
    override val sendData: (data: String) -> Unit
    override val switchInlineQuery: (query: String, chooseChatTypes: List<ChatType>) -> Unit
    override val openLink: (url: String, options: OpenLinkOptions) -> Unit
    override val openTelegramLink: (url: String) -> Unit
    override val openInvoice: (url: String, callback: VoidFunction) -> Unit
    override val shareToStory: (mediaUrl: String, params: StoryShareParams) -> Unit
    override val showPopup: (params: PopupParams, callback: VoidFunction) -> Unit
    override val showAlert: (message: String, callback: VoidFunction) -> Unit
    override val showConfirm: (message: String, callback: VoidFunction) -> Unit
    override val showScanQrPopup: (params: ScanQrPopupParams, callback: VoidFunction) -> Unit
    override val closeScanQrPopup: VoidFunction
    override val readTextFromClipboard: (callback: VoidFunction) -> Unit
    override val requestWriteAccess: (callback: VoidFunction) -> Unit
    override val requestContact: (callback: VoidFunction) -> Unit
    override val ready: VoidFunction
    override val expand: VoidFunction
    override val close: VoidFunction
}

/**
 * `Bot API 6.7+` A method that inserts the bot's username and the specified inline query in the current chat's
 * input field. Query may be empty, in which case only the bot's username will be inserted.
 * You can specify which types of chats the user will be able to choose from. It can be one or more of the
 * following types: *users*, *bots*, *groups*, *channels*.
 * */
@Suppress("UnsafeCastFromDynamic")
fun WebApp.switchInlineQuery(query: String) = switchInlineQuery(query, undefined.asDynamic())

/**
 * A method that opens a link in an external browser. The Mini App will *not* be closed.
 * `Bot API 6.4+` If the optional options parameter is passed with the field [OpenLinkOptions.tryInstantView]=true,
 * the link will be opened in [Instant View](https://instantview.telegram.org/) mode if possible.
 *
 * *Note that this method can be called only in response to user interaction with the Mini App interface
 * (e.g. a click inside the Mini App or on the main button)*
 * */
@Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
fun WebApp.openLink(url: String, options: (OpenLinkOptions.() -> Unit)? = undefined) =
    openLink(url, options?.let { jso(it) } ?: undefined.asDynamic() as OpenLinkOptions)

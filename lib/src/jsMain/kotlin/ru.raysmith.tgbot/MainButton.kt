package ru.raysmith.tgbot

import web.scheduling.VoidFunction

/** This object controls the main button, which is displayed at the bottom of the Mini App in the Telegram interface. */
external interface MainButton {

    /** Current button text. Set to *CONTINUE* by default. */
    val text: String

    /** Current button color. Set to [ThemeParams.buttonColor] by default. */
    val color: String

    /** Current button text color. Set to [ThemeParams.buttonTextColor] by default. */
    val textColor: String

    /** Shows whether the button is visible. Set to *false* by default. */
    val isVisible: Boolean

    /** Shows whether the button is active. Set to *true* by default. */
    val isActive: Boolean

    /** *Readonly*. Shows whether the button is displaying a loading indicator. */
    val isProgressVisible: Boolean

    /** A method to set the button text. */
    val setText: (text: String) -> MainButton

    // TODO ? link to mainButtonClicked
    /**
     * A method that sets the button press event handler.
     * An alias for [Telegram.WebApp.onEvent][WebApp.onEvent]`('mainButtonClicked', callback)`
     * */
    val onClick: (callback: VoidFunction) -> MainButton

    // TODO ? link to mainButtonClicked
    /**
     * A method that removes the button press event handler.
     * An alias for [Telegram.WebApp.onEvent][WebApp.onEvent]`('mainButtonClicked', callback)`
     * */
    val offClick: (callback: VoidFunction) -> MainButton

    /**
     * A method to make the button visible.
     *
     * *Note that opening the Mini App from the
     * [attachment menu](https://core.telegram.org/bots/webapps#launching-mini-apps-from-the-attachment-menu) hides the
     * main button until the user interacts with the Mini App interface.*
     * */
    val show: () -> MainButton

    /** A method to hide the button. */
    val hide: () -> MainButton

    /** A method to enable the button. */
    val enable: () -> MainButton

    /** A method to disable the button. */
    val disable: () -> MainButton

    /**
     * A method to show a loading indicator on the button.
     * It is recommended to display loading progress if the action tied to the button may take a long time. By default,
     * the button is disabled while the action is in progress. If the parameter `leaveActive=true` is passed,
     * the button remains enabled.
     * */
    val showProgress: (leaveActive: Boolean) -> MainButton

    /** A method to hide the loading indicator. */
    val hideProgress: () -> MainButton

    /** A method to set the button parameters */
    val setParams: (params: MainButtonParams) -> MainButton
}


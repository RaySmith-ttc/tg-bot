package ru.raysmith.tgbot

import ru.raysmith.tgbot.events.EventType
import web.scheduling.VoidFunction

/** This object controls the main button, which is displayed at the bottom of the Mini App in the Telegram interface. */
external interface BottomButton {

    /**
     * *Readonly*. Type of the button. It can be either main for the [BottomButtonType.main] button
     * or [BottomButtonType.secondary] for the secondary button.
     * */
    val type: BottomButtonType

    /**
     * Current button text. Set to [BottomButtonText.Continue] for the main button and [BottomButtonText.Cancel] for
     * the secondary button by default.
     * */
    val text: BottomButtonText

    /**
     * Current button color. Set to [ThemeParams.buttonColor] for the main button and
     * [ThemeParams.bottomBarBgColor] for the secondary button by default.
     * */
    val color: String

    /** Current button text color. Set to [ThemeParams.buttonTextColor] for the main button and
     * [ThemeParams.buttonColor] for the secondary button by default.
     * */
    val textColor: String

    /** Shows whether the button is visible. Set to *false* by default. */
    val isVisible: Boolean

    /** Shows whether the button is active. Set to *true* by default. */
    val isActive: Boolean

    /**
     * Shows whether the button has a shine effect. Set to false by default.
     *
     * @since Bot API 7.10
     * */
    val hasShineEffect: Boolean

    /**
     * Position of the secondary button. Not defined for the main button. It applies only if both the main and
     * secondary buttons are visible. Set to [BottomButtonPosition.left] by default.
     *
     * @since Bot API 7.10
     * */
    val position: BottomButtonPosition

    /** *Readonly*. Shows whether the button is displaying a loading indicator. */
    val isProgressVisible: Boolean

    /** A method to set the button text. */
    val setText: (text: String) -> BottomButton

    /**
     * A method that sets the button press event handler.
     * An alias for [WebApp.onEvent][WebApp.onEvent]`(`[EventType.mainButtonClicked]`, callback)` or
     *  [WebApp.onEvent][WebApp.onEvent]`(`[EventType.secondaryButtonClicked]`, callback)`
     * */
    val onClick: (callback: VoidFunction) -> BottomButton

    /**
     * A method that removes the button press event handler.
     * An alias for [WebApp.onEvent][WebApp.onEvent]`(`[EventType.mainButtonClicked]`, callback)` or
     * [WebApp.onEvent][WebApp.onEvent]`(`[EventType.secondaryButtonClicked]`, callback)`
     * */
    val offClick: (callback: VoidFunction) -> BottomButton

    /**
     * A method to make the button visible.
     *
     * *Note that opening the Mini App from the
     * [attachment menu](https://core.telegram.org/bots/webapps#launching-mini-apps-from-the-attachment-menu) hides the
     * main button until the user interacts with the Mini App interface.*
     * */
    val show: () -> BottomButton

    /** A method to hide the button. */
    val hide: () -> BottomButton

    /** A method to enable the button. */
    val enable: () -> BottomButton

    /** A method to disable the button. */
    val disable: () -> BottomButton

    /**
     * A method to show a loading indicator on the button.
     * It is recommended to display loading progress if the action tied to the button may take a long time. By default,
     * the button is disabled while the action is in progress. If the parameter `leaveActive=true` is passed,
     * the button remains enabled.
     * */
    val showProgress: (leaveActive: Boolean) -> BottomButton

    /** A method to hide the loading indicator. */
    val hideProgress: () -> BottomButton

    /** A method to set the button parameters */
    val setParams: (params: MainButtonParams) -> BottomButton
}


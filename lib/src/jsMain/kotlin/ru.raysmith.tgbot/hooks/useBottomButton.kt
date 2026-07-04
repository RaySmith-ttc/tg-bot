package ru.raysmith.tgbot.hooks

import kotlinx.js.JsPlainObject
import react.useMemo
import react.useState
import ru.raysmith.tgbot.*
import web.function.VoidFunction

fun useBottomButton(type: BottomButtonType): BottomButtonHookType {
    var bb by useState(when(type) {
        BottomButtonType.main -> webApp.MainButton
        BottomButtonType.secondary -> webApp.SecondaryButton
    })

    val setText = { text: String ->
        bb = copyOf(bb.setText(text))
    }

    val onClick = { callback: VoidFunction ->
        bb = copyOf(bb.onClick(callback))
    }

    val offClick = { callback: VoidFunction ->
        bb = copyOf(bb.offClick(callback))
    }

    val show = {
        bb = copyOf(bb.show())
    }

    val hide = {
        bb = copyOf(bb.hide())
    }

    val enable = {
        bb = copyOf(bb.enable())
    }

    val disable = {
        bb = copyOf(bb.disable())
    }

    val showProgress = { leaveActive: Boolean ->
        bb = copyOf(bb.showProgress(leaveActive))
    }

    val hideProgress = {
        bb = copyOf(bb.hideProgress())
    }

    val setParams = { params: MainButtonParams ->
        bb = copyOf(bb.setParams(params))
    }

    return useMemo(bb) {
        BottomButtonHookType(
            type = bb.type,
            text = bb.text,
            color = bb.color,
            textColor = bb.textColor,
            isVisible = bb.isVisible,
            isActive = bb.isActive,
            hasShineEffect = bb.hasShineEffect,
            position = bb.position,
            isProgressVisible = bb.isProgressVisible,
            setText = setText,
            onClick = onClick,
            offClick = offClick,
            show = show,
            hide = hide,
            enable = enable,
            disable = disable,
            showProgress = showProgress,
            hideProgress = hideProgress,
            setParams = setParams,
        )
    }
}

@JsPlainObject
external interface BottomButtonHookType {

    /**
     * Type of the button. It can be either main for the [BottomButtonType.main] button or [BottomButtonType.secondary]
     * for the secondary button.
     * */
    val type: BottomButtonType

    /**
     * Current button text. It can be [BottomButtonText.Continue] for the main button and [BottomButtonText.Cancel] for
     * the secondary button.
     * */
    val text: BottomButtonText

    /**
     * Current button color. It can be [ThemeParams.buttonColor] for the main button and
     * [ThemeParams.bottomBarBgColor] for the secondary button.
     * */
    val color: String

    /** Current button text color. It can be [ThemeParams.buttonTextColor] for the main button and
     * [ThemeParams.buttonColor] for the secondary button.
     * */
    val textColor: String

    /** Shows whether the button is visible */
    val isVisible: Boolean

    /** Shows whether the button is active */
    val isActive: Boolean

    /** Shows whether the button has a shine effect */
    val hasShineEffect: Boolean

    /**
     * Position of the secondary button. Not defined for the main button. It applies only if both the main and
     * secondary buttons are visible.
     * */
    val position: BottomButtonPosition

    /** Shows whether the button is displaying a loading indicator */
    val isProgressVisible: Boolean

    /** A method to set the button text */
    val setText: (text: String) -> Unit

    /** A method that sets the button press event handler */
    val onClick: (callback: VoidFunction) -> Unit

    /** A method that removes the button press event handler*/
    val offClick: (callback: VoidFunction) -> Unit

    /**
     * A method to make the button visible.
     *
     * *Note that opening the Mini App from the
     * [attachment menu](https://core.telegram.org/bots/webapps#launching-mini-apps-from-the-attachment-menu) hides the
     * main button until the user interacts with the Mini App interface.*
     * */
    val show: VoidFunction

    /** A method to hide the button. */
    val hide: VoidFunction

    /** A method to enable the button. */
    val enable: VoidFunction

    /** A method to disable the button. */
    val disable: VoidFunction

    /**
     * A method to show a loading indicator on the button.
     * It is recommended to display loading progress if the action tied to the button may take a long time. By default,
     * the button is disabled while the action is in progress. If the parameter `leaveActive=true` is passed,
     * the button remains enabled.
     * */
    val showProgress: (leaveActive: Boolean) -> Unit

    /** A method to hide the loading indicator. */
    val hideProgress: VoidFunction

    /** A method to set the button parameters */
    val setParams: (params: MainButtonParams) -> Unit
}

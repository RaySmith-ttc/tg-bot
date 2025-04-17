package ru.raysmith.tgbot.hooks

import js.objects.jso
import react.useMemo
import react.useState
import ru.raysmith.tgbot.*
import web.scheduling.VoidFunction

fun useBottomButton(type: BottomButtonType): BottomButtonHookType {
    var bb by useState(when(type) {
        BottomButtonType.main -> webApp.MainButton
        BottomButtonType.secondary -> webApp.SecondaryButton
        else -> error("Unknown bottom button type: $type")
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
        jso {
            this.type = bb.type
            this.text = bb.text
            this.color = bb.color
            this.textColor = bb.textColor
            this.isVisible = bb.isVisible
            this.isActive = bb.isActive
            this.hasShineEffect = bb.hasShineEffect
            this.position = bb.position
            this.isProgressVisible = bb.isProgressVisible
            this.setText = setText
            this.onClick = onClick
            this.offClick = offClick
            this.show = show
            this.hide = hide
            this.enable = enable
            this.disable = disable
            this.showProgress = showProgress
            this.hideProgress = hideProgress
            this.setParams = setParams
        }
    }
}

external interface BottomButtonHookType {

    /**
     * Type of the button. It can be either main for the [BottomButtonType.main] button or [BottomButtonType.secondary]
     * for the secondary button.
     * */
    var type: BottomButtonType

    /**
     * Current button text. It can be [BottomButtonText.Continue] for the main button and [BottomButtonText.Cancel] for
     * the secondary button.
     * */
    var text: BottomButtonText

    /**
     * Current button color. It can be [ThemeParams.buttonColor] for the main button and
     * [ThemeParams.bottomBarBgColor] for the secondary button.
     * */
    var color: String

    /** Current button text color. It can be [ThemeParams.buttonTextColor] for the main button and
     * [ThemeParams.buttonColor] for the secondary button.
     * */
    var textColor: String

    /** Shows whether the button is visible */
    var isVisible: Boolean

    /** Shows whether the button is active */
    var isActive: Boolean

    /** Shows whether the button has a shine effect */
    var hasShineEffect: Boolean

    /**
     * Position of the secondary button. Not defined for the main button. It applies only if both the main and
     * secondary buttons are visible.
     * */
    var position: BottomButtonPosition

    /** Shows whether the button is displaying a loading indicator */
    var isProgressVisible: Boolean

    /** A method to set the button text */
    var setText: (text: String) -> Unit

    /** A method that sets the button press event handler */
    var onClick: (callback: VoidFunction) -> Unit

    /** A method that removes the button press event handler*/
    var offClick: (callback: VoidFunction) -> Unit

    /**
     * A method to make the button visible.
     *
     * *Note that opening the Mini App from the
     * [attachment menu](https://core.telegram.org/bots/webapps#launching-mini-apps-from-the-attachment-menu) hides the
     * main button until the user interacts with the Mini App interface.*
     * */
    var show: VoidFunction

    /** A method to hide the button. */
    var hide: VoidFunction

    /** A method to enable the button. */
    var enable: VoidFunction

    /** A method to disable the button. */
    var disable: VoidFunction

    /**
     * A method to show a loading indicator on the button.
     * It is recommended to display loading progress if the action tied to the button may take a long time. By default,
     * the button is disabled while the action is in progress. If the parameter `leaveActive=true` is passed,
     * the button remains enabled.
     * */
    var showProgress: (leaveActive: Boolean) -> Unit

    /** A method to hide the loading indicator. */
    var hideProgress: VoidFunction

    /** A method to set the button parameters */
    var setParams: (params: MainButtonParams) -> Unit
}
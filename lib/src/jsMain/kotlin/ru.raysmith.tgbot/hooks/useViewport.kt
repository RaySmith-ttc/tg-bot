package ru.raysmith.tgbot.hooks

import js.objects.jso
import react.useEffectOnce
import react.useMemo
import react.useState
import ru.raysmith.tgbot.*
import ru.raysmith.tgbot.events.ViewportChangedEvent
import web.scheduling.VoidFunction

/**
 * This hook provides information about the current state of the viewport.
 *
 * @see useInsets
 * */
fun useViewport(): ViewportHookType {
    var viewportHeight by useState(webApp.viewportHeight)
    var viewportStableHeight by useState(webApp.viewportStableHeight)
    var isExpanded by useState(webApp.isExpanded)
    var isVerticalSwipesEnabled by useState(webApp.isVerticalSwipesEnabled)
    var isStateStable by useState(true)
    var isFullscreen by useState(webApp.isFullscreen)
    var fullscreenFailed by useState<FullscreenFailedType?>(null)
    var isOrientationLocked by useState(webApp.isOrientationLocked)

    val enableVerticalSwipes = {
        webApp.enableVerticalSwipes()
        isVerticalSwipesEnabled = true
    }

    val disableVerticalSwipes = {
        webApp.disableVerticalSwipes()
        isVerticalSwipesEnabled = false
    }

    val expand = {
        webApp.expand()
    }

    val requestFullscreen = {
        fullscreenFailed = null
        webApp.requestFullscreen()
    }

    val exitFullscreen = {
        webApp.exitFullscreen()
    }

    val lockOrientation = {
        webApp.lockOrientation()
        isOrientationLocked = true
    }

    val unlockOrientation = {
        webApp.unlockOrientation()
        isOrientationLocked = false
    }

    useEffectOnce {
        webApp.onEvent(EventType.viewportChanged) { payload: ViewportChangedEvent ->
            isStateStable = payload.isStateStable
            viewportHeight = webApp.viewportHeight
            viewportStableHeight = webApp.viewportStableHeight
            if (payload.isStateStable) {
                isExpanded = webApp.isExpanded
            }
        }

        webApp.onEvent(EventType.fullscreenChanged) {
            isFullscreen = webApp.isFullscreen
        }

        webApp.onEvent(EventType.fullscreenFailed) { payload: FullscreenFailed ->
            fullscreenFailed = payload.error
        }
    }

    return useMemo(
        viewportHeight,
        viewportStableHeight,
        isExpanded,
        isVerticalSwipesEnabled,
        isStateStable,
        isFullscreen,
        fullscreenFailed,
        isOrientationLocked,
    ) {
        jso {
            this.viewportHeight = viewportHeight
            this.viewportStableHeight = viewportStableHeight
            this.isExpanded = isExpanded
            this.isStateStable = isStateStable
            this.isVerticalSwipesEnabled = isVerticalSwipesEnabled
            this.enableVerticalSwipes = enableVerticalSwipes
            this.disableVerticalSwipes = disableVerticalSwipes
            this.expand = expand
            this.isFullscreen = isFullscreen
            this.fullscreenFailed = fullscreenFailed
            this.requestFullscreen = requestFullscreen
            this.exitFullscreen = exitFullscreen
            this.isOrientationLocked = isOrientationLocked
            this.lockOrientation = lockOrientation
            this.unlockOrientation = unlockOrientation
        }
    }
}

external interface ViewportHookType {

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
    var viewportHeight: Float

    /**
     * The height of the visible area of the Mini App in its last stable state. Also available in CSS as a variable
     * `[CssVar.tgViewportStableHeight].
     *
     * The application can display just the top part of the Mini App, with its lower part remaining outside the screen
     * area. From this position, the user can “pull” the Mini App to its maximum height, while the bot can do the same
     * by calling the [expand] method. Unlike the value of [viewportHeight], the value of [viewportStableHeight]
     * does not change as the position of the Mini App changes with user gestures or during animations. The value of
     * [viewportStableHeight] will be updated after all gestures and animations are completed and the Mini App reaches
     * its final size.
     *
     * *Note the event [EventType.viewportChanged] with the passed parameter `[isStateStable]=true`, which will allow
     * you to track when the stable state of the height of the visible area changes.
     * */
    var viewportStableHeight: Float

    /**
     * *True*, if the Mini App is expanded to the maximum available height. False, if the Mini App occupies part of
     * the screen and can be expanded to the full height using the [expand] method.
     * */
    var isExpanded: Boolean

    /** If true, the resizing of the Mini App is finished. */
    var isStateStable: Boolean

    /**
     * *True*, if vertical swipes to close or minimize the Mini App are enabled. False, if vertical swipes to close or
     * minimize the Mini App are disabled. In any case, the user will still be able to minimize and close the
     * Mini App by swiping the Mini App's header.
     * */
    var isVerticalSwipesEnabled: Boolean

    /**
     * A method that enables vertical swipes to close or minimize the Mini App. For user convenience, it is recommended
     * to always enable swipes unless they conflict with the Mini App's own gestures.
     *
     * @since Bot API 7.7
     * */
    var enableVerticalSwipes: VoidFunction

    /**
     * A method that disables vertical swipes to close or minimize the Mini App. This method is useful if your Mini App
     * uses swipe gestures that may conflict with the gestures for minimizing and closing the app.
     *
     * @since Bot API 7.7
     * */
    var disableVerticalSwipes: VoidFunction

    /**
     * A method that expands the Mini App to the maximum available height. To find out if the Mini App is expanded to
     * the maximum height, refer to the value of the [isExpanded] parameter
     * */
    var expand: VoidFunction

    /**
     * *True*, if the Mini App is currently being displayed in fullscreen mode.
     * */
    var isFullscreen: Boolean

    /**
     * Not null if a request to enter fullscreen mode fails.
     *
     * Possible values:
     * - [FullscreenFailedType.UNSUPPORTED] – Fullscreen mode is not supported on this device or platform.
     * - [FullscreenFailedType.ALREADY_FULLSCREEN] – The Mini App is already in fullscreen mode.
     */
    var fullscreenFailed: FullscreenFailedType?

    /**
     * A method that requests opening the Mini App in fullscreen mode. Although the header is transparent in fullscreen
     * mode, it is recommended that the Mini App sets the header color using the setHeaderColor method.
     * This color helps determine a contrasting color for the status bar and other UI controls.
     *
     * @since Bot API 8.0
     * */
    var requestFullscreen: VoidFunction

    /**
     * A method that requests exiting fullscreen mode.
     *
     * @since Bot API 8.0
     * */
    var exitFullscreen: VoidFunction

    /**
     * *True*, if the Mini App’s orientation is currently locked.
     * *False*, if orientation changes freely based on the device’s rotation.
     * */
    var isOrientationLocked : Boolean

    /**
     * A method that locks the Mini App’s orientation to its current mode (either portrait or landscape). Once locked,
     * the orientation remains fixed, regardless of device rotation. This is useful if a stable orientation is needed
     * during specific interactions.
     *
     * @since Bot API 8.0
     * */
    var lockOrientation: VoidFunction

    /**
     * A method that unlocks the Mini App’s orientation, allowing it to follow the device's rotation freely. Use this to
     * restore automatic orientation adjustments based on the device orientation.
     *
     * @since Bot API 8.0
     * */
    var unlockOrientation: VoidFunction
}
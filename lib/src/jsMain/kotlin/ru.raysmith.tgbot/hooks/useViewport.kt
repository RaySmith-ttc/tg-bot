package ru.raysmith.tgbot.hooks

import js.objects.jso
import react.useEffectOnce
import react.useMemo
import react.useState
import ru.raysmith.tgbot.CssVar
import ru.raysmith.tgbot.EventType
import ru.raysmith.tgbot.WebApp
import ru.raysmith.tgbot.events.ViewportChangedEvent
import ru.raysmith.tgbot.webApp
import web.scheduling.VoidFunction

fun useViewport(): ViewportHookType {
    var viewportHeight by useState(webApp.viewportHeight)
    var viewportStableHeight by useState(webApp.viewportStableHeight)
    var isExpanded by useState(webApp.isExpanded)
    var isVerticalSwipesEnabled by useState(webApp.isVerticalSwipesEnabled)
    var isStateStable by useState(true)

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

    useEffectOnce {
        webApp.onEvent(EventType.viewportChanged) { payload: ViewportChangedEvent ->
            isStateStable = payload.isStateStable
            viewportHeight = webApp.viewportHeight
            viewportStableHeight = webApp.viewportStableHeight
            if (payload.isStateStable) {
                isExpanded = webApp.isExpanded
            }
        }
    }

    return useMemo(
        viewportHeight,
        viewportStableHeight,
        isExpanded,
        isVerticalSwipesEnabled,
        isStateStable
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
        }
    }
}

external interface ViewportHookType {

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
    var viewportHeight: Float

    // TODO link to expand, event
    /**
     * The height of the visible area of the Mini App in its last stable state. Also available in CSS as a variable
     * `[CssVar.tgViewportStableHeight].
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
    var viewportStableHeight: Float

    // TODO link to expand
    /**
     * *True*, if the Mini App is expanded to the maximum available height. False, if the Mini App occupies part of
     * the screen and can be expanded to the full height using the **expand()** method.
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
     * the maximum height, refer to the value of the [Telegram.WebApp.isExpanded][WebApp.isExpanded] parameter
     * */
    var expand: VoidFunction
}
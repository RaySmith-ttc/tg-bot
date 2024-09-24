package ru.raysmith.tgbot.webappapp

import js.objects.jso
import kotlinx.browser.window
import react.FC
import react.PropsWithChildren
import react.create
import react.router.RouterProvider
import react.router.dom.createBrowserRouter
import react.router.useLocation
import react.router.useNavigate
import react.useMemo

val Router = FC<PropsWithChildren> { props ->
    RouterProvider {
        router = createBrowserRouter(arrayOf(
            jso {
                path = Paths.root
                element = RootPage.create()
                children = arrayOf(
                    jso {
                        index = true
                        element = MainPage.create()
                    },
                    jso {
                        path = Paths.biometrical
                        element = BiometricalPage.create()
                    }
                )
            }
        ))
        +props
    }
}

object Paths {
    val root = "/"
    val biometrical = "/biometrical"
}

external interface RouterProps {
    var back: () -> Unit
    var forward: () -> Unit
    var reload: () -> Unit
    var push: (href: String) -> Unit
    var replace: (href: String) -> Unit
}

fun useRouter(): RouterProps {
    val navigate = useNavigate()
    return useMemo(navigate) {
        jso {
            back = { navigate(-1) }
            forward = { navigate(1) }
            reload = { window.location.reload() }
            push = { navigate(it) }
            replace = { navigate(it, jso { replace = true }) }
        }
    }
}

fun usePathname(): String {
    val pathname = useLocation().pathname

    return useMemo(pathname) { pathname }
}
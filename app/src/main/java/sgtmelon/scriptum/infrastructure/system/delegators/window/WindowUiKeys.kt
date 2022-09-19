package sgtmelon.scriptum.infrastructure.system.delegators.window

/**
 * Keys for describe system UI elements for [WindowUiDelegator].
 */
sealed class WindowUiKeys {

    sealed class Background : WindowUiKeys() {
        object Standard : Background()
        object Dark : Background()
    }

    sealed class StatusBar : WindowUiKeys() {
        object Standard : StatusBar()
        object Transparent : StatusBar()
    }

    sealed class Navigation : WindowUiKeys() {
        object Standard : Navigation()
        object RotationCatch : Navigation()
        object Transparent : Navigation()
    }

    sealed class NavDivider : WindowUiKeys() {
        object Standard : NavDivider()
        object RotationCatch : NavDivider()
        object Transparent : NavDivider()
    }
}
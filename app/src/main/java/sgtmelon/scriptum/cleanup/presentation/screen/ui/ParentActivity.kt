package sgtmelon.scriptum.cleanup.presentation.screen.ui

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.IAppActivity
import sgtmelon.scriptum.infrastructure.model.key.Theme
import sgtmelon.scriptum.infrastructure.system.delegators.ToastDelegator
import sgtmelon.scriptum.infrastructure.system.delegators.window.WindowUiDelegator
import sgtmelon.scriptum.infrastructure.system.delegators.window.WindowUiKeys.Background
import sgtmelon.scriptum.infrastructure.system.delegators.window.WindowUiKeys.NavDivider
import sgtmelon.scriptum.infrastructure.system.delegators.window.WindowUiKeys.Navigation
import sgtmelon.scriptum.infrastructure.system.delegators.window.WindowUiKeys.StatusBar

/**
 * Parent class for all activities.
 */
abstract class ParentActivity : AppCompatActivity(), IAppActivity {

    protected val toast = ToastDelegator(lifecycle)

    /** Keys which describes UI look of system window. */
    protected open val background: Background = Background.Standard
    protected open val statusBar: StatusBar = StatusBar.Standard
    protected open val navigation: Navigation = Navigation.Standard
    protected open val navDivider: NavDivider = NavDivider.Standard

    // TODO move this staff inside onCreate
    override fun setupTheme(theme: Theme) {
        Log.i("HERE", "setupTheme: $theme")
        val mode = when (theme) {
            Theme.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
            Theme.DARK -> AppCompatDelegate.MODE_NIGHT_YES
            Theme.SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }

        AppCompatDelegate.setDefaultNightMode(mode)

        val windowUi = WindowUiDelegator(window, background, statusBar, navigation, navDivider)
        windowUi.setup(context = this)
    }
}
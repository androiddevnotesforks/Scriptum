package sgtmelon.scriptum.infrastructure.screen.theme

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.ViewDataBinding
import javax.inject.Inject
import sgtmelon.scriptum.infrastructure.model.key.preference.Theme
import sgtmelon.scriptum.infrastructure.screen.parent.BindingActivity
import sgtmelon.scriptum.infrastructure.system.delegators.window.WindowUiDelegator
import sgtmelon.scriptum.infrastructure.system.delegators.window.WindowUiKeys.Background
import sgtmelon.scriptum.infrastructure.system.delegators.window.WindowUiKeys.NavDivider
import sgtmelon.scriptum.infrastructure.system.delegators.window.WindowUiKeys.Navigation
import sgtmelon.scriptum.infrastructure.system.delegators.window.WindowUiKeys.StatusBar

/**
 * Parent activity class, which work with application theming and system bars.
 */
abstract class ThemeActivity<T : ViewDataBinding> : BindingActivity<T>(),
    ThemeChangeCallback {

    @Inject lateinit var themeViewModel: ThemeViewModel

    /** Keys which describes UI-look of system window. */
    protected open val background: Background = Background.Standard
    protected open val statusBar: StatusBar = StatusBar.Standard
    protected open val navigation: Navigation = Navigation.Standard
    protected open val navDivider: NavDivider = NavDivider.Standard

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupInsets()
        setupView()
        setupDialogs()
        setupObservers()

        checkThemeChange()

        /** Setup this staff after [checkThemeChange] call. */
        val windowUi = WindowUiDelegator(window, background, statusBar, navigation, navDivider)
        windowUi.setup(context = this)
    }

    /** Setup spaces from android bars and other staff for current screen. */
    open fun setupInsets() = Unit

    open fun setupView() = Unit

    /**
     * If some dialogs was displayed before (e.g. before rotation change) need to check it and
     * setup all needed listeners/data
     */
    open fun setupDialogs() = Unit

    open fun setupObservers() = Unit

    override fun checkThemeChange() {
        val mode = when (themeViewModel.theme) {
            Theme.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
            Theme.DARK -> AppCompatDelegate.MODE_NIGHT_YES
            Theme.SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }

        AppCompatDelegate.setDefaultNightMode(mode)
    }
}
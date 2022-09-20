package sgtmelon.scriptum.cleanup.presentation.screen.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import javax.inject.Inject
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.ThemeViewModel
import sgtmelon.scriptum.infrastructure.model.key.Theme
import sgtmelon.scriptum.infrastructure.system.delegators.ToastDelegator
import sgtmelon.scriptum.infrastructure.system.delegators.window.WindowUiDelegator
import sgtmelon.scriptum.infrastructure.system.delegators.window.WindowUiKeys.Background
import sgtmelon.scriptum.infrastructure.system.delegators.window.WindowUiKeys.NavDivider
import sgtmelon.scriptum.infrastructure.system.delegators.window.WindowUiKeys.Navigation
import sgtmelon.scriptum.infrastructure.system.delegators.window.WindowUiKeys.StatusBar

/**
 * Parent activity class, which work with application theming and system bars.
 */
abstract class ThemeActivity : AppCompatActivity() {

    protected val toast = ToastDelegator(lifecycle)

    @Inject internal lateinit var themeViewModel: ThemeViewModel

    /** Keys which describes UI-look of system window. */
    protected open val background: Background = Background.Standard
    protected open val statusBar: StatusBar = StatusBar.Standard
    protected open val navigation: Navigation = Navigation.Standard
    protected open val navDivider: NavDivider = NavDivider.Standard

    protected val fm get() = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject(ScriptumApplication.component)

        setupTheme(themeViewModel.theme)

        /** Setup this staff after [setupTheme]. */
        val windowUi = WindowUiDelegator(window, background, statusBar, navigation, navDivider)
        windowUi.setup(context = this)
    }

    override fun onResume() {
        super.onResume()
        onThemeChange()
    }

    abstract fun inject(component: ScriptumComponent)

    private fun setupTheme(theme: Theme) {
        val mode = when (theme) {
            Theme.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
            Theme.DARK -> AppCompatDelegate.MODE_NIGHT_YES
            Theme.SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }

        AppCompatDelegate.setDefaultNightMode(mode)
    }

    fun onThemeChange() {
        if (!themeViewModel.isThemeChanged()) return

        val intent = intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)

        overridePendingTransition(R.anim.fragment_fade_in, R.anim.fragment_fade_out)
        finish()

        overridePendingTransition(R.anim.fragment_fade_in, R.anim.fragment_fade_out)
        startActivity(intent)
    }
}
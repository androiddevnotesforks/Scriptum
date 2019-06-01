package sgtmelon.scriptum.screen.vm

import android.app.Application
import sgtmelon.scriptum.R
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.screen.callback.AppCallback
import sgtmelon.scriptum.screen.view.AppActivity

/**
 * ViewModel для [AppActivity]
 *
 * @author SerjantArbuz
 */
class AppViewModel(application: Application) : ParentViewModel(application) {

    lateinit var callback: AppCallback

    @Theme private var currentTheme: Int = 0

    fun onSetupTheme() {
        currentTheme = preference.theme

        when (currentTheme) {
            Theme.light -> callback.setTheme(R.style.App_Light_UI)
            Theme.dark -> callback.setTheme(R.style.App_Dark_UI)
        }
    }

    fun isThemeChange() = currentTheme != preference.theme

}
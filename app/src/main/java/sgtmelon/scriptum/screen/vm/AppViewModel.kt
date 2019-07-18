package sgtmelon.scriptum.screen.vm

import android.app.Application
import sgtmelon.scriptum.R
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.screen.callback.IAppActivity
import sgtmelon.scriptum.screen.callback.IAppViewModel
import sgtmelon.scriptum.screen.view.AppActivity

/**
 * ViewModel для [AppActivity]
 *
 * @author SerjantArbuz
 */
class AppViewModel(application: Application) : ParentViewModel(application), IAppViewModel {

    lateinit var callback: IAppActivity

    @Theme private var currentTheme: Int = 0

    override fun onSetup() {
        currentTheme = iPreferenceRepo.theme

        when (currentTheme) {
            Theme.light -> callback.setTheme(R.style.App_Light_UI)
            Theme.dark -> callback.setTheme(R.style.App_Dark_UI)
        }
    }

    override fun isThemeChange() = currentTheme != iPreferenceRepo.theme

}
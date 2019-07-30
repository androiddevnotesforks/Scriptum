package sgtmelon.scriptum.screen.vm

import android.app.Application
import sgtmelon.scriptum.R
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.screen.ui.AppActivity
import sgtmelon.scriptum.screen.ui.callback.IAppActivity
import sgtmelon.scriptum.screen.vm.callback.IAppViewModel

/**
 * ViewModel for [AppActivity]
 *
 * @author SerjantArbuz
 */
class AppViewModel(application: Application) : ParentViewModel<IAppActivity>(application),
        IAppViewModel {

    @Theme private var currentTheme: Int = 0

    override fun onSetup() {
        currentTheme = iPreferenceRepo.theme

        when (currentTheme) {
            Theme.LIGHT -> callback?.setTheme(R.style.App_Light_UI)
            Theme.DARK -> callback?.setTheme(R.style.App_Dark_UI)
        }
    }

    override fun isThemeChange() = currentTheme != iPreferenceRepo.theme

}
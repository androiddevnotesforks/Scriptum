package sgtmelon.scriptum.screen.vm

import android.app.Application
import sgtmelon.scriptum.R
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.screen.view.callback.IAppActivity
import sgtmelon.scriptum.screen.vm.callback.IAppViewModel
import sgtmelon.scriptum.screen.view.AppActivity

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
            Theme.light -> callback?.setTheme(R.style.App_Light_UI)
            Theme.dark -> callback?.setTheme(R.style.App_Dark_UI)
        }
    }

    override fun isThemeChange() = currentTheme != iPreferenceRepo.theme

}
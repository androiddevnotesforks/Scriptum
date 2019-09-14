package sgtmelon.scriptum.screen.vm

import android.app.Application
import android.os.Bundle
import sgtmelon.scriptum.R
import sgtmelon.scriptum.interactor.app.AppInteractor
import sgtmelon.scriptum.interactor.app.IAppInteractor
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.screen.ui.AppActivity
import sgtmelon.scriptum.screen.ui.callback.IAppActivity
import sgtmelon.scriptum.screen.vm.callback.IAppViewModel

/**
 * ViewModel for [AppActivity]
 */
class AppViewModel(application: Application) : ParentViewModel<IAppActivity>(application),
        IAppViewModel {

    private val iInteractor: IAppInteractor = AppInteractor(context)

    @Theme private var currentTheme: Int = iInteractor.theme

    override fun onSetup(bundle: Bundle?) {
        when (currentTheme) {
            Theme.LIGHT -> callback?.setTheme(R.style.App_Light_UI)
            Theme.DARK -> callback?.setTheme(R.style.App_Dark_UI)
        }
    }

    override fun isThemeChange() = currentTheme != iInteractor.theme

}
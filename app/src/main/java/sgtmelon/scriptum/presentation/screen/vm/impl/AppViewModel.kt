package sgtmelon.scriptum.presentation.screen.vm.impl

import android.app.Application
import android.os.Bundle
import sgtmelon.scriptum.R
import sgtmelon.scriptum.domain.interactor.callback.IAppInteractor
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.presentation.screen.ui.callback.IAppActivity
import sgtmelon.scriptum.presentation.screen.vm.callback.IAppViewModel

/**
 * ViewModel for [IAppActivity].
 */
class AppViewModel(application: Application) : ParentViewModel<IAppActivity>(application),
        IAppViewModel {

    private lateinit var interactor: IAppInteractor

    fun setInteractor(interactor: IAppInteractor) {
        this.interactor = interactor
    }


    @Theme private var theme: Int = Theme.UNDEFINED

    override fun onSetup(bundle: Bundle?) {
        theme = interactor.theme

        callback?.changeControlColor(theme == Theme.LIGHT)

        when (theme) {
            Theme.LIGHT -> callback?.setTheme(R.style.App_Light_UI)
            Theme.DARK -> callback?.setTheme(R.style.App_Dark_UI)
        }
    }

    override fun isThemeChange(): Boolean = theme != interactor.theme

}
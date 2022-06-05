package sgtmelon.scriptum.presentation.screen.vm.impl

import android.app.Application
import android.os.Bundle
import sgtmelon.scriptum.domain.interactor.callback.IAppInteractor
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.common.test.annotation.RunPrivate
import sgtmelon.scriptum.domain.interactor.callback.preference.develop.IPrintDevelopInteractor
import sgtmelon.scriptum.presentation.screen.ui.callback.IAppActivity
import sgtmelon.scriptum.presentation.screen.vm.callback.IAppViewModel

/**
 * ViewModel for [IAppActivity].
 */
class AppViewModel(
    callback: IAppActivity,
    private val interactor: IAppInteractor
) : ParentViewModel<IAppActivity>(callback),
        IAppViewModel {

    @Theme
    @RunPrivate var theme: Int = Theme.UNDEFINED

    override fun onSetup(bundle: Bundle?) {
        theme = interactor.theme

        callback?.setupTheme(theme)
        callback?.changeControlColor()
        callback?.changeSystemColor()
    }

    override fun isThemeChange(): Boolean = theme != interactor.theme

}
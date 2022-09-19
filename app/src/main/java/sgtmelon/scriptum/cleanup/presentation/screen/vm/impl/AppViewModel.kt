package sgtmelon.scriptum.cleanup.presentation.screen.vm.impl

import android.os.Bundle
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.IAppActivity
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.IAppViewModel
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.infrastructure.model.key.Theme

/**
 * ViewModel for [IAppActivity].
 */
class AppViewModel(
    callback: IAppActivity,
    private val preferencesRepo: PreferencesRepo
) : ParentViewModel<IAppActivity>(callback),
        IAppViewModel {

    private val theme: Theme = preferencesRepo.theme

    override fun onSetup(bundle: Bundle?) {
        callback?.setupTheme(theme)
    }

    override fun isThemeChange(): Boolean = theme != preferencesRepo.theme

}
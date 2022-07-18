package sgtmelon.scriptum.cleanup.presentation.screen.vm.impl

import android.os.Bundle
import sgtmelon.common.test.annotation.RunPrivate
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

    @RunPrivate
    lateinit var theme: Theme

    override fun onSetup(bundle: Bundle?) {
        theme = preferencesRepo.theme

        callback?.setupTheme(theme)
        callback?.changeControlColor()
        callback?.changeSystemColor()
    }

    override fun isThemeChange(): Boolean = theme != preferencesRepo.theme

}
package sgtmelon.scriptum.presentation.screen.vm.callback

import sgtmelon.scriptum.presentation.screen.ui.callback.IAppActivity
import sgtmelon.scriptum.presentation.screen.vm.impl.AppViewModel

/**
 * Interface for communication [IAppActivity] with [AppViewModel].
 */
interface IAppViewModel : IParentViewModel {

    fun isThemeChange(): Boolean
}
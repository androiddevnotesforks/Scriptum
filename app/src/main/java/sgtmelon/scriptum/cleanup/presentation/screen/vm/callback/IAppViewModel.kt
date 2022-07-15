package sgtmelon.scriptum.cleanup.presentation.screen.vm.callback

import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.IAppActivity
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.AppViewModel

/**
 * Interface for communication [IAppActivity] with [AppViewModel].
 */
interface IAppViewModel : IParentViewModel {

    fun isThemeChange(): Boolean
}
package sgtmelon.scriptum.presentation.screen.vm.callback

import sgtmelon.scriptum.presentation.screen.ui.impl.AppActivity
import sgtmelon.scriptum.presentation.screen.vm.impl.AppViewModel

/**
 * Interface for communication [AppActivity] with [AppViewModel].
 */
interface IAppViewModel : IParentViewModel {

    fun isThemeChange(): Boolean

}
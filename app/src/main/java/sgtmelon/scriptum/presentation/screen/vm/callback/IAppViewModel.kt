package sgtmelon.scriptum.presentation.screen.vm.callback

import sgtmelon.scriptum.presentation.screen.ui.AppActivity
import sgtmelon.scriptum.presentation.screen.vm.AppViewModel

/**
 * Interface for communication [AppActivity] with [AppViewModel].
 */
interface IAppViewModel : IParentViewModel {

    fun isThemeChange(): Boolean

}
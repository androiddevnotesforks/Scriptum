package sgtmelon.scriptum.screen.vm.callback

import sgtmelon.scriptum.screen.ui.AppActivity
import sgtmelon.scriptum.screen.vm.AppViewModel

/**
 * Interface for communication [AppActivity] with [AppViewModel]
 */
interface IAppViewModel : IParentViewModel{

    fun isThemeChange(): Boolean

}
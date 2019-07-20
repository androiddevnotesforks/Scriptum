package sgtmelon.scriptum.screen.vm.callback

import sgtmelon.scriptum.screen.ui.AppActivity
import sgtmelon.scriptum.screen.vm.AppViewModel

/**
 * Интерфейс для обзения [AppActivity] с [AppViewModel]
 *
 * @author SerjantArbuz
 */
interface IAppViewModel : IParentViewModel{

    fun onSetup()

    fun isThemeChange(): Boolean

}
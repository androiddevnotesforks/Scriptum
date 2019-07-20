package sgtmelon.scriptum.screen.callback

import sgtmelon.scriptum.screen.view.AppActivity
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
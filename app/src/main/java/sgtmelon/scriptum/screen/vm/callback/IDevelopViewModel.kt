package sgtmelon.scriptum.screen.vm.callback

import sgtmelon.scriptum.screen.view.DevelopActivity
import sgtmelon.scriptum.screen.vm.DevelopViewModel

/**
 * Интерфейс для общения [DevelopActivity] с [DevelopViewModel]
 *
 * @author SerjantArbuz
 */
interface IDevelopViewModel : IParentViewModel {

    fun onSetup(): Any

    fun onIntroClick()

}
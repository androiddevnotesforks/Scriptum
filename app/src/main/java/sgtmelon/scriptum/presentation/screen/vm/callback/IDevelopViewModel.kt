package sgtmelon.scriptum.presentation.screen.vm.callback

import sgtmelon.scriptum.presentation.screen.ui.impl.DevelopActivity
import sgtmelon.scriptum.presentation.screen.vm.impl.DevelopViewModel

/**
 * Interface for communication [DevelopActivity] with [DevelopViewModel].
 */
interface IDevelopViewModel : IParentViewModel {

    fun onIntroClick()

}
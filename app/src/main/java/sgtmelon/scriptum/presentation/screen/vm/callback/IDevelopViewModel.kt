package sgtmelon.scriptum.presentation.screen.vm.callback

import sgtmelon.scriptum.presentation.screen.ui.DevelopActivity
import sgtmelon.scriptum.presentation.screen.vm.DevelopViewModel

/**
 * Interface for communication [DevelopActivity] with [DevelopViewModel].
 */
interface IDevelopViewModel : IParentViewModel {

    fun onIntroClick()

}
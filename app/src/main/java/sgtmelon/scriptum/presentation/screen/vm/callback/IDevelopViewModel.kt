package sgtmelon.scriptum.presentation.screen.vm.callback

import sgtmelon.scriptum.presentation.screen.ui.callback.IDevelopActivity
import sgtmelon.scriptum.presentation.screen.vm.impl.DevelopViewModel

/**
 * Interface for communication [IDevelopActivity] with [DevelopViewModel].
 */
interface IDevelopViewModel : IParentViewModel {
    fun onIntroClick()
}
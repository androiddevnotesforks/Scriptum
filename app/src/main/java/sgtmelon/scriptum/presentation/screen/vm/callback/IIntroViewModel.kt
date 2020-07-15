package sgtmelon.scriptum.presentation.screen.vm.callback

import sgtmelon.scriptum.presentation.screen.ui.callback.IIntroActivity
import sgtmelon.scriptum.presentation.screen.vm.impl.IntroViewModel

/**
 * Interface for communication [IIntroActivity] with [IntroViewModel].
 */
interface IIntroViewModel : IParentViewModel {
    fun onClickEnd()
}
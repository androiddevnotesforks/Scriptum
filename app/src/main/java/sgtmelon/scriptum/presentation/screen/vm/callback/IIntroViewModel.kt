package sgtmelon.scriptum.presentation.screen.vm.callback

import sgtmelon.scriptum.presentation.screen.ui.impl.intro.IntroActivity
import sgtmelon.scriptum.presentation.screen.vm.impl.IntroViewModel

/**
 * Interface for communication [IntroActivity] with [IntroViewModel].
 */
interface IIntroViewModel : IParentViewModel {

    fun onClickEnd()

}
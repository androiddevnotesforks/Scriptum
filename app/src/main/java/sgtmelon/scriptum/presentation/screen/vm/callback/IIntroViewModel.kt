package sgtmelon.scriptum.presentation.screen.vm.callback

import sgtmelon.scriptum.presentation.screen.ui.intro.IntroActivity
import sgtmelon.scriptum.presentation.screen.vm.IntroViewModel

/**
 * Interface for communication [IntroActivity] with [IntroViewModel].
 */
interface IIntroViewModel : IParentViewModel {

    fun onClickEnd()

}
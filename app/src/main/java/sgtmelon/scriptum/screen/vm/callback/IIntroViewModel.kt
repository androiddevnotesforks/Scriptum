package sgtmelon.scriptum.screen.vm.callback

import sgtmelon.scriptum.screen.ui.intro.IntroActivity
import sgtmelon.scriptum.screen.vm.IntroViewModel

/**
 * Interface for communication [IntroActivity] with [IntroViewModel].
 */
interface IIntroViewModel : IParentViewModel {

    fun onClickEnd()

}
package sgtmelon.scriptum.interactor.callback

import sgtmelon.scriptum.interactor.IntroInteractor
import sgtmelon.scriptum.screen.vm.IntroViewModel

/**
 * Interface for communication [IntroViewModel] with [IntroInteractor]
 */
interface IIntroInteractor {

    fun onIntroFinish()

}
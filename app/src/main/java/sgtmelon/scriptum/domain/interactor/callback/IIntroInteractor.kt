package sgtmelon.scriptum.domain.interactor.callback

import sgtmelon.scriptum.domain.interactor.impl.IntroInteractor
import sgtmelon.scriptum.presentation.screen.vm.impl.IntroViewModel

/**
 * Interface for communication [IntroViewModel] with [IntroInteractor]
 */
interface IIntroInteractor {

    fun onIntroFinish()

}
package sgtmelon.scriptum.domain.interactor.callback

import sgtmelon.scriptum.domain.interactor.impl.IntroInteractor
import sgtmelon.scriptum.presentation.screen.vm.callback.IIntroViewModel

/**
 * Interface for communication [IIntroViewModel] with [IntroInteractor].
 */
interface IIntroInteractor {
    fun onIntroFinish()
}
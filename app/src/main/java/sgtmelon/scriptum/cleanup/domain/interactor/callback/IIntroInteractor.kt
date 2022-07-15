package sgtmelon.scriptum.cleanup.domain.interactor.callback

import sgtmelon.scriptum.cleanup.domain.interactor.impl.IntroInteractor
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.IIntroViewModel

/**
 * Interface for communication [IIntroViewModel] with [IntroInteractor].
 */
interface IIntroInteractor {
    fun onIntroFinish()
}
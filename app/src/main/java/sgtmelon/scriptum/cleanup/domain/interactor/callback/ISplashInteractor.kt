package sgtmelon.scriptum.cleanup.domain.interactor.callback

import sgtmelon.scriptum.cleanup.domain.interactor.impl.SplashInteractor
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.ISplashViewModel

/**
 * Interface for communication [ISplashViewModel] with [SplashInteractor].
 */
interface ISplashInteractor : IParentInteractor {
    val isFirstStart: Boolean
}
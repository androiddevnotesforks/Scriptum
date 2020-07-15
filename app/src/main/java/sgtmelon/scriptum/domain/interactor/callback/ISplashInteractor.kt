package sgtmelon.scriptum.domain.interactor.callback

import sgtmelon.scriptum.domain.interactor.impl.SplashInteractor
import sgtmelon.scriptum.presentation.screen.vm.callback.ISplashViewModel

/**
 * Interface for communication [ISplashViewModel] with [SplashInteractor].
 */
interface ISplashInteractor : IParentInteractor {
    val firstStart: Boolean
}
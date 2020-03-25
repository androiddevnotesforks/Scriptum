package sgtmelon.scriptum.domain.interactor.callback

import sgtmelon.scriptum.domain.interactor.SplashInteractor
import sgtmelon.scriptum.presentation.screen.vm.impl.SplashViewModel

/**
 * Interface for communication [SplashViewModel] with [SplashInteractor]
 */
interface ISplashInteractor : IParentInteractor {

    val firstStart: Boolean

}
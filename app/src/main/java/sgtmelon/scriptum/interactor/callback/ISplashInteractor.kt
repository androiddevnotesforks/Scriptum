package sgtmelon.scriptum.interactor.callback

import sgtmelon.scriptum.interactor.SplashInteractor
import sgtmelon.scriptum.presentation.screen.vm.SplashViewModel

/**
 * Interface for communication [SplashViewModel] with [SplashInteractor]
 */
interface ISplashInteractor : IParentInteractor {

    val firstStart: Boolean

}
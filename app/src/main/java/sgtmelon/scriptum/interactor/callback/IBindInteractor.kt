package sgtmelon.scriptum.interactor.callback

import sgtmelon.scriptum.interactor.BindInteractor
import sgtmelon.scriptum.interactor.IntroInteractor
import sgtmelon.scriptum.screen.vm.IntroViewModel

/**
 * Interface for communication with [BindInteractor]
 */
interface IBindInteractor : IParentInteractor {

    suspend fun notifyBind()

}
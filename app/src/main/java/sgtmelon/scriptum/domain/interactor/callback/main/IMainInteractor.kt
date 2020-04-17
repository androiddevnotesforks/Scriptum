package sgtmelon.scriptum.domain.interactor.callback.main

import sgtmelon.scriptum.domain.interactor.callback.IParentInteractor
import sgtmelon.scriptum.domain.interactor.impl.main.MainInteractor
import sgtmelon.scriptum.presentation.screen.vm.impl.main.MainViewModel

/**
 * Interface for communication [MainViewModel] with [MainInteractor]
 */
interface IMainInteractor : IParentInteractor {

    suspend fun tidyUpAlarm()

}
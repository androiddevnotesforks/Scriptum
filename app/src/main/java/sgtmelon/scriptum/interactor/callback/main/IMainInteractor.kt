package sgtmelon.scriptum.interactor.callback.main

import sgtmelon.scriptum.interactor.callback.IParentInteractor
import sgtmelon.scriptum.interactor.main.MainInteractor
import sgtmelon.scriptum.presentation.screen.vm.main.MainViewModel

/**
 * Interface for communication [MainViewModel] with [MainInteractor]
 */
interface IMainInteractor : IParentInteractor {

    suspend fun tidyUpAlarm()

}
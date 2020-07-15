package sgtmelon.scriptum.domain.interactor.callback.main

import sgtmelon.scriptum.domain.interactor.callback.IParentInteractor
import sgtmelon.scriptum.domain.interactor.impl.main.MainInteractor
import sgtmelon.scriptum.presentation.screen.vm.callback.main.IMainViewModel

/**
 * Interface for communication [IMainViewModel] with [MainInteractor].
 */
interface IMainInteractor : IParentInteractor {
    suspend fun tidyUpAlarm()
}
package sgtmelon.scriptum.cleanup.domain.interactor.callback.system

import sgtmelon.scriptum.cleanup.domain.interactor.callback.IParentInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.system.SystemInteractor
import sgtmelon.scriptum.cleanup.presentation.screen.presenter.system.ISystemPresenter

/**
 * Interface for communication [ISystemPresenter] with [SystemInteractor]
 */
interface ISystemInteractor : IParentInteractor {

    suspend fun tidyUpAlarm()

    suspend fun notifyNotesBind()

    suspend fun notifyCountBind()

    suspend fun unbindNote(id: Long)
}
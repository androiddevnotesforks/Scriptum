package sgtmelon.scriptum.domain.interactor.callback.eternal

import sgtmelon.scriptum.domain.interactor.callback.IParentInteractor
import sgtmelon.scriptum.domain.interactor.impl.eternal.EternalInteractor
import sgtmelon.scriptum.presentation.service.presenter.IEternalPresenter

/**
 * Interface for communication [IEternalPresenter] with [EternalInteractor]
 */
interface IEternalInteractor : IParentInteractor {

    suspend fun tidyUpAlarm()

    suspend fun notifyNotesBind()

    suspend fun notifyInfoBind()

    suspend fun unbindNote(id: Long)
}
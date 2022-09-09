package sgtmelon.scriptum.cleanup.domain.interactor.callback.main

import sgtmelon.scriptum.cleanup.domain.interactor.callback.IParentInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.main.BinInteractor
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.main.IBinViewModel

/**
 * Interface for communication [IBinViewModel] with [BinInteractor].
 */
interface IBinInteractor : IParentInteractor {

    suspend fun getCount(): Int

    suspend fun getList(): MutableList<NoteItem>
}
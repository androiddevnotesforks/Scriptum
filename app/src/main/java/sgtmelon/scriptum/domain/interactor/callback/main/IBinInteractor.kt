package sgtmelon.scriptum.domain.interactor.callback.main

import sgtmelon.scriptum.domain.interactor.callback.IParentInteractor
import sgtmelon.scriptum.domain.interactor.impl.main.BinInteractor
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.presentation.screen.vm.callback.main.IBinViewModel

/**
 * Interface for communication [IBinViewModel] with [BinInteractor].
 */
interface IBinInteractor : IParentInteractor {

    suspend fun getCount(): Int

    suspend fun getList(): MutableList<NoteItem>

    suspend fun clearBin()

    suspend fun restoreNote(item: NoteItem)

    suspend fun copy(item: NoteItem): String

    suspend fun clearNote(item: NoteItem)

}
package sgtmelon.scriptum.interactor.callback.main

import sgtmelon.scriptum.interactor.callback.IParentInteractor
import sgtmelon.scriptum.interactor.main.BinInteractor
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.screen.vm.main.BinViewModel

/**
 * Interface for communication [BinViewModel] with [BinInteractor]
 */
interface IBinInteractor : IParentInteractor {

    @Theme val theme: Int

    suspend fun getList(): MutableList<NoteItem>

    suspend fun clearBin()

    suspend fun restoreNote(noteItem: NoteItem)

    suspend fun copy(noteItem: NoteItem)

    suspend fun clearNote(noteItem: NoteItem)

}
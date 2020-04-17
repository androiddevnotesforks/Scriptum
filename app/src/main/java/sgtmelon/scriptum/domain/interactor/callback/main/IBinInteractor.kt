package sgtmelon.scriptum.domain.interactor.callback.main

import sgtmelon.scriptum.domain.interactor.callback.IParentInteractor
import sgtmelon.scriptum.domain.interactor.impl.main.BinInteractor
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.presentation.screen.vm.impl.main.BinViewModel

/**
 * Interface for communication [BinViewModel] with [BinInteractor]
 */
interface IBinInteractor : IParentInteractor {

    @Theme val theme: Int

    suspend fun getCount(): Int

    suspend fun getList(): MutableList<NoteItem>

    suspend fun clearBin()

    suspend fun restoreNote(noteItem: NoteItem)

    suspend fun copy(noteItem: NoteItem)

    suspend fun clearNote(noteItem: NoteItem)

}
package sgtmelon.scriptum.interactor.callback.main

import sgtmelon.scriptum.interactor.callback.IParentInteractor
import sgtmelon.scriptum.interactor.main.BinInteractor
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.screen.vm.main.BinViewModel

/**
 * Interface for communication [BinViewModel] with [BinInteractor]
 */
interface IBinInteractor : IParentInteractor {

    @Theme val theme: Int

    fun getList(): MutableList<NoteModel>

    suspend fun clearBin()

    suspend fun restoreNote(noteModel: NoteModel)

    suspend fun copy(noteEntity: NoteEntity)

    suspend fun clearNote(noteModel: NoteModel)

}
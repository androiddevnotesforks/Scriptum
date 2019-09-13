package sgtmelon.scriptum.interactor.bin

import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.annotation.Theme

/**
 * Interface for communication with [BinInteractor]
 *
 * @author SerjantArbuz
 */
interface IBinInteractor {

    @Theme val theme: Int

    fun getList(): MutableList<NoteModel>

    suspend fun clearBin()

    suspend fun restoreNote(noteModel: NoteModel)

    suspend fun clearNote(noteModel: NoteModel)

}
package sgtmelon.scriptum.interactor.bin

import android.content.Context
import sgtmelon.scriptum.interactor.ParentInteractor
import sgtmelon.scriptum.model.NoteModel

class BinInteractor(context: Context) : ParentInteractor(context), IBinInteractor {

    override val theme: Int get() = iPreferenceRepo.theme

    override fun getList() = iRoomRepo.getNoteModelList(bin = true)

    override suspend fun clearBin() = iRoomRepo.clearBin()

    override suspend fun restoreNote(noteModel: NoteModel) = iRoomRepo.restoreNote(noteModel)

    override suspend fun clearNote(noteModel: NoteModel) = iRoomRepo.clearNote(noteModel)

}
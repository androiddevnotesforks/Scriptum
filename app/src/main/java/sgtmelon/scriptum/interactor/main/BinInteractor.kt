package sgtmelon.scriptum.interactor.main

import android.content.Context
import sgtmelon.scriptum.interactor.ParentInteractor
import sgtmelon.scriptum.interactor.callback.main.IBinInteractor
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.screen.vm.main.BinViewModel

/**
 * Interactor for [BinViewModel]
 */
class BinInteractor(context: Context) : ParentInteractor(context), IBinInteractor {

    @Theme override val theme: Int get() = iPreferenceRepo.theme

    override fun getList() = iRoomRepo.getNoteModelList(bin = true)

    override suspend fun clearBin() = iRoomRepo.clearBin()

    override suspend fun restoreNote(noteModel: NoteModel) = iRoomRepo.restoreNote(noteModel)

    override suspend fun clearNote(noteModel: NoteModel) = iRoomRepo.clearNote(noteModel)

}
package sgtmelon.scriptum.interactor.main

import android.content.Context
import sgtmelon.scriptum.interactor.ParentInteractor
import sgtmelon.scriptum.interactor.callback.main.IBinInteractor
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.screen.ui.callback.main.IBinBridge
import sgtmelon.scriptum.screen.vm.main.BinViewModel

/**
 * Interactor for [BinViewModel]
 */
class BinInteractor(context: Context, private var callback: IBinBridge?) :
        ParentInteractor(context),
        IBinInteractor {

    override fun onDestroy(func: () -> Unit) = super.onDestroy { callback = null }


    @Theme override val theme: Int get() = iPreferenceRepo.theme

    override fun getList() = iRoomRepo.getNoteModelList(bin = true)

    override suspend fun clearBin() = iRoomRepo.clearBin()

    override suspend fun restoreNote(noteModel: NoteModel) = iRoomRepo.restoreNote(noteModel)

    override suspend fun copy(noteEntity: NoteEntity) {
        callback?.copyClipboard(iRoomRepo.getCopyText(noteEntity))
    }

    override suspend fun clearNote(noteModel: NoteModel) = iRoomRepo.clearNote(noteModel)

}
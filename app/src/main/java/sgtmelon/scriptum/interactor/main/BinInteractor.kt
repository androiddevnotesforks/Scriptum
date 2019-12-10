package sgtmelon.scriptum.interactor.main

import android.content.Context
import sgtmelon.scriptum.interactor.ParentInteractor
import sgtmelon.scriptum.interactor.callback.main.IBinInteractor
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.repository.note.INoteRepo
import sgtmelon.scriptum.repository.note.NoteRepo
import sgtmelon.scriptum.screen.ui.callback.main.IBinBridge
import sgtmelon.scriptum.screen.vm.main.BinViewModel

/**
 * Interactor for [BinViewModel]
 */
class BinInteractor(context: Context, private var callback: IBinBridge?) :
        ParentInteractor(context),
        IBinInteractor {

    private val iNoteRepo: INoteRepo = NoteRepo(context)

    override fun onDestroy(func: () -> Unit) = super.onDestroy { callback = null }


    @Theme override val theme: Int get() = iPreferenceRepo.theme

    override suspend fun getList() : MutableList<NoteItem> {
        val sort = iPreferenceRepo.sort
        return iNoteRepo.getList(sort, bin = true, optimal = true, filterVisible = false)
    }

    override suspend fun clearBin() = iNoteRepo.clearBin()

    override suspend fun restoreNote(noteItem: NoteItem) = iNoteRepo.restoreNote(noteItem)

    override suspend fun copy(noteItem: NoteItem) {
        callback?.copyClipboard(iNoteRepo.getCopyText(noteItem))
    }

    override suspend fun clearNote(noteItem: NoteItem) = iNoteRepo.clearNote(noteItem)

}
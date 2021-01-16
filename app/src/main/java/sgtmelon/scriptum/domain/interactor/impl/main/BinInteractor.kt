package sgtmelon.scriptum.domain.interactor.impl.main

import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.data.repository.room.callback.INoteRepo
import sgtmelon.scriptum.domain.interactor.callback.main.IBinInteractor
import sgtmelon.scriptum.domain.interactor.impl.ParentInteractor
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.domain.model.annotation.test.RunPrivate
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.extension.runMain
import sgtmelon.scriptum.presentation.screen.ui.callback.main.IBinBridge
import sgtmelon.scriptum.presentation.screen.vm.callback.main.IBinViewModel

/**
 * Interactor for [IBinViewModel].
 */
class BinInteractor(
    private val preferenceRepo: IPreferenceRepo,
    private val noteRepo: INoteRepo,
    @RunPrivate var callback: IBinBridge?
) : ParentInteractor(),
    IBinInteractor {

    override fun onDestroy(func: () -> Unit) = super.onDestroy { callback = null }


    @Theme override val theme: Int get() = preferenceRepo.theme

    override suspend fun getCount(): Int = noteRepo.getCount(isBin = true)

    override suspend fun getList(): MutableList<NoteItem> {
        val sort = preferenceRepo.sort
        return noteRepo.getList(sort, isBin = true, isOptimal = true, filterVisible = false)
    }

    override suspend fun clearBin() = noteRepo.clearBin()

    override suspend fun restoreNote(item: NoteItem) = noteRepo.restoreNote(item)

    override suspend fun copy(item: NoteItem) {
        val text = noteRepo.getCopyText(item)
        runMain { callback?.copyClipboard(text) }
    }

    override suspend fun clearNote(item: NoteItem) = noteRepo.clearNote(item)
}
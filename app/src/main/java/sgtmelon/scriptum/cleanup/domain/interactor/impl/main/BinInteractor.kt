package sgtmelon.scriptum.cleanup.domain.interactor.impl.main

import sgtmelon.scriptum.infrastructure.preferences.Preferences
import sgtmelon.scriptum.cleanup.data.repository.room.callback.INoteRepo
import sgtmelon.scriptum.cleanup.domain.interactor.callback.main.IBinInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.ParentInteractor
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.main.IBinViewModel

/**
 * Interactor for [IBinViewModel].
 */
class BinInteractor(
    private val preferenceRepo: Preferences,
    private val noteRepo: INoteRepo
) : ParentInteractor(),
    IBinInteractor {

    override suspend fun getCount(): Int = noteRepo.getCount(isBin = true)

    override suspend fun getList(): MutableList<NoteItem> {
        val sort = preferenceRepo.sort
        return noteRepo.getList(sort, isBin = true, isOptimal = true, filterVisible = false)
    }

    override suspend fun clearBin() = noteRepo.clearBin()

    override suspend fun restoreNote(item: NoteItem) = noteRepo.restoreNote(item)

    override suspend fun copy(item: NoteItem): String = noteRepo.getCopyText(item)

    override suspend fun clearNote(item: NoteItem) = noteRepo.clearNote(item)
}
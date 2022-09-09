package sgtmelon.scriptum.cleanup.domain.interactor.impl.note

import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.RankRepo
import sgtmelon.scriptum.cleanup.domain.interactor.callback.note.IRollNoteInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.ParentInteractor
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.note.IRollNoteViewModel

/**
 * Interactor for [IRollNoteViewModel].
 */
class RollNoteInteractor(
    private val rankRepo: RankRepo,
    private val noteRepo: NoteRepo
) : ParentInteractor(),
    IRollNoteInteractor {

    override suspend fun getItem(id: Long): NoteItem.Roll? {
        val noteItem = noteRepo.getItem(id, isOptimal = false)

        if (noteItem !is NoteItem.Roll) return null

        return noteItem
    }


    override suspend fun setVisible(noteItem: NoteItem.Roll) = noteRepo.setRollVisible(noteItem)

    /**
     * Update single roll.
     */
    override suspend fun updateRollCheck(noteItem: NoteItem.Roll, p: Int) {
        noteRepo.updateRollCheck(noteItem, p)
    }


    override suspend fun getRankId(check: Int): Long = rankRepo.getId(check)


    override suspend fun convertNote(item: NoteItem.Roll) {
        noteRepo.convertNote(item, useCache = true)
    }

    override suspend fun saveNote(item: NoteItem.Roll, isCreate: Boolean) {
        noteRepo.saveNote(item, isCreate)
        rankRepo.updateConnection(item)
    }
}
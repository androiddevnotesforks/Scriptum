package sgtmelon.scriptum.cleanup.domain.interactor.impl.note

import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.RankRepo
import sgtmelon.scriptum.cleanup.domain.interactor.callback.note.ITextNoteInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.ParentInteractor
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.note.ITextNoteViewModel
import sgtmelon.scriptum.data.repository.database.AlarmRepo

/**
 * Interactor for [ITextNoteViewModel].
 */
class TextNoteInteractor(
    private val alarmRepo: AlarmRepo,
    private val rankRepo: RankRepo,
    private val noteRepo: NoteRepo
) : ParentInteractor(),
    ITextNoteInteractor {

    override suspend fun getItem(id: Long): NoteItem.Text? {
        val noteItem = noteRepo.getItem(id, isOptimal = false)

        if (noteItem !is NoteItem.Text) return null

        return noteItem
    }

    override suspend fun getRankDialogItemArray(emptyName: String): Array<String> {
        return rankRepo.getDialogItemArray(emptyName)
    }


    override suspend fun getRankId(check: Int): Long = rankRepo.getId(check)


    override suspend fun convertNote(item: NoteItem.Text) {
        noteRepo.convertNote(item)
    }

    override suspend fun updateNote(item: NoteItem.Text) = noteRepo.updateNote(item)

    override suspend fun saveNote(item: NoteItem.Text, isCreate: Boolean) {
        noteRepo.saveNote(item, isCreate)
        rankRepo.updateConnection(item)
    }
}
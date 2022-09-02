package sgtmelon.scriptum.cleanup.domain.interactor.impl.note

import java.util.Calendar
import sgtmelon.scriptum.cleanup.data.repository.room.callback.AlarmRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.RankRepo
import sgtmelon.scriptum.cleanup.domain.interactor.callback.note.ITextNoteInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.ParentInteractor
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.note.ITextNoteViewModel

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

    /**
     * TODO make common
     */
    override suspend fun getDateList(): List<String> = alarmRepo.getList().map { it.alarm.date }

    override suspend fun clearDate(item: NoteItem.Text) = alarmRepo.delete(item.id)

    override suspend fun setDate(item: NoteItem.Text, calendar: Calendar) {
        alarmRepo.insertOrUpdate(item, calendar)
    }


    override suspend fun convertNote(item: NoteItem.Text) {
        noteRepo.convertNote(item)
    }

    override suspend fun updateNote(item: NoteItem.Text) = noteRepo.updateNote(item)

    override suspend fun clearNote(item: NoteItem.Text) = noteRepo.clearNote(item)

    override suspend fun saveNote(item: NoteItem.Text, isCreate: Boolean) {
        noteRepo.saveNote(item, isCreate)
        rankRepo.updateConnection(item)
    }
}
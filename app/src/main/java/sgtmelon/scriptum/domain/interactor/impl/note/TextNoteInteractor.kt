package sgtmelon.scriptum.domain.interactor.impl.note

import sgtmelon.common.getText
import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.data.repository.room.callback.IAlarmRepo
import sgtmelon.scriptum.data.repository.room.callback.INoteRepo
import sgtmelon.scriptum.data.repository.room.callback.IRankRepo
import sgtmelon.scriptum.domain.interactor.callback.note.ITextNoteInteractor
import sgtmelon.scriptum.domain.interactor.impl.ParentInteractor
import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.presentation.control.note.save.SaveControl
import sgtmelon.scriptum.presentation.screen.vm.callback.note.ITextNoteViewModel
import java.util.*

/**
 * Interactor for [ITextNoteViewModel].
 */
class TextNoteInteractor(
    private val preferenceRepo: IPreferenceRepo,
    private val alarmRepo: IAlarmRepo,
    private val rankRepo: IRankRepo,
    private val noteRepo: INoteRepo
) : ParentInteractor(),
    ITextNoteInteractor {

    override fun getSaveModel(): SaveControl.Model = with(preferenceRepo) {
        return@with SaveControl.Model(pauseSaveOn, autoSaveOn, savePeriod)
    }

    @Color override val defaultColor: Int get() = preferenceRepo.defaultColor


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
        alarmRepo.insertOrUpdate(item, calendar.getText())
    }


    override suspend fun convertNote(item: NoteItem.Text) {
        noteRepo.convertNote(item)
    }

    override suspend fun restoreNote(item: NoteItem.Text) = noteRepo.restoreNote(item)

    override suspend fun updateNote(item: NoteItem.Text) = noteRepo.updateNote(item)

    override suspend fun clearNote(item: NoteItem.Text) = noteRepo.clearNote(item)

    override suspend fun saveNote(item: NoteItem.Text, isCreate: Boolean) {
        noteRepo.saveNote(item, isCreate)
        rankRepo.updateConnection(item)
    }

    override suspend fun deleteNote(item: NoteItem.Text) = noteRepo.deleteNote(item)
}
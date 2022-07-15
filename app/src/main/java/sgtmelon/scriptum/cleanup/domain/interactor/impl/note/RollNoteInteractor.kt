package sgtmelon.scriptum.cleanup.domain.interactor.impl.note

import sgtmelon.common.utils.getText
import sgtmelon.scriptum.infrastructure.preferences.Preferences
import sgtmelon.scriptum.cleanup.data.repository.room.callback.IAlarmRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.INoteRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.IRankRepo
import sgtmelon.scriptum.cleanup.domain.interactor.callback.note.IRollNoteInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.ParentInteractor
import sgtmelon.scriptum.cleanup.domain.model.annotation.Color
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.presentation.control.note.save.SaveControl
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.note.IRollNoteViewModel
import java.util.*

/**
 * Interactor for [IRollNoteViewModel].
 */
class RollNoteInteractor(
    private val preferenceRepo: Preferences,
    private val alarmRepo: IAlarmRepo,
    private val rankRepo: IRankRepo,
    private val noteRepo: INoteRepo
) : ParentInteractor(),
    IRollNoteInteractor {

    override fun getSaveModel(): SaveControl.Model = with(preferenceRepo) {
        return@with SaveControl.Model(pauseSaveOn, autoSaveOn, savePeriod)
    }

    @Color override val defaultColor: Int get() = preferenceRepo.defaultColor


    override suspend fun getItem(id: Long): NoteItem.Roll? {
        val noteItem = noteRepo.getItem(id, isOptimal = false)

        if (noteItem !is NoteItem.Roll) return null

        return noteItem
    }

    override suspend fun getRankDialogItemArray(emptyName: String): Array<String> {
        return rankRepo.getDialogItemArray(emptyName)
    }


    override suspend fun setVisible(noteItem: NoteItem.Roll) = noteRepo.setRollVisible(noteItem)

    /**
     * Update single roll.
     */
    override suspend fun updateRollCheck(noteItem: NoteItem.Roll, p: Int) {
        noteRepo.updateRollCheck(noteItem, p)
    }

    /**
     * Update all rolls rely on checks.
     */
    override suspend fun updateRollCheck(noteItem: NoteItem.Roll, isCheck: Boolean) {
        noteRepo.updateRollCheck(noteItem, isCheck)
    }


    override suspend fun getRankId(check: Int): Long = rankRepo.getId(check)

    /**
     * TODO make common
     */
    override suspend fun getDateList(): List<String> = alarmRepo.getList().map { it.alarm.date }

    override suspend fun clearDate(item: NoteItem.Roll) = alarmRepo.delete(item.id)

    override suspend fun setDate(item: NoteItem.Roll, calendar: Calendar) {
        alarmRepo.insertOrUpdate(item, calendar.getText())
    }


    override suspend fun convertNote(item: NoteItem.Roll) {
        noteRepo.convertNote(item, useCache = true)
    }

    override suspend fun restoreNote(item: NoteItem.Roll) = noteRepo.restoreNote(item)

    override suspend fun updateNote(item: NoteItem.Roll) = noteRepo.updateNote(item)

    override suspend fun clearNote(item: NoteItem.Roll) = noteRepo.clearNote(item)

    override suspend fun saveNote(item: NoteItem.Roll, isCreate: Boolean) {
        noteRepo.saveNote(item, isCreate)
        rankRepo.updateConnection(item)
    }

    override suspend fun deleteNote(item: NoteItem.Roll) = noteRepo.deleteNote(item)
}
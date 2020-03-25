package sgtmelon.scriptum.domain.interactor.note

import sgtmelon.extension.getText
import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.data.repository.room.callback.IAlarmRepo
import sgtmelon.scriptum.data.repository.room.callback.INoteRepo
import sgtmelon.scriptum.data.repository.room.callback.IRankRepo
import sgtmelon.scriptum.domain.interactor.ParentInteractor
import sgtmelon.scriptum.domain.interactor.callback.note.IRollNoteInteractor
import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.presentation.control.note.save.SaveControl
import sgtmelon.scriptum.presentation.screen.ui.callback.note.roll.IRollNoteBridge
import sgtmelon.scriptum.presentation.screen.vm.impl.note.RollNoteViewModel
import java.util.*

/**
 * Interactor for [RollNoteViewModel].
 */
class RollNoteInteractor(
        private val preferenceRepo: IPreferenceRepo,
        private val alarmRepo: IAlarmRepo,
        private val rankRepo: IRankRepo,
        private val noteRepo: INoteRepo,
        private var callback: IRollNoteBridge?
) : ParentInteractor(),
        IRollNoteInteractor {

    private var rankIdVisibleList: List<Long>? = null

    private suspend fun getRankIdVisibleList(): List<Long> {
        return rankIdVisibleList ?: rankRepo.getIdVisibleList().also { rankIdVisibleList = it }
    }

    override fun onDestroy(func: () -> Unit) = super.onDestroy { callback = null }


    override fun getSaveModel() = with(preferenceRepo) {
        SaveControl.Model(pauseSaveOn, autoSaveOn, savePeriod)
    }

    @Theme override val theme: Int get() = preferenceRepo.theme

    @Color override val defaultColor: Int get() = preferenceRepo.defaultColor


    override suspend fun getItem(id: Long): NoteItem? {
        val item = noteRepo.getItem(id, optimisation = false)

        if (item != null) callback?.notifyNoteBind(item, getRankIdVisibleList())

        return item
    }

    override suspend fun getRankDialogItemArray() = rankRepo.getDialogItemArray()

    /**
     * Update single roll.
     */
    override suspend fun updateRollCheck(noteItem: NoteItem, p: Int) {
        noteRepo.updateRollCheck(noteItem, p)
        callback?.notifyNoteBind(noteItem, getRankIdVisibleList())
    }

    /**
     * Update all rolls rely on checks.
     */
    override suspend fun updateRollCheck(noteItem: NoteItem, check: Boolean) {
        noteRepo.updateRollCheck(noteItem, check)
        callback?.notifyNoteBind(noteItem, getRankIdVisibleList())
    }

    override suspend fun getRankId(check: Int): Long = rankRepo.getId(check)

    override suspend fun getDateList() = alarmRepo.getList().map { it.alarm.date }

    override suspend fun clearDate(noteItem: NoteItem) {
        alarmRepo.delete(noteItem.id)
        callback?.cancelAlarm(noteItem.id)
    }

    override suspend fun setDate(noteItem: NoteItem, calendar: Calendar) {
        alarmRepo.insertOrUpdate(noteItem, calendar.getText())
        callback?.setAlarm(calendar, noteItem.id)
    }

    override suspend fun convert(noteItem: NoteItem) {
        noteRepo.convertToText(noteItem, useCache = true)
    }


    override suspend fun restoreNote(noteItem: NoteItem) = noteRepo.restoreNote(noteItem)

    override suspend fun updateNote(noteItem: NoteItem, updateBind: Boolean) {
        noteRepo.updateNote(noteItem)

        if (updateBind) callback?.notifyNoteBind(noteItem, getRankIdVisibleList())
    }

    override suspend fun clearNote(noteItem: NoteItem) = noteRepo.clearNote(noteItem)

    override suspend fun saveNote(noteItem: NoteItem, isCreate: Boolean) {
        noteRepo.saveRollNote(noteItem, isCreate)
        rankRepo.updateConnection(noteItem)

        callback?.notifyNoteBind(noteItem, getRankIdVisibleList())
    }

    override suspend fun deleteNote(noteItem: NoteItem) {
        noteRepo.deleteNote(noteItem)

        callback?.cancelAlarm(noteItem.id)
        callback?.cancelNoteBind(noteItem.id.toInt())
    }

}
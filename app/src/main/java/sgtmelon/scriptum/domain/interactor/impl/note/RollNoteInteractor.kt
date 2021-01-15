package sgtmelon.scriptum.domain.interactor.impl.note

import sgtmelon.extension.getText
import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.data.repository.room.callback.IAlarmRepo
import sgtmelon.scriptum.data.repository.room.callback.INoteRepo
import sgtmelon.scriptum.data.repository.room.callback.IRankRepo
import sgtmelon.scriptum.domain.interactor.callback.note.IRollNoteInteractor
import sgtmelon.scriptum.domain.interactor.impl.ParentInteractor
import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.domain.model.annotation.test.RunPrivate
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.extension.runMain
import sgtmelon.scriptum.presentation.control.note.save.SaveControl
import sgtmelon.scriptum.presentation.screen.ui.callback.note.IParentNoteBridge
import sgtmelon.scriptum.presentation.screen.vm.callback.note.IRollNoteViewModel
import java.util.*

/**
 * Interactor for [IRollNoteViewModel].
 */
class RollNoteInteractor(
        private val preferenceRepo: IPreferenceRepo,
        private val alarmRepo: IAlarmRepo,
        private val rankRepo: IRankRepo,
        private val noteRepo: INoteRepo,
        @RunPrivate var callback: IParentNoteBridge?
) : ParentInteractor(),
        IRollNoteInteractor {

    private var rankIdVisibleList: List<Long>? = null

    private suspend fun getRankIdVisibleList(): List<Long> {
        return rankIdVisibleList ?: rankRepo.getIdVisibleList().also { rankIdVisibleList = it }
    }

    override fun onDestroy(func: () -> Unit) = super.onDestroy { callback = null }


    override fun getSaveModel(): SaveControl.Model = with(preferenceRepo) {
        return@with SaveControl.Model(pauseSaveOn, autoSaveOn, savePeriod)
    }

    @Theme override val theme: Int get() = preferenceRepo.theme

    @Color override val defaultColor: Int get() = preferenceRepo.defaultColor


    override suspend fun getItem(id: Long): NoteItem.Roll? {
        val noteItem = noteRepo.getItem(id, isOptimal = false)

        if (noteItem !is NoteItem.Roll) return null

        val rankIdList = getRankIdVisibleList()
        runMain { callback?.notifyNoteBind(noteItem, rankIdList, preferenceRepo.sort) }

        return noteItem
    }

    override suspend fun getRankDialogItemArray(emptyName: String): Array<String> {
        return rankRepo.getDialogItemArray(emptyName)
    }


    override suspend fun setVisible(noteItem: NoteItem.Roll, updateBind: Boolean) {
        noteRepo.setRollVisible(noteItem)

        val rankIdList = getRankIdVisibleList()
        runMain { callback?.notifyNoteBind(noteItem, rankIdList, preferenceRepo.sort) }
    }


    /**
     * Update single roll.
     */
    override suspend fun updateRollCheck(noteItem: NoteItem.Roll, p: Int) {
        noteRepo.updateRollCheck(noteItem, p)

        val rankIdList = getRankIdVisibleList()
        runMain { callback?.notifyNoteBind(noteItem, rankIdList, preferenceRepo.sort) }
    }

    /**
     * Update all rolls rely on checks.
     */
    override suspend fun updateRollCheck(noteItem: NoteItem.Roll, isCheck: Boolean) {
        noteRepo.updateRollCheck(noteItem, isCheck)

        val rankIdList = getRankIdVisibleList()
        runMain { callback?.notifyNoteBind(noteItem, rankIdList, preferenceRepo.sort) }
    }

    override suspend fun getRankId(check: Int): Long = rankRepo.getId(check)

    override suspend fun getDateList(): List<String> = alarmRepo.getList().map { it.alarm.date }

    override suspend fun clearDate(noteItem: NoteItem.Roll) {
        alarmRepo.delete(noteItem.id)

        runMain { callback?.cancelAlarm(noteItem.id) }
    }

    override suspend fun setDate(noteItem: NoteItem.Roll, calendar: Calendar) {
        alarmRepo.insertOrUpdate(noteItem, calendar.getText())

        runMain { callback?.setAlarm(calendar, noteItem.id) }
    }

    override suspend fun convertNote(noteItem: NoteItem.Roll) {
        noteRepo.convertNote(noteItem, useCache = true)
    }


    override suspend fun restoreNote(noteItem: NoteItem.Roll) = noteRepo.restoreNote(noteItem)

    override suspend fun updateNote(noteItem: NoteItem.Roll, updateBind: Boolean) {
        noteRepo.updateNote(noteItem)

        if (updateBind) {
            val rankIdList = getRankIdVisibleList()
            runMain { callback?.notifyNoteBind(noteItem, rankIdList, preferenceRepo.sort) }
        }
    }

    override suspend fun clearNote(noteItem: NoteItem.Roll) = noteRepo.clearNote(noteItem)

    override suspend fun saveNote(noteItem: NoteItem.Roll, isCreate: Boolean) {
        noteRepo.saveNote(noteItem, isCreate)
        rankRepo.updateConnection(noteItem)

        val rankIdList = getRankIdVisibleList()
        runMain { callback?.notifyNoteBind(noteItem, rankIdList, preferenceRepo.sort) }
    }

    override suspend fun deleteNote(noteItem: NoteItem.Roll) {
        noteRepo.deleteNote(noteItem)

        runMain {
            callback?.cancelAlarm(noteItem.id)
            callback?.cancelNoteBind(noteItem.id)
        }
    }

}
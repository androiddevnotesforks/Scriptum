package sgtmelon.scriptum.domain.interactor.impl.note

import androidx.annotation.VisibleForTesting
import sgtmelon.extension.getText
import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.data.repository.room.callback.IAlarmRepo
import sgtmelon.scriptum.data.repository.room.callback.INoteRepo
import sgtmelon.scriptum.data.repository.room.callback.IRankRepo
import sgtmelon.scriptum.domain.interactor.callback.note.IRollNoteInteractor
import sgtmelon.scriptum.domain.interactor.impl.ParentInteractor
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
        @VisibleForTesting var callback: IRollNoteBridge?
) : ParentInteractor(),
        IRollNoteInteractor {

    private var rankIdVisibleList: List<Long>? = null

    private suspend fun getRankIdVisibleList(): List<Long>? {
        return rankIdVisibleList ?: rankRepo.getIdVisibleList()?.also { rankIdVisibleList = it }
    }

    override fun onDestroy(func: () -> Unit) = super.onDestroy { callback = null }


    override fun getSaveModel(): SaveControl.Model? = with(preferenceRepo) {
        val pauseSaveOn = pauseSaveOn ?: return@with null
        val autoSaveOn = autoSaveOn ?: return@with null
        val savePeriod = savePeriod ?: return@with null

        return@with SaveControl.Model(pauseSaveOn, autoSaveOn, savePeriod)
    }

    @Theme override val theme: Int? get() = preferenceRepo.theme

    @Color override val defaultColor: Int? get() = preferenceRepo.defaultColor


    override suspend fun getItem(id: Long): NoteItem.Roll? {
        val item = noteRepo.getItem(id, optimal = false)

        if (item !is NoteItem.Roll) return null

        val sort = preferenceRepo.sort ?: return null
        val rankIdVisibleList = getRankIdVisibleList() ?: return null

        callback?.notifyNoteBind(item, rankIdVisibleList, sort)

        return item
    }

    override suspend fun getRankDialogItemArray(emptyName: String): Array<String>? {
        return rankRepo.getDialogItemArray(emptyName)
    }


    override suspend fun setVisible(noteId: Long, isVisible: Boolean) {
        noteRepo.setRollVisible(noteId, isVisible)
    }

    override suspend fun getVisible(noteId: Long): Boolean? = noteRepo.getRollVisible(noteId)


    /**
     * Update single roll.
     */
    override suspend fun updateRollCheck(noteItem: NoteItem.Roll, p: Int) {
        noteRepo.updateRollCheck(noteItem, p)

        val sort = preferenceRepo.sort ?: return
        val rankIdVisibleList = getRankIdVisibleList() ?: return

        callback?.notifyNoteBind(noteItem, rankIdVisibleList, sort)
    }

    /**
     * Update all rolls rely on checks.
     */
    override suspend fun updateRollCheck(noteItem: NoteItem.Roll, check: Boolean) {
        noteRepo.updateRollCheck(noteItem, check)

        val sort = preferenceRepo.sort ?: return
        val rankIdVisibleList = getRankIdVisibleList() ?: return

        callback?.notifyNoteBind(noteItem, rankIdVisibleList, sort)
    }

    override suspend fun getRankId(check: Int): Long? = rankRepo.getId(check)

    override suspend fun getDateList(): List<String>? = alarmRepo.getList()?.map { it.alarm.date }

    override suspend fun clearDate(noteItem: NoteItem.Roll) {
        alarmRepo.delete(noteItem.id)
        callback?.cancelAlarm(noteItem.id)
    }

    override suspend fun setDate(noteItem: NoteItem.Roll, calendar: Calendar) {
        alarmRepo.insertOrUpdate(noteItem, calendar.getText())
        callback?.setAlarm(calendar, noteItem.id)
    }

    override suspend fun convertNote(noteItem: NoteItem.Roll) {
        noteRepo.convertNote(noteItem, useCache = true)
    }


    override suspend fun restoreNote(noteItem: NoteItem.Roll) = noteRepo.restoreNote(noteItem)

    override suspend fun updateNote(noteItem: NoteItem.Roll, updateBind: Boolean) {
        noteRepo.updateNote(noteItem)

        if (updateBind) {
            val sort = preferenceRepo.sort ?: return
            val rankIdVisibleList = getRankIdVisibleList() ?: return

            callback?.notifyNoteBind(noteItem, rankIdVisibleList, sort)
        }
    }

    override suspend fun clearNote(noteItem: NoteItem.Roll) = noteRepo.clearNote(noteItem)

    override suspend fun saveNote(noteItem: NoteItem.Roll, isCreate: Boolean) {
        noteRepo.saveNote(noteItem, isCreate)
        rankRepo.updateConnection(noteItem)

        val sort = preferenceRepo.sort ?: return
        val rankIdVisibleList = getRankIdVisibleList() ?: return

        callback?.notifyNoteBind(noteItem, rankIdVisibleList, sort)
    }

    override suspend fun deleteNote(noteItem: NoteItem.Roll) {
        noteRepo.deleteNote(noteItem)

        callback?.cancelAlarm(noteItem.id)
        callback?.cancelNoteBind(noteItem.id)
    }

}
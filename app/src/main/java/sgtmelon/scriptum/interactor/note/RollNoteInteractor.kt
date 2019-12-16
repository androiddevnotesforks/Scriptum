package sgtmelon.scriptum.interactor.note

import android.content.Context
import sgtmelon.extension.getString
import sgtmelon.scriptum.control.SaveControl
import sgtmelon.scriptum.interactor.ParentInteractor
import sgtmelon.scriptum.interactor.callback.note.IRollNoteInteractor
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.repository.alarm.AlarmRepo
import sgtmelon.scriptum.repository.alarm.IAlarmRepo
import sgtmelon.scriptum.repository.note.INoteRepo
import sgtmelon.scriptum.repository.note.NoteRepo
import sgtmelon.scriptum.repository.rank.IRankRepo
import sgtmelon.scriptum.repository.rank.RankRepo
import sgtmelon.scriptum.screen.ui.callback.note.roll.IRollNoteBridge
import sgtmelon.scriptum.screen.vm.note.RollNoteViewModel
import java.util.*

/**
 * Interactor for [RollNoteViewModel]
 */
class RollNoteInteractor(context: Context, private var callback: IRollNoteBridge?) :
        ParentInteractor(context),
        IRollNoteInteractor {

    private val iAlarmRepo: IAlarmRepo = AlarmRepo(context)
    private val iRankRepo: IRankRepo = RankRepo(context)
    private val iNoteRepo: INoteRepo = NoteRepo(context)

    private var rankIdVisibleList: List<Long>? = null

    private suspend fun getRankIdVisibleList(): List<Long> {
        return rankIdVisibleList ?: iRankRepo.getIdVisibleList().also { rankIdVisibleList = it }
    }

    override fun onDestroy(func: () -> Unit) = super.onDestroy { callback = null }


    override fun getSaveModel() = with(iPreferenceRepo) {
        SaveControl.Model(pauseSaveOn, autoSaveOn, savePeriod)
    }

    @Theme override val theme: Int get() = iPreferenceRepo.theme

    @Color override val defaultColor: Int get() = iPreferenceRepo.defaultColor


    override suspend fun getItem(id: Long): NoteItem? {
        val item = iNoteRepo.getItem(id, optimisation = false)

        if (item != null) callback?.notifyNoteBind(item, getRankIdVisibleList())

        return item
    }

    override suspend fun getRankDialogItemArray() = iRankRepo.getDialogItemArray()

    /**
     * Update single roll.
     */
    override suspend fun updateRollCheck(noteItem: NoteItem, p: Int) {
        iNoteRepo.updateRollCheck(noteItem, p)
        callback?.notifyNoteBind(noteItem, getRankIdVisibleList())
    }

    /**
     * Update all rolls rely on checks.
     */
    override suspend fun updateRollCheck(noteItem: NoteItem, check: Boolean) {
        iNoteRepo.updateRollCheck(noteItem, check)
        callback?.notifyNoteBind(noteItem, getRankIdVisibleList())
    }

    override suspend fun getRankId(check: Int): Long = iRankRepo.getId(check)

    override suspend fun getDateList() = iAlarmRepo.getList().map { it.alarm.date }

    override suspend fun clearDate(noteItem: NoteItem) {
        iAlarmRepo.delete(noteItem.id)
        callback?.cancelAlarm(noteItem.id)
    }

    override suspend fun setDate(noteItem: NoteItem, calendar: Calendar) {
        iAlarmRepo.insertOrUpdate(noteItem, calendar.getString())
        callback?.setAlarm(calendar, noteItem.id)
    }

    override suspend fun convert(noteItem: NoteItem) {
        iNoteRepo.convertToText(noteItem, useCache = true)
    }


    override suspend fun restoreNote(noteItem: NoteItem) = iNoteRepo.restoreNote(noteItem)

    override suspend fun updateNote(noteItem: NoteItem, updateBind: Boolean) {
        iNoteRepo.updateNote(noteItem)

        if (updateBind) callback?.notifyNoteBind(noteItem, getRankIdVisibleList())
    }

    override suspend fun clearNote(noteItem: NoteItem) = iNoteRepo.clearNote(noteItem)

    override suspend fun saveNote(noteItem: NoteItem, isCreate: Boolean) {
        iNoteRepo.saveRollNote(noteItem, isCreate)
        iRankRepo.updateConnection(noteItem)

        callback?.notifyNoteBind(noteItem, getRankIdVisibleList())
    }

    override suspend fun deleteNote(noteItem: NoteItem) {
        iNoteRepo.deleteNote(noteItem)

        callback?.cancelAlarm(noteItem.id)
        callback?.cancelNoteBind(noteItem.id.toInt())
    }

}
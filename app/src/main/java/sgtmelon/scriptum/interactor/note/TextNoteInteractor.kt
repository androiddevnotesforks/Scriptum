package sgtmelon.scriptum.interactor.note

import sgtmelon.extension.getText
import sgtmelon.scriptum.control.SaveControl
import sgtmelon.scriptum.interactor.ParentInteractor
import sgtmelon.scriptum.interactor.callback.note.ITextNoteInteractor
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.repository.room.callback.IAlarmRepo
import sgtmelon.scriptum.repository.room.callback.INoteRepo
import sgtmelon.scriptum.repository.room.callback.IRankRepo
import sgtmelon.scriptum.screen.ui.callback.note.text.ITextNoteBridge
import sgtmelon.scriptum.screen.vm.note.TextNoteViewModel
import java.util.*

/**
 * Interactor for [TextNoteViewModel].
 */
class TextNoteInteractor(
        private val iPreferenceRepo: IPreferenceRepo,
        private val iAlarmRepo: IAlarmRepo,
        private val iRankRepo: IRankRepo,
        private val iNoteRepo: INoteRepo,
        private var callback: ITextNoteBridge?
) : ParentInteractor(),
        ITextNoteInteractor {

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


    override suspend fun getRankId(check: Int): Long = iRankRepo.getId(check)

    override suspend fun getDateList() = iAlarmRepo.getList().map { it.alarm.date }

    override suspend fun clearDate(noteItem: NoteItem) {
        iAlarmRepo.delete(noteItem.id)
        callback?.cancelAlarm(noteItem.id)
    }

    override suspend fun setDate(noteItem: NoteItem, calendar: Calendar) {
        iAlarmRepo.insertOrUpdate(noteItem, calendar.getText())
        callback?.setAlarm(calendar, noteItem.id)
    }

    override suspend fun convert(noteItem: NoteItem) = iNoteRepo.convertToRoll(noteItem)


    override suspend fun restoreNote(noteItem: NoteItem) = iNoteRepo.restoreNote(noteItem)

    override suspend fun updateNote(noteItem: NoteItem, updateBind: Boolean) {
        iNoteRepo.updateNote(noteItem)

        if (updateBind) callback?.notifyNoteBind(noteItem, getRankIdVisibleList())
    }

    override suspend fun clearNote(noteItem: NoteItem) = iNoteRepo.clearNote(noteItem)

    override suspend fun saveNote(noteItem: NoteItem, isCreate: Boolean) {
        iNoteRepo.saveTextNote(noteItem, isCreate)
        iRankRepo.updateConnection(noteItem)

        callback?.notifyNoteBind(noteItem, getRankIdVisibleList())
    }

    override suspend fun deleteNote(noteItem: NoteItem) {
        iNoteRepo.deleteNote(noteItem)

        callback?.cancelAlarm(noteItem.id)
        callback?.cancelNoteBind(noteItem.id.toInt())
    }

}
package sgtmelon.scriptum.interactor.note

import android.content.Context
import sgtmelon.extension.getString
import sgtmelon.scriptum.control.SaveControl
import sgtmelon.scriptum.interactor.ParentInteractor
import sgtmelon.scriptum.interactor.callback.note.ITextNoteInteractor
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.repository.alarm.AlarmRepo
import sgtmelon.scriptum.repository.alarm.IAlarmRepo
import sgtmelon.scriptum.repository.note.INoteRepo
import sgtmelon.scriptum.repository.note.NoteRepo
import sgtmelon.scriptum.repository.rank.IRankRepo
import sgtmelon.scriptum.repository.rank.RankRepo
import sgtmelon.scriptum.screen.ui.callback.note.text.ITextNoteBridge
import sgtmelon.scriptum.screen.vm.note.TextNoteViewModel
import java.util.*

/**
 * Interactor for [TextNoteViewModel]
 */
class TextNoteInteractor(context: Context, private var callback: ITextNoteBridge?) :
        ParentInteractor(context),
        ITextNoteInteractor {

    private val iAlarmRepo: IAlarmRepo = AlarmRepo(context)
    private val iRankRepo: IRankRepo = RankRepo(context)
    private val iNoteRepo: INoteRepo = NoteRepo(context)

    private val rankIdVisibleList: List<Long> = iRankRepo.getIdVisibleList()

    override fun onDestroy(func: () -> Unit) = super.onDestroy { callback = null }


    override fun getSaveModel() = with(iPreferenceRepo) {
        SaveControl.Model(pauseSaveOn, autoSaveOn, savePeriod)
    }

    @Theme override val theme: Int get() = iPreferenceRepo.theme

    @Color override val defaultColor: Int get() = iPreferenceRepo.defaultColor


    override fun isRankEmpty() = iRankRepo.isEmpty()

    override fun getItem(id: Long, updateBind: Boolean): NoteItem? {
        val item = iNoteRepo.getItem(id, optimisation = false)

        if (updateBind && item != null) callback?.notifyNoteBind(item, rankIdVisibleList)

        return item
    }

    override fun getRankDialogItemArray() = iRankRepo.getDialogItemArray()


    override fun getRankId(check: Int): Long = iRankRepo.getId(check)

    override suspend fun getDateList() = iAlarmRepo.getList().map { it.alarm.date }

    override suspend fun clearDate(noteItem: NoteItem) {
        iAlarmRepo.delete(noteItem.id)
        callback?.cancelAlarm(noteItem.id)
    }

    override suspend fun setDate(noteItem: NoteItem, calendar: Calendar) {
        iAlarmRepo.insertOrUpdate(noteItem, calendar.getString())
        callback?.setAlarm(calendar, noteItem.id)
    }

    override fun convert(noteItem: NoteItem) = iNoteRepo.convertToRoll(noteItem)


    override suspend fun restoreNote(noteItem: NoteItem) = iNoteRepo.restoreNote(noteItem)

    override suspend fun updateNote(noteItem: NoteItem, updateBind: Boolean) {
        iNoteRepo.updateNote(noteItem)

        if (updateBind) callback?.notifyNoteBind(noteItem, rankIdVisibleList)
    }

    override suspend fun clearNote(noteItem: NoteItem) = iNoteRepo.clearNote(noteItem)

    override fun saveNote(noteItem: NoteItem, isCreate: Boolean) {
        iNoteRepo.saveTextNote(noteItem, isCreate)
        iRankRepo.updateConnection(noteItem)

        callback?.notifyNoteBind(noteItem, rankIdVisibleList)
    }

    override suspend fun deleteNote(noteItem: NoteItem) {
        iNoteRepo.deleteNote(noteItem)

        callback?.cancelAlarm(noteItem.id)
        callback?.cancelNoteBind(noteItem.id.toInt())
    }

}
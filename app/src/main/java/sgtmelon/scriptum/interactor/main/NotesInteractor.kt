package sgtmelon.scriptum.interactor.main

import android.content.Context
import sgtmelon.extension.getText
import sgtmelon.scriptum.interactor.ParentInteractor
import sgtmelon.scriptum.interactor.callback.main.INotesInteractor
import sgtmelon.scriptum.model.annotation.Sort
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.repository.alarm.AlarmRepo
import sgtmelon.scriptum.repository.alarm.IAlarmRepo
import sgtmelon.scriptum.repository.note.INoteRepo
import sgtmelon.scriptum.repository.note.NoteRepo
import sgtmelon.scriptum.repository.rank.IRankRepo
import sgtmelon.scriptum.repository.rank.RankRepo
import sgtmelon.scriptum.screen.ui.callback.main.INotesBridge
import sgtmelon.scriptum.screen.vm.main.NotesViewModel
import java.util.*

/**
 * Interactor for [NotesViewModel]
 */
class NotesInteractor(context: Context, private var callback: INotesBridge?) :
        ParentInteractor(context),
        INotesInteractor {

    private val iNoteRepo: INoteRepo = NoteRepo(context)
    private val iAlarmRepo: IAlarmRepo = AlarmRepo(context)
    private val iRankRepo: IRankRepo = RankRepo(context)


    override fun onDestroy(func: () -> Unit) = super.onDestroy { callback = null }


    @Theme override val theme: Int get() = iPreferenceRepo.theme

    @Sort override val sort: Int get() = iPreferenceRepo.sort


    override suspend fun getCount() = iNoteRepo.getCount(bin = false)

    override suspend fun getList() : MutableList<NoteItem> {
        val sort = iPreferenceRepo.sort
        return iNoteRepo.getList(sort, bin = false, optimal = true, filterVisible = true)
    }

    override suspend fun isListHide() = iNoteRepo.isListHide()

    override suspend fun updateNote(noteItem: NoteItem) {
        iNoteRepo.updateNote(noteItem)

        /**
         * Need for prevent overriding noteItem rollList in list model
         */
        val noteMirror = noteItem.deepCopy(rollList = iNoteRepo.getRollList(noteItem.id))
        callback?.notifyNoteBind(noteMirror, iRankRepo.getIdVisibleList())
    }

    override suspend fun convert(noteItem: NoteItem) {
        when (noteItem.type) {
            NoteType.TEXT -> iNoteRepo.convertToRoll(noteItem)
            NoteType.ROLL -> iNoteRepo.convertToText(noteItem, useCache = false)
        }

        callback?.notifyNoteBind(noteItem, iRankRepo.getIdVisibleList())

        /**
         * Optimisation for get only first 4 items
         */
        val optimalSize = NoteItem.ROLL_OPTIMAL_SIZE
        if (noteItem.rollList.size > optimalSize) {
            noteItem.rollList.dropLast(n = noteItem.rollList.size - optimalSize)
        }
    }


    override suspend fun getDateList() = iAlarmRepo.getList().map { it.alarm.date }

    override suspend fun clearDate(noteItem: NoteItem) {
        iAlarmRepo.delete(noteItem.id)
        callback?.cancelAlarm(noteItem.id)
    }

    override suspend fun setDate(noteItem: NoteItem, calendar: Calendar) {
        iAlarmRepo.insertOrUpdate(noteItem, calendar.getText())
        callback?.setAlarm(calendar, noteItem.id)
    }


    override suspend fun copy(noteItem: NoteItem) {
        callback?.copyClipboard(iNoteRepo.getCopyText(noteItem))
    }

    override suspend fun deleteNote(noteItem: NoteItem) {
        iNoteRepo.deleteNote(noteItem)

        callback?.cancelAlarm(noteItem.id)
        callback?.cancelNoteBind(noteItem.id.toInt())
    }


    override suspend fun getNotification(noteId: Long) = iAlarmRepo.getItem(noteId)

}
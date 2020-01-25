package sgtmelon.scriptum.interactor.main

import sgtmelon.extension.getText
import sgtmelon.scriptum.interactor.ParentInteractor
import sgtmelon.scriptum.interactor.callback.main.INotesInteractor
import sgtmelon.scriptum.model.annotation.Sort
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.repository.room.callback.IAlarmRepo
import sgtmelon.scriptum.repository.room.callback.INoteRepo
import sgtmelon.scriptum.repository.room.callback.IRankRepo
import sgtmelon.scriptum.screen.ui.callback.main.INotesBridge
import sgtmelon.scriptum.screen.vm.main.NotesViewModel
import java.util.*

/**
 * Interactor for [NotesViewModel].
 */
class NotesInteractor(
        private val iPreferenceRepo: IPreferenceRepo,
        private val iNoteRepo: INoteRepo,
        private val iAlarmRepo: IAlarmRepo,
        private val iRankRepo: IRankRepo,
        private var callback: INotesBridge?
) : ParentInteractor(),
        INotesInteractor {

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
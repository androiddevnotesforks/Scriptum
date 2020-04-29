package sgtmelon.scriptum.domain.interactor.impl.main

import androidx.annotation.VisibleForTesting
import sgtmelon.extension.getText
import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.data.repository.room.callback.IAlarmRepo
import sgtmelon.scriptum.data.repository.room.callback.INoteRepo
import sgtmelon.scriptum.data.repository.room.callback.IRankRepo
import sgtmelon.scriptum.domain.interactor.callback.main.INotesInteractor
import sgtmelon.scriptum.domain.interactor.impl.ParentInteractor
import sgtmelon.scriptum.domain.model.annotation.Sort
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.presentation.screen.ui.callback.main.INotesBridge
import sgtmelon.scriptum.presentation.screen.vm.impl.main.NotesViewModel
import java.util.*

/**
 * Interactor for [NotesViewModel].
 */
class NotesInteractor(
        private val preferenceRepo: IPreferenceRepo,
        private val noteRepo: INoteRepo,
        private val alarmRepo: IAlarmRepo,
        private val rankRepo: IRankRepo,
        @VisibleForTesting var callback: INotesBridge?
) : ParentInteractor(),
        INotesInteractor {

    override fun onDestroy(func: () -> Unit) = super.onDestroy { callback = null }


    @Theme override val theme: Int get() = preferenceRepo.theme

    @Sort override val sort: Int get() = preferenceRepo.sort


    override suspend fun getCount() = noteRepo.getCount(bin = false)

    override suspend fun getList() : MutableList<NoteItem> {
        val sort = preferenceRepo.sort
        return noteRepo.getList(sort, bin = false, optimal = true, filterVisible = true)
    }

    override suspend fun isListHide() = noteRepo.isListHide()

    override suspend fun updateNote(noteItem: NoteItem) {
        noteRepo.updateNote(noteItem)

        /**
         * Need for prevent overriding noteItem rollList in list model
         */
        val noteMirror = when (noteItem) {
            is NoteItem.Text -> noteItem.deepCopy()
            is NoteItem.Roll -> noteItem.deepCopy(rollList = noteRepo.getRollList(noteItem.id))
        }

        callback?.notifyNoteBind(noteMirror, rankRepo.getIdVisibleList())
    }

    override suspend fun convert(noteItem: NoteItem): NoteItem {
        val convertItem = when (noteItem) {
            is NoteItem.Text -> noteRepo.convertNote(noteItem)
            is NoteItem.Roll -> noteRepo.convertNote(noteItem, useCache = false)
        }

        callback?.notifyNoteBind(noteItem, rankRepo.getIdVisibleList())

        /**
         * Optimisation for get only first 4 items
         */
        if (convertItem is NoteItem.Roll) {
            val previewSize = NoteItem.Roll.PREVIEW_SIZE

            with(convertItem.list) {
                if (size > previewSize) dropLast(n = size - previewSize)
            }
        }

        return convertItem
    }


    override suspend fun getDateList() = alarmRepo.getList().map { it.alarm.date }

    override suspend fun clearDate(noteItem: NoteItem) {
        alarmRepo.delete(noteItem.id)
        callback?.cancelAlarm(noteItem.id)
    }

    override suspend fun setDate(noteItem: NoteItem, calendar: Calendar) {
        alarmRepo.insertOrUpdate(noteItem, calendar.getText())
        callback?.setAlarm(calendar, noteItem.id)
    }


    override suspend fun copy(noteItem: NoteItem) {
        callback?.copyClipboard(noteRepo.getCopyText(noteItem))
    }

    override suspend fun deleteNote(noteItem: NoteItem) {
        noteRepo.deleteNote(noteItem)

        callback?.cancelAlarm(noteItem.id)
        callback?.cancelNoteBind(noteItem.id.toInt())
    }


    override suspend fun getNotification(noteId: Long) = alarmRepo.getItem(noteId)

}
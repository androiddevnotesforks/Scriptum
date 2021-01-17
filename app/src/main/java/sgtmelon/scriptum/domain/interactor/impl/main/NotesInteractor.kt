package sgtmelon.scriptum.domain.interactor.impl.main

import sgtmelon.extension.getText
import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.data.repository.room.callback.IAlarmRepo
import sgtmelon.scriptum.data.repository.room.callback.INoteRepo
import sgtmelon.scriptum.data.repository.room.callback.IRankRepo
import sgtmelon.scriptum.domain.interactor.callback.main.INotesInteractor
import sgtmelon.scriptum.domain.interactor.impl.ParentInteractor
import sgtmelon.scriptum.domain.model.annotation.Sort
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.domain.model.annotation.test.RunPrivate
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.model.item.NotificationItem
import sgtmelon.scriptum.extension.runMain
import sgtmelon.scriptum.presentation.screen.ui.callback.main.INotesBridge
import sgtmelon.scriptum.presentation.screen.vm.callback.main.INotesViewModel
import java.util.*

/**
 * Interactor for [INotesViewModel].
 */
class NotesInteractor(
    private val preferenceRepo: IPreferenceRepo,
    private val alarmRepo: IAlarmRepo,
    private val rankRepo: IRankRepo,
    private val noteRepo: INoteRepo,
    @RunPrivate var callback: INotesBridge?
) : ParentInteractor(),
    INotesInteractor {

    @RunPrivate var rankIdVisibleList: List<Long>? = null

    @RunPrivate suspend fun getRankIdVisibleList(): List<Long> {
        return rankIdVisibleList ?: rankRepo.getIdVisibleList().also { rankIdVisibleList = it }
    }

    override fun onDestroy(func: () -> Unit) = super.onDestroy { callback = null }


    @Theme override val theme: Int get() = preferenceRepo.theme

    @Sort override val sort: Int get() = preferenceRepo.sort


    override suspend fun getCount(): Int = noteRepo.getCount(isBin = false)

    override suspend fun getList(): MutableList<NoteItem> {
        val sort = preferenceRepo.sort
        return noteRepo.getList(sort, isBin = false, isOptimal = true, filterVisible = true)
    }

    override suspend fun isListHide(): Boolean = noteRepo.isListHide()

    override suspend fun updateNote(item: NoteItem) {
        noteRepo.updateNote(item)

        val noteMirror = makeMirror(item)
        val rankIdList = getRankIdVisibleList()
        runMain { callback?.notifyNoteBind(noteMirror, rankIdList, preferenceRepo.sort) }
    }

    /**
     * Need for prevent overriding noteItem rollList in list model
     */
    @RunPrivate suspend fun makeMirror(item: NoteItem): NoteItem {
        return when (item) {
            is NoteItem.Text -> item.deepCopy()
            is NoteItem.Roll -> item.deepCopy(list = noteRepo.getRollList(item.id))
        }
    }

    override suspend fun convertNote(item: NoteItem): NoteItem {
        val convertItem = when (item) {
            is NoteItem.Text -> noteRepo.convertNote(item)
            is NoteItem.Roll -> noteRepo.convertNote(item, useCache = false)
        }

        val rankIdList = getRankIdVisibleList()
        runMain { callback?.notifyNoteBind(item, rankIdList, preferenceRepo.sort) }

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


    /**
     * TODO make common
     */
    override suspend fun getDateList(): List<String> = alarmRepo.getList().map { it.alarm.date }

    override suspend fun clearDate(item: NoteItem) {
        alarmRepo.delete(item.id)

        runMain { callback?.cancelAlarm(item.id) }
    }

    override suspend fun setDate(item: NoteItem, calendar: Calendar) {
        alarmRepo.insertOrUpdate(item, calendar.getText())

        runMain { callback?.setAlarm(calendar, item.id) }
    }


    override suspend fun copy(item: NoteItem) {
        val text = noteRepo.getCopyText(item)
        runMain { callback?.copyClipboard(text) }
    }

    override suspend fun deleteNote(item: NoteItem) {
        noteRepo.deleteNote(item)

        runMain {
            callback?.cancelAlarm(item.id)
            callback?.cancelNoteBind(item.id)
        }
    }


    override suspend fun getNotification(noteId: Long): NotificationItem? {
        return alarmRepo.getItem(noteId)
    }
}
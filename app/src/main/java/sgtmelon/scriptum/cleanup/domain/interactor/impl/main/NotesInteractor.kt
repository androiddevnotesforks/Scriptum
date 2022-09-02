package sgtmelon.scriptum.cleanup.domain.interactor.impl.main

import java.util.Calendar
import sgtmelon.common.test.annotation.RunPrivate
import sgtmelon.scriptum.cleanup.data.repository.room.callback.AlarmRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.cleanup.domain.interactor.callback.main.INotesInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.ParentInteractor
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.main.INotesViewModel
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo

/**
 * Interactor for [INotesViewModel].
 */
class NotesInteractor(
    private val preferenceRepo: PreferencesRepo,
    private val alarmRepo: AlarmRepo,
    private val noteRepo: NoteRepo
) : ParentInteractor(),
    INotesInteractor {

    override suspend fun getCount(): Int = noteRepo.getCount(isBin = false)

    override suspend fun getList(): MutableList<NoteItem> {
        val sort = preferenceRepo.sort
        return noteRepo.getList(sort, isBin = false, isOptimal = true, filterVisible = true)
    }

    override suspend fun isListHide(): Boolean = noteRepo.isListHide()

    override suspend fun updateNote(item: NoteItem) = noteRepo.updateNote(item)

    override suspend fun convertNote(item: NoteItem): NoteItem {
        val convertItem = when (item) {
            is NoteItem.Text -> noteRepo.convertNote(item)
            is NoteItem.Roll -> noteRepo.convertNote(item, useCache = false)
        }

        if (convertItem is NoteItem.Roll) {
            onConvertOptimisation(convertItem)
        }

        return convertItem
    }

    /**
     * Optimisation for get only first 4 items in roll list.
     */
    @RunPrivate fun onConvertOptimisation(item: NoteItem.Roll) {
        val previewSize = NoteItem.Roll.PREVIEW_SIZE
        val list = item.list

        while (list.isNotEmpty() && list.size > previewSize) {
            list.removeAt(list.lastIndex)
        }
    }


    /**
     * TODO make common
     */
    override suspend fun getDateList(): List<String> = alarmRepo.getList().map { it.alarm.date }

    override suspend fun clearDate(item: NoteItem) = alarmRepo.delete(item.id)

    override suspend fun setDate(item: NoteItem, calendar: Calendar) {
        alarmRepo.insertOrUpdate(item, calendar)
    }


    override suspend fun copy(item: NoteItem) = noteRepo.getCopyText(item)


    override suspend fun getNotification(noteId: Long): NotificationItem? {
        return alarmRepo.getItem(noteId)
    }
}
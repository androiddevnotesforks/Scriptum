package sgtmelon.scriptum.cleanup.domain.interactor.callback.main

import sgtmelon.scriptum.cleanup.domain.interactor.callback.IParentInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.main.NotesInteractor
import sgtmelon.scriptum.cleanup.domain.model.annotation.Sort
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.main.INotesViewModel
import java.util.*

/**
 * Interface for communication [INotesViewModel] with [NotesInteractor].
 */
interface INotesInteractor : IParentInteractor {

    @Sort val sort: Int


    suspend fun getCount(): Int

    suspend fun getList(): MutableList<NoteItem>

    suspend fun isListHide(): Boolean

    suspend fun updateNote(item: NoteItem)

    suspend fun convertNote(item: NoteItem): NoteItem


    suspend fun getDateList(): List<String>

    suspend fun clearDate(item: NoteItem)

    suspend fun setDate(item: NoteItem, calendar: Calendar)


    suspend fun copy(item: NoteItem): String

    suspend fun deleteNote(item: NoteItem)


    suspend fun getNotification(noteId: Long): NotificationItem?

}
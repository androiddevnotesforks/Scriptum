package sgtmelon.scriptum.domain.interactor.callback.main

import sgtmelon.scriptum.domain.interactor.callback.IParentInteractor
import sgtmelon.scriptum.domain.interactor.main.NotesInteractor
import sgtmelon.scriptum.domain.model.annotation.Sort
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.model.item.NotificationItem
import sgtmelon.scriptum.presentation.screen.vm.impl.main.NotesViewModel
import java.util.*

/**
 * Interface for communication [NotesViewModel] with [NotesInteractor]
 */
interface INotesInteractor : IParentInteractor {

    @Theme val theme: Int

    @Sort val sort: Int


    suspend fun getCount(): Int

    suspend fun getList(): MutableList<NoteItem>

    suspend fun isListHide(): Boolean

    suspend fun updateNote(noteItem: NoteItem)

    suspend fun convert(noteItem: NoteItem)


    suspend fun getDateList(): List<String>

    suspend fun clearDate(noteItem: NoteItem)

    suspend fun setDate(noteItem: NoteItem, calendar: Calendar)


    suspend fun copy(noteItem: NoteItem)

    suspend fun deleteNote(noteItem: NoteItem)


    suspend fun getNotification(noteId: Long): NotificationItem?

}
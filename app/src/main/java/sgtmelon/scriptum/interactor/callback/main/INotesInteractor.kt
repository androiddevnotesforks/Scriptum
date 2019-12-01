package sgtmelon.scriptum.interactor.callback.main

import sgtmelon.scriptum.interactor.callback.IParentInteractor
import sgtmelon.scriptum.interactor.main.NotesInteractor
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.item.NotificationItem
import sgtmelon.scriptum.screen.vm.main.NotesViewModel
import java.util.*

/**
 * Interface for communication [NotesViewModel] with [NotesInteractor]
 */
interface INotesInteractor : IParentInteractor {

    @Theme val theme: Int

    fun getList(): MutableList<NoteItem>

    fun isListHide(): Boolean

    suspend fun updateNote(noteItem: NoteItem)

    fun convert(noteItem: NoteItem)


    suspend fun getDateList(): List<String>

    suspend fun clearDate(noteItem: NoteItem)

    suspend fun setDate(noteItem: NoteItem, calendar: Calendar)


    suspend fun copy(noteItem: NoteItem)

    suspend fun deleteNote(noteItem: NoteItem)


    suspend fun getNotification(noteItem: NoteItem): NotificationItem?

}
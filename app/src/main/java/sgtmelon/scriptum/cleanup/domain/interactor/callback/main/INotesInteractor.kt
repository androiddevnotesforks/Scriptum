package sgtmelon.scriptum.cleanup.domain.interactor.callback.main

import sgtmelon.scriptum.cleanup.domain.interactor.callback.IParentInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.main.NotesInteractor
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.main.INotesViewModel

/**
 * Interface for communication [INotesViewModel] with [NotesInteractor].
 */
interface INotesInteractor : IParentInteractor {

    suspend fun getCount(): Int

    suspend fun getList(): MutableList<NoteItem>

    suspend fun isListHide(): Boolean

    suspend fun updateNote(item: NoteItem)

    suspend fun convertNote(item: NoteItem): NoteItem


    suspend fun clearDate(item: NoteItem)


    suspend fun getNotification(noteId: Long): NotificationItem?

}
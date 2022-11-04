package sgtmelon.scriptum.cleanup.domain.interactor.impl.main

import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.cleanup.domain.interactor.callback.main.INotesInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.ParentInteractor
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.screen.main.notes.NotesViewModel
import sgtmelon.test.prod.RunPrivate

/**
 * Interactor for [NotesViewModel].
 */
// TODO refactor after notesViewModel
class NotesInteractor(private val noteRepo: NoteRepo) : ParentInteractor(),
    INotesInteractor {

//    override suspend fun getCount(): Int = noteRepo.getCount(isBin = false)


    //    override suspend fun isListHide(): Boolean = noteRepo.isListHide()

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
}
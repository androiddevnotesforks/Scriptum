package sgtmelon.scriptum.cleanup.domain.interactor.impl.note

import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.cleanup.domain.interactor.callback.note.ITextNoteInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.ParentInteractor
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.note.ITextNoteViewModel

/**
 * Interactor for [ITextNoteViewModel].
 */
class TextNoteInteractor(
    private val noteRepo: NoteRepo
) : ParentInteractor(),
    ITextNoteInteractor {

    override suspend fun getItem(id: Long): NoteItem.Text? {
        val noteItem = noteRepo.getItem(id, isOptimal = false)

        if (noteItem !is NoteItem.Text) return null

        return noteItem
    }


    override suspend fun convertNote(item: NoteItem.Text) {
        noteRepo.convertNote(item)
    }
}
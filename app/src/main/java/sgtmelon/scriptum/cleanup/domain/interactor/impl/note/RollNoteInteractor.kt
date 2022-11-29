package sgtmelon.scriptum.cleanup.domain.interactor.impl.note

import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.cleanup.domain.interactor.callback.note.IRollNoteInteractor
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.note.IRollNoteViewModel

/**
 * Interactor for [IRollNoteViewModel].
 */
class RollNoteInteractor(
    private val noteRepo: NoteRepo
) : IRollNoteInteractor {

    override suspend fun convertNote(item: NoteItem.Roll) {
        noteRepo.convertNote(item, useCache = true)
    }
}
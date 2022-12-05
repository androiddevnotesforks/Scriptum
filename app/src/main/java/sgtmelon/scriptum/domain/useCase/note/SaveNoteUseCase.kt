package sgtmelon.scriptum.domain.useCase.note

import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.RankRepo
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem

class SaveNoteUseCase(
    private val noteRepo: NoteRepo,
    private val rankRepo: RankRepo
) {

    suspend operator fun invoke(item: NoteItem, isCreate: Boolean) {
        when (item) {
            is NoteItem.Text -> noteRepo.saveNote(item, isCreate)
            is NoteItem.Roll -> noteRepo.saveNote(item, isCreate)
        }

        rankRepo.updateConnection(item)
    }
}
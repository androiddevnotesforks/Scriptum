package sgtmelon.scriptum.domain.useCase.note

import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem

class ConvertNoteUseCase(private val repository: NoteRepo) {

    suspend operator fun invoke(item: NoteItem.Text) = repository.convertNote(item)

    suspend operator fun invoke(item: NoteItem.Roll) = repository.convertNote(item)

    suspend operator fun invoke(item: NoteItem): NoteItem {
        return when (item) {
            is NoteItem.Text -> invoke(item)
            is NoteItem.Roll -> invoke(item)
        }
    }
}
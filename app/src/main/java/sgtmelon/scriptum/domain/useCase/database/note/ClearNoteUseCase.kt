package sgtmelon.scriptum.domain.useCase.database.note

import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem

interface ClearNoteUseCase {

    suspend operator fun invoke(item: NoteItem)
}
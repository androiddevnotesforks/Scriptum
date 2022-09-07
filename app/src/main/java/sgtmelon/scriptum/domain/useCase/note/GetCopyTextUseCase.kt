package sgtmelon.scriptum.domain.useCase.note

import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem

interface GetCopyTextUseCase {

    suspend operator fun invoke(item: NoteItem): String
}
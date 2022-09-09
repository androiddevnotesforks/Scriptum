package sgtmelon.scriptum.domain.useCase.main

import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem

interface GetNoteListUseCase {

    suspend operator fun invoke(isBin: Boolean): List<NoteItem>
}
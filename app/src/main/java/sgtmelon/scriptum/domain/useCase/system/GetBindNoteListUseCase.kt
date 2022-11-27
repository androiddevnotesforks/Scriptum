package sgtmelon.scriptum.domain.useCase.system

import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo

class GetBindNoteListUseCase(
    private val preferencesRepo: PreferencesRepo,
    private val noteRepo: NoteRepo
) {

    suspend operator fun invoke(): List<NoteItem> = noteRepo.getBindNoteList(preferencesRepo.sort)
}
package sgtmelon.scriptum.domain.useCase.main

import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo

class GetNotesListUseCase(
    private val preferencesRepo: PreferencesRepo,
    private val noteRepo: NoteRepo
) {

    suspend operator fun invoke(): Pair<List<NoteItem>, Boolean> {
        return noteRepo.getNotesList(preferencesRepo.sort)
    }
}
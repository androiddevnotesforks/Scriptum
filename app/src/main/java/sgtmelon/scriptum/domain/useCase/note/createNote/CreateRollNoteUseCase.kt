package sgtmelon.scriptum.domain.useCase.note.createNote

import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo

class CreateRollNoteUseCase(val repository: PreferencesRepo) : CreateNoteUseCase<NoteItem.Roll> {

    override operator fun invoke(): NoteItem.Roll = NoteItem.Roll(color = repository.defaultColor)

}
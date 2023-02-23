package sgtmelon.scriptum.domain.useCase.note.createNote

import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo

class CreateTextNoteUseCase(val repository: PreferencesRepo) : CreateNoteUseCase<NoteItem.Text> {

    override operator fun invoke(): NoteItem.Text = NoteItem.Text(color = repository.defaultColor)

}
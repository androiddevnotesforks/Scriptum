package sgtmelon.scriptum.domain.useCase.note.createNote

import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType

class CreateNoteUseCase(
    private val createText: CreateTypeNoteUseCase<NoteItem.Text>,
    private val createRoll: CreateTypeNoteUseCase<NoteItem.Roll>
) {

    operator fun invoke(type: NoteType): NoteItem {
        return when (type) {
            NoteType.TEXT -> createText()
            NoteType.ROLL -> createRoll()
        }
    }
}
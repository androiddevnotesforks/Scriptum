package sgtmelon.scriptum.infrastructure.screen.note

import androidx.lifecycle.ViewModel
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType

class NoteViewModelImpl : ViewModel(),
    NoteViewModel {

    override fun convertType(type: NoteType): NoteType {
        return when (type) {
            NoteType.TEXT -> NoteType.ROLL
            NoteType.ROLL -> NoteType.TEXT
        }
    }
}
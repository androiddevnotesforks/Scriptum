package sgtmelon.scriptum.infrastructure.screen.note

import androidx.lifecycle.ViewModel
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType

class NoteViewModelImpl(
    private val preferencesRepo: PreferencesRepo
) : ViewModel(),
    NoteViewModel {

    override val defaultColor: Color get() = preferencesRepo.defaultColor

    override fun convertType(type: NoteType): NoteType {
        return when (type) {
            NoteType.TEXT -> NoteType.ROLL
            NoteType.ROLL -> NoteType.TEXT
        }
    }
}
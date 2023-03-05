package sgtmelon.scriptum.infrastructure.model.init

import sgtmelon.scriptum.infrastructure.model.key.NoteState
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType

data class NoteInit(
    var state: NoteState,
    var isEdit: Boolean,
    var id: Long,
    var type: NoteType,
    var color: Color,
    var name: String
)
package sgtmelon.scriptum.infrastructure.model.init

import sgtmelon.scriptum.infrastructure.model.key.NoteState
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType

data class NoteInit(
    var isEdit: Boolean,
    var state: NoteState,
    var id: Long,
    var type: NoteType,
    var color: Color,
    var name: String
)
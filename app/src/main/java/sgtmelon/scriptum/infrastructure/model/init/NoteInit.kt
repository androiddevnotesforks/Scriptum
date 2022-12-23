package sgtmelon.scriptum.infrastructure.model.init

import sgtmelon.scriptum.infrastructure.model.key.NoteState
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType

class NoteInit(
    var isEdit: Boolean,
    var noteState: NoteState,
    var id: Long,
    var type: NoteType,
    var color: Color
)
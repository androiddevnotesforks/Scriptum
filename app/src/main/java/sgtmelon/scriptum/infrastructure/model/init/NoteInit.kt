package sgtmelon.scriptum.infrastructure.model.init

import kotlinx.serialization.Serializable
import sgtmelon.scriptum.infrastructure.model.key.NoteState
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType

@Serializable
data class NoteInit(
    var state: NoteState,
    var isEdit: Boolean,
    var id: Long,
    var type: NoteType,
    var color: Color?,
    var name: String // TODO после того, как сюда будет добавлена моделька заметки, можно будет убрать
)
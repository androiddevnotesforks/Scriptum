package sgtmelon.scriptum.infrastructure.model.init

import kotlinx.serialization.Serializable
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.model.key.NoteState

@Serializable
data class NoteInit(
    var state: NoteState,
    var isEdit: Boolean,
    var noteItem: NoteItem
)
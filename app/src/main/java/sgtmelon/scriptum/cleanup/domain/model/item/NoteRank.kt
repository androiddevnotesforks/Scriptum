package sgtmelon.scriptum.cleanup.domain.model.item

import kotlinx.serialization.Serializable
import sgtmelon.scriptum.infrastructure.database.DbData.Note

@Serializable
data class NoteRank(var id: Long = Note.Default.RANK_ID, var position: Int = Note.Default.RANK_PS)
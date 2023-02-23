package sgtmelon.scriptum.cleanup.domain.model.item

import sgtmelon.scriptum.infrastructure.database.DbData.Note

data class NoteRank(var id: Long = Note.Default.RANK_ID, var position: Int = Note.Default.RANK_PS)
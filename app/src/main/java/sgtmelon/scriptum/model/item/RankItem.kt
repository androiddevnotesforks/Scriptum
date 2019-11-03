package sgtmelon.scriptum.model.item

import androidx.room.ColumnInfo
import androidx.room.TypeConverters
import sgtmelon.scriptum.adapter.RankAdapter
import sgtmelon.scriptum.model.data.DbData
import sgtmelon.scriptum.room.converter.BoolConverter
import sgtmelon.scriptum.room.converter.StringConverter

/**
 * Model for store short information about rank, use in [RankAdapter]
 */
@TypeConverters(BoolConverter::class, StringConverter::class)
data class RankItem(
        @ColumnInfo(name = DbData.Rank.ID) val id: Long,
        @ColumnInfo(name = DbData.Rank.NOTE_ID) val noteId: MutableList<Long>,
        @ColumnInfo(name = DbData.Rank.POSITION) var position: Int,
        @ColumnInfo(name = DbData.Rank.NAME) var name: String,
        @ColumnInfo(name = DbData.Rank.VISIBLE) var isVisible: Boolean
)
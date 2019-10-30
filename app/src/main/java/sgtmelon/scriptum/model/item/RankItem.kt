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
        @ColumnInfo(name = DbData.Rank.ID) val id: Long
)
package sgtmelon.scriptum.model.item

import androidx.room.ColumnInfo
import androidx.room.TypeConverters
import sgtmelon.scriptum.adapter.RollAdapter
import sgtmelon.scriptum.model.data.DbData.Roll
import sgtmelon.scriptum.room.converter.BoolConverter

/**
 * Model for store short information about roll, use in [RollAdapter]
 */
@TypeConverters(BoolConverter::class)
data class RollItem(
        @ColumnInfo(name = Roll.ID) var id: Long?,
        @ColumnInfo(name = Roll.POSITION) var position: Int,
        @ColumnInfo(name = Roll.CHECK) var isCheck: Boolean,
        @ColumnInfo(name = Roll.TEXT) var text: String
)
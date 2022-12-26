package sgtmelon.scriptum.cleanup.domain.model.item

import androidx.room.ColumnInfo
import androidx.room.TypeConverters
import sgtmelon.scriptum.cleanup.presentation.adapter.RollAdapter
import sgtmelon.scriptum.infrastructure.converter.types.BoolConverter
import sgtmelon.scriptum.infrastructure.database.DbData.Roll
import sgtmelon.scriptum.infrastructure.database.DbData.Roll.Default

/**
 * Model for store short information about roll, use in [RollAdapter].
 */
@TypeConverters(BoolConverter::class)
data class RollItem(
    @ColumnInfo(name = Roll.ID) var id: Long? = Default.ID,
    @ColumnInfo(name = Roll.POSITION) var position: Int,
    @ColumnInfo(name = Roll.CHECK) var isCheck: Boolean = Default.CHECK,
    @ColumnInfo(name = Roll.TEXT) var text: String
)
package sgtmelon.scriptum.model.item

import androidx.room.ColumnInfo
import androidx.room.TypeConverters
import sgtmelon.scriptum.adapter.RankAdapter
import sgtmelon.scriptum.model.data.DbData.Rank
import sgtmelon.scriptum.model.data.DbData.Rank.Default
import sgtmelon.scriptum.room.converter.type.BoolConverter
import sgtmelon.scriptum.room.converter.model.StringConverter

/**
 * Model for store short information about rank, use in [RankAdapter]
 */
@TypeConverters(BoolConverter::class, StringConverter::class)
data class RankItem(
        @ColumnInfo(name = Rank.ID) val id: Long,
        @ColumnInfo(name = Rank.NOTE_ID) val noteId: MutableList<Long> = Default.NOTE_ID,
        @ColumnInfo(name = Rank.POSITION) var position: Int = Default.POSITION,
        @ColumnInfo(name = Rank.NAME) var name: String,
        @ColumnInfo(name = Rank.VISIBLE) var isVisible: Boolean = Default.VISIBLE
) {

    fun switchVisible() = apply { isVisible = !isVisible }

}
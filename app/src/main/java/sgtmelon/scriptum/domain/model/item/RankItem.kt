package sgtmelon.scriptum.domain.model.item

import androidx.room.ColumnInfo
import androidx.room.TypeConverters
import sgtmelon.scriptum.data.room.converter.type.BoolConverter
import sgtmelon.scriptum.data.room.converter.type.StringConverter
import sgtmelon.scriptum.domain.model.data.DbData.Rank
import sgtmelon.scriptum.domain.model.data.DbData.Rank.Default
import sgtmelon.scriptum.presentation.adapter.RankAdapter

/**
 * Model for store short information about rank, use in [RankAdapter].
 */
@TypeConverters(BoolConverter::class, StringConverter::class)
data class RankItem(
    @ColumnInfo(name = Rank.ID) val id: Long,
    @ColumnInfo(name = Rank.NOTE_ID) val noteId: MutableList<Long> = Default.NOTE_ID,
    @ColumnInfo(name = Rank.POSITION) var position: Int = Default.POSITION,
    @ColumnInfo(name = Rank.NAME) var name: String,
    @ColumnInfo(name = Rank.VISIBLE) var isVisible: Boolean = Default.VISIBLE,
    var bindCount: Int = ND_BIND_COUNT,
    var notificationCount: Int = ND_NOTIFICATION_COUNT
) {

    fun switchVisible() = apply { isVisible = !isVisible }

    companion object {
        const val ND_BIND_COUNT = 0
        const val ND_NOTIFICATION_COUNT = 0
    }
}
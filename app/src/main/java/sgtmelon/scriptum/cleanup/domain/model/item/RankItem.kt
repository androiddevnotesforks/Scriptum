package sgtmelon.scriptum.cleanup.domain.model.item

import androidx.room.ColumnInfo
import androidx.room.TypeConverters
import sgtmelon.scriptum.infrastructure.adapter.RankAdapter
import sgtmelon.scriptum.infrastructure.converter.types.BoolConverter
import sgtmelon.scriptum.infrastructure.converter.types.LongListConverter
import sgtmelon.scriptum.infrastructure.database.DbData.Rank
import sgtmelon.scriptum.infrastructure.database.DbData.Rank.Default

/**
 * Model for store short information about rank, use in [RankAdapter].
 */
@TypeConverters(BoolConverter::class, LongListConverter::class)
data class RankItem(
    @ColumnInfo(name = Rank.ID) val id: Long,
    @ColumnInfo(name = Rank.NOTE_ID) val noteId: MutableList<Long> = Default.NOTE_ID,
    @ColumnInfo(name = Rank.POSITION) var position: Int = Default.POSITION,
    @ColumnInfo(name = Rank.NAME) var name: String,
    @ColumnInfo(name = Rank.VISIBLE) var isVisible: Boolean = Default.VISIBLE,
    var bindCount: Int = Default.BIND_COUNT,
    var notificationCount: Int = Default.NOTIFICATION_COUNT
)
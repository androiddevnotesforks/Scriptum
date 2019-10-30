package sgtmelon.scriptum.room.entity

import androidx.room.*
import sgtmelon.scriptum.adapter.RankAdapter
import sgtmelon.scriptum.model.data.DbData.Rank
import sgtmelon.scriptum.model.data.DbData.Rank.Default
import sgtmelon.scriptum.room.converter.BoolConverter
import sgtmelon.scriptum.room.converter.StringConverter

/**
 * Element of list in [RankAdapter]
 */
@Entity(
        tableName = Rank.TABLE,
        indices = [Index(value = [Rank.NAME], name = Rank.INDEX_NAME, unique = true)]
)
@TypeConverters(BoolConverter::class, StringConverter::class)
data class RankEntity(
        @ColumnInfo(name = Rank.ID) @PrimaryKey(autoGenerate = true) var id: Long = Default.ID,
        @ColumnInfo(name = Rank.NOTE_ID) var noteId: MutableList<Long> = Default.NOTE_ID,
        @ColumnInfo(name = Rank.POSITION) var position: Int = Default.POSITION,
        @ColumnInfo(name = Rank.NAME) var name: String = Default.NAME,
        @ColumnInfo(name = Rank.VISIBLE) var isVisible: Boolean = Default.VISIBLE
)
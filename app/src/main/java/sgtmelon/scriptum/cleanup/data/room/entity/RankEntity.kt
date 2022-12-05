package sgtmelon.scriptum.cleanup.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import sgtmelon.scriptum.cleanup.data.room.converter.type.BoolConverter
import sgtmelon.scriptum.cleanup.data.room.converter.type.StringConverter
import sgtmelon.scriptum.cleanup.domain.model.data.DbData.Rank
import sgtmelon.scriptum.cleanup.domain.model.data.DbData.Rank.Default
import sgtmelon.scriptum.cleanup.domain.model.data.DbData.Rank.Room

/**
 * Entity of category.
 */
@Entity(
    tableName = Rank.TABLE,
    indices = [Index(value = [Rank.NAME], name = Rank.INDEX_NAME, unique = true)]
)
@TypeConverters(BoolConverter::class, StringConverter::class)
data class RankEntity(
    @ColumnInfo(name = Rank.ID, defaultValue = Room.ID)
    @PrimaryKey(autoGenerate = true)
    var id: Long = Default.ID,

    @ColumnInfo(name = Rank.NOTE_ID, defaultValue = Room.NOTE_ID)
    val noteId: MutableList<Long> = Default.NOTE_ID,

    @ColumnInfo(name = Rank.POSITION, defaultValue = Room.POSITION)
    var position: Int = Default.POSITION,

    @ColumnInfo(name = Rank.NAME, defaultValue = Room.NAME)
    var name: String = Default.NAME,

    @ColumnInfo(name = Rank.VISIBLE, defaultValue = Room.VISIBLE)
    var isVisible: Boolean = Default.VISIBLE
) {

    companion object {
        operator fun get(name: String): RankEntity = RankEntity(name = name)
    }
}
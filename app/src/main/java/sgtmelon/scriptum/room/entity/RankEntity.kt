package sgtmelon.scriptum.room.entity

import androidx.room.*
import sgtmelon.scriptum.adapter.RankAdapter
import sgtmelon.scriptum.model.data.DbData.Rank
import sgtmelon.scriptum.room.converter.BoolConverter
import sgtmelon.scriptum.room.converter.StringConverter

/**
 * Element of list in [RankAdapter]
 */
@Entity(tableName = Rank.TABLE, indices = [Index(value = [Rank.NAME], unique = true)])
@TypeConverters(BoolConverter::class, StringConverter::class)
data class RankEntity(
        @ColumnInfo(name = Rank.ID) @PrimaryKey(autoGenerate = true) var id: Long = ND_ID,
        @ColumnInfo(name = Rank.NOTE_ID) var noteId: MutableList<Long> = ArrayList(),
        @ColumnInfo(name = Rank.POSITION) var position: Int = ND_POSITION,
        @ColumnInfo(name = Rank.NAME) var name: String = ND_NAME,
        @ColumnInfo(name = Rank.VISIBLE) var isVisible: Boolean = ND_VISIBLE
) {

    companion object {
        const val ND_ID = 0L
        const val ND_POSITION = 0
        const val ND_NAME = ""
        const val ND_VISIBLE = true
    }

}
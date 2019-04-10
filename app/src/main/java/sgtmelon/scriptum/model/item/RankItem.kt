package sgtmelon.scriptum.model.item

import androidx.room.*
import sgtmelon.scriptum.model.RankModel
import sgtmelon.scriptum.model.key.DbField.Rank
import sgtmelon.scriptum.room.converter.BoolConverter
import sgtmelon.scriptum.room.converter.StringConverter
import java.util.*

/**
 * Элемент списка категорий [RankModel]
 */
@Entity(tableName = Rank.TABLE)
@TypeConverters(BoolConverter::class, StringConverter::class)
class RankItem(
        @ColumnInfo(name = Rank.ID) @PrimaryKey(autoGenerate = true) var id: Long = 0,
        @ColumnInfo(name = Rank.NOTE_ID) var noteId: MutableList<Long> = ArrayList(),
        @ColumnInfo(name = Rank.POSITION) var position: Int = 0,
        @ColumnInfo(name = Rank.NAME) var name: String = "",
        @ColumnInfo(name = Rank.VISIBLE) var isVisible: Boolean = true
) {

    @Ignore var textCount = 0
    @Ignore var rollCount = 0

}
package sgtmelon.scriptum.model.item

import androidx.room.ColumnInfo
import androidx.room.TypeConverters
import org.json.JSONObject
import sgtmelon.scriptum.adapter.RollAdapter
import sgtmelon.scriptum.model.data.DbData.Roll
import sgtmelon.scriptum.room.converter.BoolConverter
import sgtmelon.scriptum.room.entity.RollEntity

/**
 * Model for store short information about roll, use in [RollAdapter]
 */
@TypeConverters(BoolConverter::class)
data class RollItem(
        @ColumnInfo(name = Roll.ID) var id: Long? = Roll.Default.ID,
        @ColumnInfo(name = Roll.POSITION) var position: Int,
        @ColumnInfo(name = Roll.CHECK) var isCheck: Boolean = Roll.Default.CHECK,
        @ColumnInfo(name = Roll.TEXT) var text: String
) {

    /**
     * TODO #TEST write unit test
     */

    /**
     * Replace [id] null value to -1 for [get] function
     */
    override fun toString() = JSONObject().apply {
        put(Roll.ID, if (id != null) id else -1L)
        put(Roll.POSITION, position)
        put(Roll.CHECK, isCheck)
        put(Roll.TEXT, text)
    }.toString()

    companion object {
        operator fun get(data: String): RollItem? = try {
            JSONObject(data).let {
                val id = it.getLong(Roll.ID)
                return@let RollItem(
                        if (id != -1L) id else null,
                        it.getInt(Roll.POSITION),
                        it.getBoolean(Roll.CHECK),
                        it.getString(Roll.TEXT)
                )
            }
        } catch (e: Throwable) {
            null
        }
    }

}
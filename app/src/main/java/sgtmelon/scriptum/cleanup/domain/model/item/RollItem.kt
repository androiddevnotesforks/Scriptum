package sgtmelon.scriptum.cleanup.domain.model.item

import androidx.room.ColumnInfo
import androidx.room.TypeConverters
import org.json.JSONObject
import sgtmelon.scriptum.cleanup.data.room.converter.type.BoolConverter
import sgtmelon.scriptum.cleanup.domain.model.data.DbData.Roll
import sgtmelon.scriptum.cleanup.domain.model.data.DbData.Roll.Default
import sgtmelon.scriptum.cleanup.domain.model.item.RollItem.Companion.get
import sgtmelon.scriptum.cleanup.presentation.adapter.RollAdapter

/**
 * Model for store short information about roll, use in [RollAdapter].
 */
@TypeConverters(BoolConverter::class)
data class RollItem(
    @ColumnInfo(name = Roll.ID) var id: Long? = Default.ID,
    @ColumnInfo(name = Roll.POSITION) var position: Int,
    @ColumnInfo(name = Roll.CHECK) var isCheck: Boolean = Default.CHECK,
    @ColumnInfo(name = Roll.TEXT) var text: String
) {
    /**
     * Replace [id] null value to -1 for [get] function
     */
    // TODO add converter toJson and back
    fun toJson(): String = JSONObject().apply {
        put(Roll.ID, if (id != null) id else -1L)
        put(Roll.POSITION, position)
        put(Roll.CHECK, isCheck)
        put(Roll.TEXT, text)
    }.toString()

    companion object {

        // TODO add converter toJson and back
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
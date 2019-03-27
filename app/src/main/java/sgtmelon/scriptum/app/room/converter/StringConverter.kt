package sgtmelon.scriptum.app.room.converter

import androidx.room.TypeConverter
import sgtmelon.scriptum.app.model.key.DbField.Value
import sgtmelon.scriptum.app.model.key.DbField
import java.util.*

/**
 * Преобразование String - List<Long>
 * В строке разделителем является DIVIDER [DbField]
 */
class StringConverter {

    @TypeConverter
    fun toList(string: String): MutableList<Long> = ArrayList<Long>().apply {
        if (string != Value.NONE && string.isNotEmpty()) {
            string.split(Value.DIVIDER.toRegex())
                    .dropLastWhile { it.isEmpty() }
                    .forEach { add(it.toLong()) }
        }
    }

    @TypeConverter
    fun toString(list: List<Long>?) =
            if (list != null && list.isNotEmpty()) list.joinToString(Value.DIVIDER) else Value.NONE

}
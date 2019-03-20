package sgtmelon.scriptum.app.room.converter

import androidx.room.TypeConverter
import sgtmelon.scriptum.office.annot.DbAnn
import java.util.*

/**
 * Преобразование String - List<Long>
 * В строке разделителем является DIVIDER [DbAnn]
 */
class StringConverter {

    @TypeConverter
    fun toList(string: String): MutableList<Long> = ArrayList<Long>().apply {
        if (string != DbAnn.Value.NONE && string.isNotEmpty()) {
            string.split(DbAnn.Value.DIVIDER.toRegex())
                    .dropLastWhile { it.isEmpty() }
                    .forEach { add(it.toLong()) }
        }
    }

    @TypeConverter
    fun toString(list: List<Long>?) = if (list != null && list.isNotEmpty()) {
        list.joinToString(DbAnn.Value.DIVIDER)
    } else {
        DbAnn.Value.NONE
    }

}
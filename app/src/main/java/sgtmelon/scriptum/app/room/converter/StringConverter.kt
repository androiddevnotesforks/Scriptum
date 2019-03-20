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
    fun fromString(value: String): List<Long> = ArrayList<Long>().apply {
        if (value != DbAnn.Value.NONE && value.isNotEmpty()) {
            value.split(DbAnn.Value.DIVIDER.toRegex())
                    .dropLastWhile { it.isEmpty() }
                    .forEach { add(it.toLong()) }
        }
    }

    @TypeConverter
    fun toString(value: List<Long>?) = if (value != null && value.isNotEmpty()) {
        value.joinToString(DbAnn.Value.DIVIDER)
    } else {
        DbAnn.Value.NONE
    }

}
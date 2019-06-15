package sgtmelon.scriptum.room.converter

import androidx.room.TypeConverter
import java.util.*

/**
 * Преобразование String - List<Long>
 *
 * @author SerjantArbuz
 */
class StringConverter {

    @TypeConverter fun toList(string: String): MutableList<Long> = ArrayList<Long>().apply {
        if (string != NONE && string.isNotEmpty()) {
            string.split(DIVIDER.toRegex())
                    .dropLastWhile { it.isEmpty() }
                    .forEach { add(it.toLong()) }
        }
    }

    @TypeConverter fun toString(list: List<Long>?) =
            if (list != null && list.isNotEmpty()) list.joinToString(DIVIDER) else NONE

    private companion object {
        const val NONE = "NONE"
        const val DIVIDER = ","
    }

}
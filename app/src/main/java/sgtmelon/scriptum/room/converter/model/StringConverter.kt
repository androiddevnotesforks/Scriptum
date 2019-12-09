package sgtmelon.scriptum.room.converter.model

import androidx.room.TypeConverter
import java.util.*

/**
 * Converter from string to long array and vice versa
 */
class StringConverter {

    @TypeConverter fun toList(string: String): MutableList<Long> = ArrayList<Long>().apply {
        if (string != NONE && string.isNotEmpty()) {
            addAll(string.split(", ".toRegex())
                    .dropLastWhile { it.isEmpty() }
                    .map { it.toLong() })
        }
    }

    @TypeConverter fun toString(list: List<Long>?) =
            if (list != null && list.isNotEmpty()) list.joinToString() else NONE

    companion object {
        const val NONE = "NONE"
    }

}
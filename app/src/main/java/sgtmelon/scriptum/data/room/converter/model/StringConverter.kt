package sgtmelon.scriptum.data.room.converter.model

import androidx.room.TypeConverter
import java.util.*

/**
 * Converter from string to long array and vice versa
 */
class StringConverter {

    @TypeConverter fun toList(string: String): MutableList<Long> = ArrayList<Long>().apply {
        if (string != NONE && string.isNotEmpty()) {
            addAll(string.split(SPLIT.toRegex())
                    .dropLastWhile { it.isEmpty() }
                    .map { it.toLong() })
        }
    }

    @TypeConverter fun toString(list: List<Long>?): String {
        return if (list.isNullOrEmpty()) NONE else list.joinToString(SPLIT)
    }

    companion object {
        const val SPLIT = ", "
        const val NONE = "NONE"
    }

}
package sgtmelon.scriptum.infrastructure.converter.types

import androidx.room.TypeConverter

/**
 * Converter from string to long array and vice versa
 */
// TODO #ROOM rename
class StringConverter {

    @TypeConverter
    fun toList(string: String): MutableList<Long> = ArrayList<Long>().apply {
        if (string != EMPTY && string.isNotEmpty()) {
            addAll(string.split(DIVIDER.toRegex())
                .dropLastWhile { it.isEmpty() }
                .mapNotNull { it.toLongOrNull() })
        }
    }

    @TypeConverter
    fun toString(list: List<Long>?): String {
        return if (list.isNullOrEmpty()) EMPTY else list.joinToString(DIVIDER)
    }

    companion object {
        const val DIVIDER = ", "
        const val EMPTY = "NONE"
    }
}
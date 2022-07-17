package sgtmelon.scriptum.cleanup.data.room.converter.type

import androidx.room.TypeConverter

/**
 * Converter from string to long array and vice versa
 */
// TODO #ROOM rename
class StringConverter {

    @TypeConverter fun toList(string: String): MutableList<Long> = ArrayList<Long>().apply {
        if (string != NONE && string.isNotEmpty()) {
            addAll(string.split(SPLIT.toRegex())
                    .dropLastWhile { it.isEmpty() }
                    .mapNotNull { it.toLongOrNull() })
        }
    }

    @TypeConverter fun toString(list: List<Long>?): String {
        return if (list.isNullOrEmpty()) NONE else list.joinToString(SPLIT)
    }

    companion object {
        // TODO rename -> DIVIDER
        const val SPLIT = ", "
        // TODO rename -> EMPTY
        const val NONE = "NONE"
    }
}
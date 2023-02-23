package sgtmelon.scriptum.infrastructure.converter.types

import androidx.room.TypeConverter
import sgtmelon.extensions.clearSplit

/**
 * Converter from long list to string and vice versa.
 */
class LongListConverter {

    @TypeConverter fun toList(value: String): MutableList<Long> {
        val list = mutableListOf<Long>()

        if (value.isNotEmpty() && value != EMPTY) {
            list.addAll(value.clearSplit(DIVIDER).mapNotNull { it.toLongOrNull() })
        }

        return list
    }

    @TypeConverter fun toString(value: List<Long>?): String {
        return value?.takeIf { it.isNotEmpty() }
            ?.joinToString(DIVIDER)
            ?: EMPTY
    }

    companion object {
        const val DIVIDER = ", "
        const val EMPTY = "NONE"
    }
}